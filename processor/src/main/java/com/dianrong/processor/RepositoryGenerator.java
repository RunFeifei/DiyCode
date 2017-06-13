package com.dianrong.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;


/**
 * Created by Lei Guoting on 17-3-15.
 */
class RepositoryGenerator {

    //生成类的包名
    private static final String REPOSITORY_PACKAGE_NAME = "com.okfit.repository";
    //生成类的类名
    private static final String REPOSITORY_CLASS_TEMPLATE = "%1$sRepository";
    //用于存放包名和类名
    private final static String bufferStringSize2[] = new String[2];
    //末端数据结构
    private static TypeName contentDataType;

    // NOTE: 17-5-8 此处crnetwork模块的各类的包名是写死的 如果crnetwork类的包名变化此处需要手动修改
    private final static ClassName ResponseHandler = ClassName.get("com.dianrong.crnetwork.response", "ResponseHandler");
    private final static ClassName DrResponse = ClassName.get("com.dianrong.crnetwork.response", "DrResponse");
    private final static ClassName ResponseCallback = ClassName.get("com.dianrong.crnetwork.response", "ResponseCallback");
    private final static ClassName RequestHandler = ClassName.get("com.dianrong.crnetwork", "RequestHandler");
    private final static ClassName DrRoot = ClassName.get("com.dianrong.crnetwork.dataformat", "DrRoot");
    private final static ClassName DrList = ClassName.get("com.dianrong.crnetwork.dataformat", "DrList");
    private final static ClassName Call = ClassName.get("retrofit2", "Call");

    public static void generateCode(Collection<Clazz> bucket, Filer filer) throws IOException {
        if (bucket == null) {
            return;
        }
        TypeSpec.Builder classBuilder;
        ArrayList<TypeSpec> repositoryList = new ArrayList<>();
        for (Clazz cls : bucket) {
            ArrayList<Method> methodList = cls.getMethods();
            if (methodList == null) {
                continue;
            }
            classBuilder = TypeSpec.classBuilder(getRepositoryClassName(cls.getClassSimpleName()))//生成类名
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

            int size = methodList.size();
            for (int i = 0; i < size; i++) {
                Method method = methodList.get(i);
                TypeName modelClassType = getMethodReturnType(method.getReturnClassName(), cls.isDiSanFangData());

                //同步方法-->01
                MethodSpec.Builder mthBuild = MethodSpec.methodBuilder(method.getName())
                        .addModifiers(Modifier.PUBLIC)
                        .returns(modelClassType);//方法返回值
                addSyncMethodBody(method, mthBuild, cls, cls.isDiSanFangData());//方法体

                //方法入参
                ArrayList<Argument> args = method.getArguments();
                for (int j = 0; j < args.size(); j++) {
                    ParameterSpec parameterSpec = ParameterSpec.get(args.get(j).element);
                    mthBuild.addParameter(parameterSpec);
                }
                classBuilder.addMethod(mthBuild.build());

                if (!cls.isDiSanFangData()) {
                    //异步方法-->
                    MethodSpec.Builder mthBuild02 = MethodSpec.methodBuilder(method.getName())
                            .addModifiers(Modifier.PUBLIC)
                            .returns(void.class);//方法返回值
                    addAsyncMethodBody(method, mthBuild02, cls);//方法体

                    //方法入参
                    for (int j = 0; j < args.size(); j++) {
                        ParameterSpec parameterSpec = ParameterSpec.get(args.get(j).element);
                        mthBuild02.addParameter(parameterSpec);
                    }
                    ParameterSpec parameterSpec = ParameterSpec.builder(
                            ParameterizedTypeName.get(ResponseCallback, ParameterizedTypeName.get(DrRoot, modelClassType)),
                            "callback").build();
                    mthBuild02.addParameter(parameterSpec);

                    classBuilder.addMethod(mthBuild02.build());
                }

            }

            TypeSpec repository = classBuilder.build();
            repositoryList.add(repository);
            JavaFile javaFile = JavaFile.builder(REPOSITORY_PACKAGE_NAME, repository).build();
            javaFile.writeTo(filer);
        }
    }

    /**
     * 点融的数据结构时
     *
     * @param method
     * @param builder
     * @param cls
     */
    private static void generateCallStatement(Method method, MethodSpec.Builder builder, Clazz cls) {
        ClassName clazz = ClassName.get(cls.getPackageName(), cls.getClassSimpleName());
        boolean isListData = method.getReturnClassName().contains("<");

        if (!method.isAssignedMethodHost()) {
            String secondStatemet = "$T call = $T.getService($T.class)." + generateInvokeMethod(method);
            ParameterizedTypeName nameInSecond = isListData ?
                    ParameterizedTypeName.get(Call, ParameterizedTypeName.get(DrRoot, ParameterizedTypeName.get(DrList, contentDataType)))
                    : ParameterizedTypeName.get(Call, ParameterizedTypeName.get(DrRoot, contentDataType));

            builder.addStatement(secondStatemet, nameInSecond, RequestHandler, clazz);
        } else {
            int numOfParams = method.getArguments().size();
            String secondStatemet = "$T call =($T)$T.getMethodCall($T.class, \"" + method.getName() + "\", ";
            secondStatemet += numOfParams <= 0 ? "null)" : arguments2array(method.getArguments()) + ", new Class[] {";

            //前四个$T
            Object[] typeNames = new Object[numOfParams + 4];
            int tyNamesNum = 0;
            typeNames[0] = isListData ?
                    ParameterizedTypeName.get(Call, ParameterizedTypeName.get(DrRoot, ParameterizedTypeName.get(DrList, contentDataType)))
                    : ParameterizedTypeName.get(Call, ParameterizedTypeName.get(DrRoot, contentDataType));
            typeNames[1] = typeNames[0];
            typeNames[2] = RequestHandler;
            typeNames[3] = clazz;

            for (int i = 0; i < numOfParams; i++) {
                Argument argument = method.getArguments().get(i);
                if (argument.primaryType.getKind().isPrimitive()) {
                    secondStatemet += (argument.className + ".class");
                } else {
                    typeNames[tyNamesNum + 4] = parseClassName(argument.className);
                    tyNamesNum++;
                    secondStatemet += "$T.class";
                }
                if (i < numOfParams - 1) {
                    secondStatemet += ", ";
                }
            }
            secondStatemet += numOfParams <= 0 ? "" : "})";
            Object[] objs = new Object[tyNamesNum + 4];
            System.arraycopy(typeNames, 0, objs, 0, tyNamesNum + 4);
            builder.addStatement(secondStatemet, objs);
        }
    }

    /**
     * 第三方的数据结构时
     */
    private static void generateCallStatementNew(Method method, MethodSpec.Builder builder, Clazz cls) {
        ClassName clazz = ClassName.get(cls.getPackageName(), cls.getClassSimpleName());

        if (!method.isAssignedMethodHost()) {
            String secondStatemet = "$T call = $T.getService($T.class)." + generateInvokeMethod(method);

            ParameterizedTypeName nameInSecond = ParameterizedTypeName.get(Call, contentDataType);

            builder.addStatement(secondStatemet, nameInSecond, RequestHandler, clazz);
        } else {
            int numOfParams = method.getArguments().size();
            String secondStatemet = "$T call =($T)$T.getMethodCall($T.class, \"" + method.getName() + "\", ";
            secondStatemet += numOfParams <= 0 ? "null)" : arguments2array(method.getArguments()) + ", new Class[] {";

            //前四个$T
            Object[] typeNames = new Object[numOfParams + 4];
            int tyNamesNum = 0;
            typeNames[0] = ParameterizedTypeName.get(Call, contentDataType);
            typeNames[1] = typeNames[0];
            typeNames[2] = RequestHandler;
            typeNames[3] = clazz;

            for (int i = 0; i < numOfParams; i++) {
                Argument argument = method.getArguments().get(i);
                if (argument.primaryType.getKind().isPrimitive()) {
                    secondStatemet += (argument.className + ".class");
                } else {
                    typeNames[tyNamesNum + 4] = parseClassName(argument.className);
                    tyNamesNum++;
                    secondStatemet += "$T.class";
                }
                if (i < numOfParams - 1) {
                    secondStatemet += ", ";
                }
            }
            secondStatemet += numOfParams <= 0 ? "" : "})";
            Object[] objs = new Object[tyNamesNum + 4];
            System.arraycopy(typeNames, 0, objs, 0, tyNamesNum + 4);
            builder.addStatement(secondStatemet, objs);
        }
    }


    private static void addSyncMethodBody(Method method, MethodSpec.Builder builder, Clazz cls, boolean isDiSanFangData) {
        if (isDiSanFangData) {
            //第一行
            generateCallStatementNew(method, builder, cls);
            //第二行
            builder.addStatement("return ($T)$T.getSyncResponse(call).body()", contentDataType, ResponseHandler);

            return;
        }

        boolean isListData = method.getReturnClassName().contains("<");
        //第一行
        String firstatement = "$T drResponse = new $T<>()";
        ParameterizedTypeName nameInFirst = isListData ?
                ParameterizedTypeName.get(DrResponse, ParameterizedTypeName.get(DrRoot, ParameterizedTypeName.get(DrList, contentDataType)))
                : ParameterizedTypeName.get(DrResponse, ParameterizedTypeName.get(DrRoot, contentDataType));

        builder.addStatement(firstatement, nameInFirst, DrResponse);

        //第二行
        generateCallStatement(method, builder, cls);

        //第三行
        builder.addStatement("return drResponse.getContentData($T.getSyncResponse(call), call)", ResponseHandler);
    }


    private static void addAsyncMethodBody(Method method, MethodSpec.Builder builder, Clazz cls) {
        //第一行
        generateCallStatement(method, builder, cls);
        String secondStatemet = "call.enqueue(callback)";
        builder.addStatement(secondStatemet);
    }


    /**
     * 生成调用的方法
     */
    private static String generateInvokeMethod(Method method) {
        StringBuilder invkMthBuild = new StringBuilder();
        ArrayList<Argument> args = method.getArguments();
        int size = args.size();
        if (size > 0) {
            final String separator = ", ";
            invkMthBuild.append(method.getName()).append("(");
            for (int i = 0; i < size; i++) {
                invkMthBuild.append(args.get(i).name).append(separator);
            }
            int length = invkMthBuild.length();
            invkMthBuild.replace(length - separator.length(), length, "");
            invkMthBuild.append(")");
        } else {
            invkMthBuild.append(method.getName()).append("()");
        }
        return invkMthBuild.toString();
    }


    private static TypeName[] parseChildrenType(String savage) {
        if (savage == null) {
            return null;
        }
        int index = savage.indexOf(",");
        int levelIndex = savage.indexOf("<");
        TypeName[] typeArray;
        if (index == levelIndex) {
            //a
            typeArray = new TypeName[] {parseClassName(savage)};
        } else if (index == -1
                || (levelIndex > 0 && savage.contains("<"))) {
            //a<b>
            String parent = savage.substring(0, levelIndex);
            savage = savage.substring(levelIndex + 1, savage.length() - 1);
            typeArray = new TypeName[] {ParameterizedTypeName.get(parseClassName(parent), parseChildrenType(savage))};
        } else {
            //a, b, c
            ArrayList<TypeName> children = new ArrayList<>(4);
            int endIndex, nextPreIndex;
            String child;
            int length;
            do {
                endIndex = index;
                length = savage.length();
                child = savage.substring(0, endIndex);
                if (endIndex == length) {
                    index = -1;
                } else {
                    savage = savage.substring(endIndex + 1);
                    nextPreIndex = savage.indexOf("<");
                    index = savage.indexOf(",");
                    if (index == nextPreIndex) {
                        index = savage.length();
                    } else if (index == -1 || index > nextPreIndex) {
                        index = findNextBackEndIndex(savage, Math.max(index, 0));
                    }
                }
                children.add(parseClassName(child));
            } while (index > 0);
            typeArray = children.toArray(new TypeName[children.size()]);
        }
        return typeArray;
    }

    private static int findNextBackEndIndex(String savage, int fromIndex) {
        int index = savage.indexOf(">", fromIndex);
        if (index > 0) {
            int lastIndex = savage.length() - 1;
            do {
                index++;
            } while (index < lastIndex && savage.charAt(index) == '>');
        }
        return index;
    }

    /**
     * @param typeName Content的数据结构:DrList<BreakerItem>> or Data
     *                 有list包裹或者直接data
     *                 带有完整包名!!!
     *                 或者是Void
     */
    private static TypeName getMethodReturnType(String typeName, boolean isDisanfang) {
        int index = typeName.indexOf("<");
        if (index > 0) {
            String parentType = typeName.substring(0, index);
            String savage = typeName.substring(index + 1, typeName.length() - 1);
            TypeName[] names = parseChildrenType(savage);
            contentDataType = names[0];
            return ParameterizedTypeName.get(parseClassName(parentType), names);
        }
        ClassName className = parseClassName(typeName);
        contentDataType = className;
        if (isDisanfang) {
            return contentDataType;
        }
        return className;
    }


    /**
     * 根据包名和类名生成ClassName对象
     * ClassName并非代表类名而是JavaPoet的 ClassName.class类
     */
    private static ClassName parseClassName(String className) {
        if (className == null) {
            return null;
        }
        String buffer[] = bufferStringSize2;
        getPackageAndClassName(className.trim(), buffer);
        return ClassName.get(buffer[0], buffer[1]);
    }


    /**
     * pkgAndName[0]=包名
     * pkgAndName[1]=类名
     */
    private static void getPackageAndClassName(String className, String[] pkgAndName) {
        if (className == null || pkgAndName == null || pkgAndName.length < 2) {
            return;
        }

        int index = className.lastIndexOf(".");
        if (index < 0) {
            pkgAndName[0] = "";
            pkgAndName[1] = className;
        } else {
            pkgAndName[0] = className.substring(0, index);
            pkgAndName[1] = className.substring(index + 1, className.length());
        }
    }


    /**
     * 生成类名
     */
    private static String getRepositoryClassName(String apiClassName) {
        String newName = cutSuffix(apiClassName, "Api");
        if (newName == null) {
            newName = cutSuffix(apiClassName, "Request");
        }

        if (newName == null) {
            newName = apiClassName;
        }
        return String.format(REPOSITORY_CLASS_TEMPLATE, newName);
    }

    /**
     * 裁剪类名
     */
    private static String cutSuffix(String apiClassName, String suffix) {
        int index = apiClassName.lastIndexOf(suffix);
        if (index < 0) {
            return null;
        }
        return apiClassName.substring(0, index);
    }


    private static String arguments2array(ArrayList<Argument> list) {
        if (list == null) {
            return "null";
        }
        int length = list.size();
        if (length == 0) {
            return "null";
        }
        StringBuilder builder = new StringBuilder("new Object[] {");
        for (int i = 0; i < length; i++) {
            Argument argument = list.get(i);
            builder.append(argument.name);
            if (i < length - 1) {
                builder.append(", ");
            }
        }
        return builder.append("}").toString();
    }


}
