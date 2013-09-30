package com.github.configurationstub;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * User: zhaohuiyu
 * Date: 8/23/12
 * Time: 10:33 PM
 */
public abstract class Invoker {

    public abstract Object invoke(Object self, Method method, Map<String, Object> args);

    public static Method getMethod(String className, String methodName) {
        Class clazz = forName(className);
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) return method;
        }
        return null;
    }

    private static Class forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
