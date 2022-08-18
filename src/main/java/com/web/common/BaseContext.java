package com.web.common;

/**
 * ThreadLocal  获取用户id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal= new ThreadLocal<>();

    public static void setId(Long id){
        threadLocal.set(id);
    }

    public static long getId(){
        return threadLocal.get();
    }
}
