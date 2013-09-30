package com.github.configurationstub.configuration;

import com.github.configurationstub.MethodUtil;
import com.github.configurationstub.MethodWrapper;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.lang.reflect.Modifier;

/**
 * User: zhaohuiyu
 * Date: 11/8/12
 */
public class MethodMeta extends ElementMeta {
    private String methodName;
    private Integer parameterSize;
    private String[] parameterTypeNames;
    private String returnType;
    private String[] parameterNames;
    private int modifiers;

    public MethodMeta(String name) {
        super(name);
        parse(name);
    }

    public boolean match(MethodWrapper method) {
        if (!this.methodName.equals(method.getName())) return false;
        String[] parameterTypeNames = method.getParameterTypeNames();
        if (parameterSize != null && !parameterSize.equals(parameterTypeNames.length)) return false;
        return isParamTypeMatch(parameterTypeNames);
    }

    private boolean isParamTypeMatch(String[] parameterTypeNames) {
        if (this.parameterTypeNames == null) return true;
        for (int i = 0; i < parameterTypeNames.length; ++i) {
            if (!parameterTypeNames[i].equals(this.parameterTypeNames[i])) {
                return false;
            }
        }
        return true;
    }

    private String parse(String name) {
        if (name.contains("/")) {
            int index = name.indexOf("/");
            this.methodName = name.substring(0, index);
            this.parameterSize = Integer.parseInt(name.substring(index + 1));
        }
        if (name.contains("(") && name.endsWith(")")) {
            int index = name.indexOf("(");
            this.methodName = name.substring(0, index);
            parseParameters(name.substring(index + 1, name.lastIndexOf(")")));
        }
        return name;
    }

    private void parseParameters(String parameterString) {
        this.parameterTypeNames = StringUtils.split(parameterString, ",");
        this.parameterSize = this.parameterTypeNames.length;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append(this.methodName)
                .append(this.parameterSize)
                .append(this.parameterTypeNames)
                .toString();
    }

    public void setMethod(CtMethod method) throws NotFoundException {
        this.returnType = method.getReturnType().getName();
        this.parameterNames = MethodUtil.getParameterNames(method);
        this.modifiers = method.getModifiers();
    }

    public String getReturnType() {
        return returnType;
    }

    public String[] getParameterTypes() {
        return this.parameterTypeNames;
    }

    public String[] getParameterNames() {
        return parameterNames;
    }

    public int getModifiers() {
        return modifiers;
    }

    public String code() {
        StringBuilder sb = new StringBuilder();
        sb.append("java.lang.reflect.Method method = ");
        sb.append("com.qunar.autotest.mock.Invoker.getMethod(\"")
                .append(this.parent.getName())
                .append("\",\"")
                .append(this.methodName)
                .append("\");\n");
        sb.append(String.format("com.qunar.autotest.mock.Invoker invoker = new %s();\n", getInvoker()));
        sb.append("java.util.Map args = new java.util.HashMap();\n");
        for (int i = 0; i < this.parameterNames.length; ++i) {
            sb.append(String.format("args.put(\"%s\",%s);\n", this.parameterNames[i], this.parameterNames[i]));
        }
        if (Modifier.isStatic(modifiers)) {
            sb.append("Object o = invoker.invoke(null,method, args);\n");
        } else {
            sb.append("Object o = invoker.invoke(this,method, args);\n");
        }
        return sb.toString();
    }

    public String getMethodName() {
        return this.methodName;
    }
}
