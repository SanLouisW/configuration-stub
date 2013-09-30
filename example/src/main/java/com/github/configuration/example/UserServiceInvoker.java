package com.github.configuration.example;

import com.github.configurationstub.Invoker;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * User: zhaohuiyu
 * Date: 9/30/13
 * Time: 10:18 PM
 */
public class UserServiceInvoker extends Invoker {
    @Override
    public Object invoke(Object self, Method method, Map<String, Object> args) {
        System.out.println("stub for user service registerUeser method");
        System.out.println("args:");
        for (Map.Entry<String, Object> entry : args.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue().toString());
        }
        return null;
    }
}
