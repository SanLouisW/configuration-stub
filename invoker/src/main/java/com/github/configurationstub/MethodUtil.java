package com.github.configurationstub;

import javassist.CtMethod;
import javassist.NotFoundException;

public class MethodUtil {
    public static String[] getParameterNames(CtMethod ctMethod) throws NotFoundException {
        String[] argNames = new String[ctMethod.getParameterTypes().length];
        for (int i = 0; i < argNames.length; ++i) {
            argNames[i] = "arg" + i;
        }
        return argNames;
    }
}
