package com.dianrong.processor;


import java.util.ArrayList;

import javax.lang.model.type.TypeMirror;

import util.Collections;
import util.Strings;

/**
 * Created by Lei Guoting on 17-3-15.
 */

class Method {
    private final String name;
    private final ArrayList<Argument> args;
    /**
     * Content的数据结构 带有完整包名
     * e.g.com.example.root.okfit.net.bean.errors.ContentData
     * or void
     */
    private String returnClassName;
    private String modifier;
    private TypeMirror primaryReturnType;
    private boolean assignedMethodHost;
    private boolean paramsNotAllPrimitive;

    public Method(String name, int numberArg) {
        if (numberArg < 0) {
            numberArg = 0;
        }

        this.name = name;
        args = new ArrayList<>(numberArg);
    }

    public void addArgument(Argument arg) {
        args.add(arg);
    }

    public ArrayList<Argument> getArguments() {
        return args;
    }

    public void setReturnClassName(String name) {
        this.returnClassName = name;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getReturnClassName() {
        return Strings.isEmpty(returnClassName) ? "void" : returnClassName;
    }

    public TypeMirror getPrimaryReturnType() {
        return primaryReturnType;
    }

    public void setPrimaryReturnType(TypeMirror primaryReturnType) {
        this.primaryReturnType = primaryReturnType;
    }

    public String getModifier() {
        return Strings.isEmpty(modifier) ? "public" : modifier;
    }

    public String getName() {
        return name;
    }

    public void setAssignedMethodHost(boolean assignedMethodHost) {
        this.assignedMethodHost = assignedMethodHost;
    }

    public boolean isAssignedMethodHost() {
        return assignedMethodHost;
    }

    public boolean isParamsAllPrimitive() {
        return !paramsNotAllPrimitive;
    }

    public void setParamsNotAllPrimitive(boolean paramsNotAllPrimitive) {
        this.paramsNotAllPrimitive = paramsNotAllPrimitive;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(32);
        builder.append(getModifier()).append(" ");
        builder.append(getReturnClassName()).append(" ");
        builder.append(name);
        builder.append("(");
        int size = Collections.size(args);
        if (size > 0) {
            final String separator = ", ";
            for (int i = 0; i < size; i++) {
                builder.append(args.get(i).toString()).append(separator);
            }
            int length = builder.length();
            builder.replace(length - separator.length(), length, "");
        }
        builder.append(")");
        return builder.toString();
    }
}
