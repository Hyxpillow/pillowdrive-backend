package com.hyxpillow.pillowdrive.common.threadlocal;

public class AuthThreadLocal {
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();
    public static void set(String token) {
        threadLocal.set(token);
    }
    public static String get() {
        return threadLocal.get();
    }
    public static void remove() {
        threadLocal.remove();
    }
}
