package com.dianrong.crnetwork.host;

import android.support.annotation.NonNull;

import com.dianrong.crnetwork.CrNetworkFactory;
import com.dianrong.crnetwork.host.dianrong.annotation.CrClassHostMap;
import com.dianrong.crnetwork.host.dianrong.annotation.DrClassHostMap;
import com.dianrong.crnetwork.host.dianrong.hosts.CreditRoadHosts;
import com.dianrong.crnetwork.host.dianrong.hosts.DianRongHosts;

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
        CrNetworkFactory.resetBaseUrl(baseUrl);
    }

    /**
     * 重置只重置ServerType,同时根据serviceClass重置host
     * 例如:重置成信途的Demo host,或者重置成点融的Product host
     */
    public static <S> void resetBaseUrl(@ServerType int selectedServerType, Class<S> serviceClass) {
        BaseUrlBindHelper.selectedServerType = selectedServerType;
        CrNetworkFactory.resetBaseUrl(BaseUrlBindHelper.checkClassHosts(serviceClass));
    }

    /**
     * 优先获取点融 & 信途的hosts
     * 同时指定了>=2 or <1 个是视作非法
     */
    public static <S> String checkClassHosts(Class<S> serviceClass) {
        int type = BaseUrlBindHelper.selectedServerType;
        DrClassHostMap drClassHostMap = serviceClass.getAnnotation(DrClassHostMap.class);
        CrClassHostMap crClassHostMap = serviceClass.getAnnotation(CrClassHostMap.class);
        ClassHostMap classHostMap = serviceClass.getAnnotation(ClassHostMap.class);
        if ((drClassHostMap != null && crClassHostMap != null)
                || (crClassHostMap != null && classHostMap != null)
                || (drClassHostMap != null && classHostMap != null)) {
            throw new IllegalStateException("multiple hosts assigned in " + serviceClass.getSimpleName());
        }

        if (drClassHostMap != null) {
            if (type == ServerType.PRODUCT) {
                return DianRongHosts.PRODUCT;
            }
            if (type == ServerType.DEMO) {
                return DianRongHosts.DEMO;
            }
            if (type == ServerType.DEV) {
                return DianRongHosts.DEV;
            }
        }

        if (crClassHostMap != null) {
            if (type == ServerType.PRODUCT) {
                return CreditRoadHosts.PRODUCT;
            }
            if (type == ServerType.DEMO) {
                return CreditRoadHosts.DEMO;
            }
            if (type == ServerType.DEV) {
                return CreditRoadHosts.DEV;
            }
        }

        if (classHostMap != null) {
            return getClassHost(serviceClass);
        }
        throw new IllegalStateException("no hosts assigned in " + serviceClass.getSimpleName());
    }


    private static <S> String getClassHost(Class<S> serviceClass) {
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

    public static int getSelectedServerType() {
        return selectedServerType;
    }
}
