package com.github.configurationstub;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * User: zhaohuiyu
 * Date: 11/9/12
 */
public class MethodWrapper {
    private String name;
    private String[] parameterTypeNames;

    public MethodWrapper(CtMethod method) {
        this.name = method.getName();
        CtClass[] parameterTypes;
        try {
            parameterTypes = method.getParameterTypes();
        } catch (NotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        this.parameterTypeNames = getParameterTypeNames(parameterTypes);
    }

    private String[] getParameterTypeNames(CtClass[] parameterTypes) {
        String[] parameterTypeNames = new String[parameterTypes.length];
        for (int i = 0; i < parameterTypeNames.length; ++i) {
            parameterTypeNames[i] = parameterTypes[i].getName();
        }
        return parameterTypeNames;
    }

    public String getName() {
        return name;
    }

    public String[] getParameterTypeNames() {
        return parameterTypeNames;
    }
}
