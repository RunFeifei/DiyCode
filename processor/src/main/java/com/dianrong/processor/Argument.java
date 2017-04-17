package com.dianrong.processor;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by Lei Guoting on 17-3-15.
 * f1(String test)-->className=java.lang.String name=test
 * f2(int test)-->className=int name=test
 */
class Argument {
    public final String name;
    public final String className;
    public final VariableElement element;
    public final TypeMirror primaryType;


    public Argument(VariableElement ve) {
        this.element = ve;
        this.name = ve.getSimpleName().toString();
        this.className = ve.asType().toString();
        this.primaryType=ve.asType();
    }

    @Override
    public String toString() {
        return String.format("%s %s", className, name);
    }
}
