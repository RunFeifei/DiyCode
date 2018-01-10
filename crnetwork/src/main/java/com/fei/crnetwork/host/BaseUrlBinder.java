package com.fei.crnetwork.host;

import android.support.annotation.NonNull;

import com.fei.crnetwork.request.ClientBuilder;
import com.fei.root.common.Strings;

import java.lang.reflect.Method;


/**
 * Created by PengFeifei on 17-4-18.
 * 重新绑定baseUrl
 * optionA:重新初始化retrofit,因为retrofit的baseUrl只能通过Builder设置,而Builder只能new出来
 * optionB:通过retrofit提供的@Url注解提供完整的域名,详见http://www.jianshu.com/p/4268e434150a
 * optionA开销大 optionB代码书写不友好,在此采用的optionA
 */

public class BaseUrlBinder {

    @ServerType
    private static int selectedServerType = ServerType.PRODUCT;

    private static String baseUrl;

    public static void initBaseUrl(String baseUrl) {
        if (Strings.isEmpty(baseUrl) || !baseUrl.startsWith("http://") && !baseUrl.startsWith("https://")) {
            throw new IllegalStateException("baseUrl is illegal: " + baseUrl);
        }
        BaseUrlBinder.baseUrl = baseUrl;
    }

    /**
     * 只重置ServerType:Product or Demo or...
     */
    public static void resetBaseUrl(@ServerType int selectedServerType) {
        BaseUrlBinder.selectedServerType = selectedServerType;
    }

    /**
     * 重置baseurl,直接将baseurl设置到retrofit中,此时serverType将不起作用
     */
    public static void resetBaseUrl(@NonNull String baseUrl) {
        if (Strings.isEmpty(baseUrl) || !baseUrl.startsWith("http://") && !baseUrl.startsWith("https://")) {
            throw new IllegalStateException("baseUrl is illegal: " + baseUrl);
        }
        ClientBuilder.resetBaseUrl(baseUrl);
    }

    /**
     * 重置只重置ServerType,同时根据serviceClass重置host
     */
    public static <S> void resetBaseUrl(@ServerType int selectedServerType, Class<S> serviceClass) {
        BaseUrlBinder.selectedServerType = selectedServerType;
        ClientBuilder.resetBaseUrl(BaseUrlBinder.getClassHost(serviceClass));
    }

    public static <S> String getClassHost(Class<S> serviceClass) {
        ClassHost classHost = serviceClass.getAnnotation(ClassHost.class);
        if (classHost == null && !Strings.isEmpty(BaseUrlBinder.baseUrl)) {
            return BaseUrlBinder.baseUrl;
        }
        if (Strings.isEmpty(classHost.PRODUCT()) ||
                !classHost.PRODUCT().startsWith("http://") && !classHost.PRODUCT().startsWith("https://")) {
            throw new IllegalStateException("Atleast has valid PRODUCT host Annotation in " + serviceClass.getSimpleName());
        }
        int type = BaseUrlBinder.selectedServerType;
        if (type == ServerType.PRODUCT) {
            return classHost.PRODUCT();
        }
        if (type == ServerType.DEMO) {
            return classHost.DEMO();
        }
        if (type == ServerType.DEV) {
            return classHost.DEV();
        }

        throw new IllegalStateException("getClassHost failed");
    }

    public static String getMethodHost(Method method) {
        MethodHost methodHost = method.getAnnotation(MethodHost.class);
        if (methodHost == null || Strings.isEmpty(methodHost.PRODUCT()) ||
                !methodHost.PRODUCT().startsWith("http://") && !methodHost.PRODUCT().startsWith("https://")) {
            throw new IllegalStateException("Atleast has valid PRODUCT host Annotation in " + methodHost.PRODUCT());
        }
        int type = BaseUrlBinder.selectedServerType;
        if (type == ServerType.PRODUCT) {
            return methodHost.PRODUCT();
        }
        if (type == ServerType.DEMO) {
            return methodHost.DEMO();
        }
        if (type == ServerType.DEV) {
            return methodHost.DEV();
        }
        throw new IllegalStateException("getMethodHost failed");
    }

    public static int getSelectedServerType() {
        return selectedServerType;
    }

    public static String getBaseUrl() {
        if (Strings.isEmpty(baseUrl)) {
            throw new RuntimeException("you should init baseUrl in Application init");
        }
        return baseUrl;
    }
}
