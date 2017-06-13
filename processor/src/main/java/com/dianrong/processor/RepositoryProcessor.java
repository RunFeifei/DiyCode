package com.dianrong.processor;

import com.google.auto.service.AutoService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import annotation.MethodHostSurpported;
import annotation.EscapeProcessorMap;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by Lei Guoting on 17-3-15.
 * NOTE:processor模块必须是独立的java module
 */
@AutoService(Processor.class)
public class RepositoryProcessor extends AbstractProcessor {
    private Filer filer;
    //在Gradle Console中查看消息
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new TreeSet<>(Arrays.asList(new String[] {
                GET.class.getCanonicalName(),
                POST.class.getCanonicalName(),
                PUT.class.getCanonicalName(),
                DELETE.class.getCanonicalName()
        }));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> getSet = roundEnv.getElementsAnnotatedWith(GET.class);
        Set<? extends Element> postSet = roundEnv.getElementsAnnotatedWith(POST.class);
        Set<? extends Element> putSet = roundEnv.getElementsAnnotatedWith(PUT.class);
        Set<? extends Element> deleteSet = roundEnv.getElementsAnnotatedWith(DELETE.class);

        final HashMap<String, Clazz> bucket = new HashMap<>();
        doProcess(getSet, bucket);
        doProcess(postSet, bucket);
        doProcess(putSet, bucket);
        doProcess(deleteSet, bucket);

        try {
            RepositoryGenerator.generateCode(bucket.values(), filer);
        } catch (Throwable e) {
            e.printStackTrace();
            messager.printMessage(Diagnostic.Kind.ERROR,
                    String.format("Some error %s)", e.toString()));
        }
        return true;
    }


    private void doProcess(Set<? extends Element> set, HashMap<String, Clazz> bucket) {
        Iterator<? extends Element> iterator = set.iterator();

        while (iterator.hasNext()) {
            Element element = iterator.next();
            if (element.getKind() != ElementKind.METHOD) {
                messager.printMessage(Diagnostic.Kind.ERROR,
                        String.format("Invalid annotation: Only METHOD can be annotated with %s)", element.getSimpleName()));
                return;
            }

            ExecutableElement executableElement = (ExecutableElement) element;
            TypeMirror returnType = executableElement.getReturnType();

            List<? extends VariableElement> variables = executableElement.getParameters();
            Method method = new Method(executableElement.getSimpleName().toString(), 3);
            method.setPrimaryReturnType(returnType);
            boolean isDiSanFangData = !returnType.toString().contains("DrRoot");
            method.setReturnClassName(isDiSanFangData ? parseReturnTypeNew(returnType.toString()) : parseReturnType(returnType
                    .toString()));
            // TODO: 17-5-5 在此不能识别crnetwork模块中的Host注解 造成在app模块中使用compile非apt方式引入此processor模块
            MethodHostSurpported methodHostMap = executableElement.getAnnotation(MethodHostSurpported.class);
            method.setAssignedMethodHost(methodHostMap != null && methodHostMap.Surpported());


            EscapeProcessorMap escapeProcessorMap = executableElement.getAnnotation(EscapeProcessorMap.class);
            if (escapeProcessorMap != null && escapeProcessorMap.Escape()) {
                iterator.remove();
                continue;
            }

            //入参
            for (VariableElement ve : variables) {
                if (!ve.asType().getKind().isPrimitive()) {
                    method.setParamsNotAllPrimitive(true);
                }
                method.addArgument(new Argument(ve));
            }

            TypeMirror clsTypeMirror = executableElement.getEnclosingElement().asType();
            String cls = clsTypeMirror.toString();
            Clazz clsInfo = bucket.get(cls);
            if (clsInfo == null) {
                clsInfo = new Clazz(clsTypeMirror, isDiSanFangData);
                bucket.put(cls, clsInfo);
            }
            clsInfo.addMethod(method);
        }
    }

    /**
     * 是带有完整包名的!!!
     * retrofit2.Call<com.example.crnetwork.dataformat.DrRoot<com.example.crnetwork.dataformat.DrList<com.example.root.okfit.net.bean.errors.ErrorItem>>>
     * 把Content的数据结构拿出来(带有完整包名)
     * Call<DrRoot<DrList<BreakerItem>>> -->DrList<BreakerItem>>
     * Call<DrRoot<Data>> -->Data
     */
    private static String parseReturnType(String primary) {
        final String startMark = "DrRoot<";
        int index = primary.lastIndexOf(startMark);
        if (index < 0) {
            return primary;
        }
        final String endMark = ">>";
        int beginIndex = index + startMark.length();
        int index2 = primary.lastIndexOf(endMark);
        return primary.substring(beginIndex, index2);
    }


    /**
     * 第三方的数据结构
     *
     * @param primary
     * @return
     */
    private static String parseReturnTypeNew(String primary) {
        int index = primary.indexOf("<", 1);
        if (index < 0) {
            return primary;
        }
        final String endMark = ">";
        int beginIndex = index + 1;
        int index2 = primary.lastIndexOf(endMark);
        return primary.substring(beginIndex, index2);
    }

}
