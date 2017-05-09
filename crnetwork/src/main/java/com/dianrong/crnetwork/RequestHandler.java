package com.dianrong.crnetwork;

import com.dianrong.crnetwork.host.BaseUrlBindHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import util.Arrays;

/**
 * Created by PengFeifei on 17-4-19.
 */

public class RequestHandler {

    public static <S> S getService(Class<S> serviceClass) {
        CrNetworkFactory.resetBaseUrl(BaseUrlBindHelper.checkClassHosts(serviceClass));
        return CrNetworkFactory.createService(serviceClass);
    }

    /**
     * 若某方法有单独的host,调用此方法
     *
     * @param methodName     method的方法名
     * @param parameters     requestMethod方法的参数
     * @param parameterTypes requestMethod方法的参数类型
     */
    public static Object getMethodCall(Class<?> serviceClass, String methodName, Object[] parameters, Class<?>... parameterTypes) {
        Method[] methods = serviceClass.getDeclaredMethods();
        Method requestMethod = RequestHandler.getMethod(serviceClass, methodName, parameterTypes);
        if (requestMethod == null) {
            return null;
        }
        CrNetworkFactory.resetBaseUrl(BaseUrlBindHelper.getMethodHost(requestMethod));
        for (Method method : methods) {
            if (method.equals(requestMethod)) {
                try {
                    method.setAccessible(true);
                    Object object;
                    if (Arrays.isEmpty(parameters)) {
                        object = method.invoke(CrNetworkFactory.createService(serviceClass));
                    } else {
                        object = method.invoke(CrNetworkFactory.createService(serviceClass), parameters);
                    }
                    return object;
                } catch (ClassCastException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.getTargetException().printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    private static <S> Method getMethod(Class<S> serviceClass, String methodName, Class<?>... parameterTypes) {
        try {
            if (Arrays.isEmpty(parameterTypes)) {
                return serviceClass.getMethod(methodName);
            }
            return serviceClass.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

}
