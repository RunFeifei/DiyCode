package com.dianrong.crnetwork.host;

import android.support.annotation.NonNull;

import com.dianrong.crnetwork.NetworkFactory;

import java.lang.reflect.Method;

import util.Strings;

/**
 * Created by PengFeifei on 17-4-18.
 */

public class BaseUrlBindHelper {

    @ServerType
    private static int selectedServerType = ServerType.PRODUCT;

    /**
     * 只重置ServerType:Product or Demo or...
     */
    public static void resetBaseUrl(@ServerType int selectedServerType) {
        BaseUrlBindHelper.selectedServerType = selectedServerType;
    }

    /**
     * 重置baseurl,直接将baseurl设置到retrofit中,此时serverType将不起作用
     */
    public static void resetBaseUrl(@NonNull String baseUrl) {
        if (Strings.isEmpty(baseUrl) || !baseUrl.startsWith("http://") && !baseUrl.startsWith("https://")) {
            throw new IllegalStateException("baseUrl is illegal: " + baseUrl);
        }
        NetworkFactory.resetBaseUrl(baseUrl);
    }

    /**
     * 重置只重置ServerType,同时根据serviceClass重置host
     * 例如:重置成信途的Demo host,或者重置成点融的Product host
     */
    public static <S> void resetBaseUrl(@ServerType int selectedServerType, Class<S> serviceClass) {
        BaseUrlBindHelper.selectedServerType = selectedServerType;
        NetworkFactory.resetBaseUrl(BaseUrlBindHelper.getClassHost(serviceClass));
    }


    public static <S> String getClassHost(Class<S> serviceClass) {
        ClassHostMap classHostMap = serviceClass.getAnnotation(ClassHostMap.class);
        if (classHostMap == null || Strings.isEmpty(classHostMap.PRODUCT()) ||
                !classHostMap.PRODUCT().startsWith("http://") && !classHostMap.PRODUCT().startsWith("https://")) {
            throw new IllegalStateException("Atleast has valid PRODUCT host Annotation in " + serviceClass.getSimpleName());
        }
        int type = BaseUrlBindHelper.selectedServerType;
        if (type == ServerType.PRODUCT) {
            return classHostMap.PRODUCT();
        }
        if (type == ServerType.DEMO) {
            return classHostMap.DEMO();
        }
        if (type == ServerType.DEV) {
            return classHostMap.DEV();
        }
        throw new IllegalStateException("getClassHost failed");
    }

    public static String getMethodHost(Method method) {
        MethodHostMap methodHostMap = method.getAnnotation(MethodHostMap.class);
        if (methodHostMap == null || Strings.isEmpty(methodHostMap.PRODUCT()) ||
                !methodHostMap.PRODUCT().startsWith("http://") && !methodHostMap.PRODUCT().startsWith("https://")) {
            throw new IllegalStateException("Atleast has valid PRODUCT host Annotation in " + methodHostMap.PRODUCT());
        }
        int type = BaseUrlBindHelper.selectedServerType;
        if (type == ServerType.PRODUCT) {
            return methodHostMap.PRODUCT();
        }
        if (type == ServerType.DEMO) {
            return methodHostMap.DEMO();
        }
        if (type == ServerType.DEV) {
            return methodHostMap.DEV();
        }
        throw new IllegalStateException("getMethodHost failed");
    }

}
