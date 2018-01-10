package com.fei.crnetwork.framework.requests;

/**
 * 同步执行多个网络请求,解决嵌套问题
 */
public interface Requests<T> {
    T onRequests();
}