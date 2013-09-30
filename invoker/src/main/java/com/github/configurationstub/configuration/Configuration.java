package com.github.configurationstub.configuration;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * User: zhaohuiyu
 * Date: 11/8/12
 */
public class Configuration {
    private RootElementMeta ROOT = new RootElementMeta();

    private Map<String, ElementMeta> name2element = new HashMap<String, ElementMeta>();

    private final static String INVOKER_LABEL = "invoker";
    private final static String METHOD_SUFFIX = ".methods";

    public RootElementMeta configure(InputStream stream) {
        Properties properties = new Properties();
        try {
            properties.load(stream);
            doConfigure(properties);
        } catch (IOException e) {

        }
        return ROOT;
    }


    private void doConfigure(Properties properties) {
        configRootElement(properties);
        configClassElements(properties);
        configInvoker(properties);
    }

    private void configRootElement(Properties properties) {
        String value = properties.getProperty(INVOKER_LABEL);
        ROOT.setInvoker(value);
    }

    private void configClassElements(Properties properties) {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = entry.getKey().toString();
            if (key.endsWith(METHOD_SUFFIX)) {
                String className = key.substring(0, key.lastIndexOf("."));
                ClassMeta classMeta = new ClassMeta(className);
                ROOT.add(classMeta);
                name2element.put(className, classMeta);
                configMethods(classMeta, entry.getValue().toString());
            }
        }
    }

    private void configMethods(ClassMeta classMeta, String methodNameStr) {
        String[] methodNames = StringUtils.split(methodNameStr, "|");
        for (String methodName : methodNames) {
            String fullMethodName = String.format("%s.%s", classMeta.getName(), methodName);
            MethodMeta methodMeta = new MethodMeta(methodName);
            classMeta.addMethod(methodMeta);
            name2element.put(fullMethodName, methodMeta);
        }
    }

    private void configInvoker(Properties properties) {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = entry.getKey().toString();
            if (key.contains(".") && key.endsWith(INVOKER_LABEL)) {
                String name = key.substring(0, key.lastIndexOf("."));
                ElementMeta elementMeta = name2element.get(name);
                if (elementMeta == null) return;
                elementMeta.setInvoker(entry.getValue().toString());
            }
        }
    }
}
