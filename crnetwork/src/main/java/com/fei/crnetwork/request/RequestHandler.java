package com.fei.crnetwork.request;

import com.fei.crnetwork.host.BaseUrlBinder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by PengFeifei on 17-4-19.
 */

public class RequestHandler {

    public static <S> S getService(Class<S> serviceClass) {
        ClientBuilder.resetBaseUrl(BaseUrlBinder.getClassHost(serviceClass));
        return ClientBuilder.createService(serviceClass);
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
        ClientBuilder.resetBaseUrl(BaseUrlBinder.getMethodHost(requestMethod));
        for (Method method : methods) {
            if (method.equals(requestMethod)) {
                try {
                    method.setAccessible(true);
                    Object object;
                    if (parameters == null || parameters.length == 0) {
                        object = method.invoke(ClientBuilder.createService(serviceClass));
                    } else {
                        object = method.invoke(ClientBuilder.createService(serviceClass), parameters);
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
            if (parameterTypes == null || parameterTypes.length == 0) {
                return serviceClass.getMethod(methodName);
            }
            return serviceClass.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

}
