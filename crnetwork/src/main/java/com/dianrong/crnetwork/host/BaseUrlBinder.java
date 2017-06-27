package com.dianrong.crnetwork.host;

import android.support.annotation.NonNull;

import com.dianrong.crnetwork.request.ClientBuilder;
import com.feifei.common.utils.Strings;

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
        ClassHostMap classHostMap = serviceClass.getAnnotation(ClassHostMap.class);
        if (classHostMap == null && !Strings.isEmpty(BaseUrlBinder.baseUrl)) {
            return BaseUrlBinder.baseUrl;
        }
        if (Strings.isEmpty(classHostMap.PRODUCT()) ||
                !classHostMap.PRODUCT().startsWith("http://") && !classHostMap.PRODUCT().startsWith("https://")) {
            throw new IllegalStateException("Atleast has valid PRODUCT host Annotation in " + serviceClass.getSimpleName());
        }
        int type = BaseUrlBinder.selectedServerType;
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
        int type = BaseUrlBinder.selectedServerType;
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
