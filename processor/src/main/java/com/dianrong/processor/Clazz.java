package com.dianrong.processor;


import java.util.ArrayList;

import javax.lang.model.type.TypeMirror;

import util.Collections;

/**
 * Created by Lei Guoting on 17-3-15.
 */
class Clazz {
    private String packageName;
    private String classSimpleName;
    private final String className;
    private final TypeMirror typeMirror;
    private final ArrayList<Method> methods;

    public Clazz(TypeMirror typeMirror) {
        this.typeMirror = typeMirror;
        String clsName = typeMirror.toString();
        this.className = clsName;
        setNameAndPackage(clsName);
        this.methods = new ArrayList<>(5);
    }

    private void setNameAndPackage(String className) {
        String clsName;
        String pkgName;
        int index = className.lastIndexOf(".");
        if (index < 0) {
            clsName = className;
            pkgName = "";
        } else {
            pkgName = className.substring(0, index);
            clsName = className.substring(index + 1, className.length());
        }

        this.classSimpleName = clsName;
        this.packageName = pkgName;
    }

    public TypeMirror getTypeMirror() {
        return typeMirror;
    }

    public void addMethod(Method method) {
        this.methods.add(method);
    }

    public ArrayList<Method> getMethods() {
        return this.methods;
    }

    public String getClassSimpleName() {
        return this.classSimpleName;
    }

    public String getClassName() {
        return this.className;
    }

    public String getPackageName() {
        return this.packageName;
    }

    @Override
    public int hashCode() {
        return this.className.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(32);
        builder.append(className);
        builder.append("{");
        int size = Collections.size(methods);
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                builder.append(methods.get(i)).append(";");
            }
        }
        builder.append("}");
        return builder.toString();
    }
}
