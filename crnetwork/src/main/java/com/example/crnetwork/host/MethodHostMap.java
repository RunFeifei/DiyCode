package com.example.crnetwork.host;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by PengFeifei on 17-4-19.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodHostMap {
    String PRODUCT();
    String DEMO();
    String DEV();
}
