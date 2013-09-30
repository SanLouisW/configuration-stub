package com.github.configurationstub.codegenerator;

import com.github.configurationstub.configuration.MethodMeta;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static java.lang.reflect.Modifier.*;

/**
 * User: zhaohuiyu
 * Date: 11/9/12
 */
public class MockMethodCodeGenerator extends MethodCodeGenerator {

    private String returnType;
    private String methodName;
    private String[] argNames;
    private String[] argTypes;
    private int modifiers;

    private final static Map<String, String> RETURN_EXPRESSIONS = new HashMap<String, String>();
    private MethodMeta method;

    static {
        RETURN_EXPRESSIONS.put("boolean", "return Boolean.valueOf(o.toString()).booleanValue();");
        RETURN_EXPRESSIONS.put("int", "return Integer.valueOf(o.toString()).intValue();");
        RETURN_EXPRESSIONS.put("double", "return Double.valueOf(o.toString()).doubleValue();");
        RETURN_EXPRESSIONS.put("float", "return Float.valueOf(o.toString()).floatValue();");
        RETURN_EXPRESSIONS.put("char", "return o.toString().charAt(0);");
        RETURN_EXPRESSIONS.put("long", "return Long.valueOf(o.toString()).longValue();");
        RETURN_EXPRESSIONS.put("short", "return Short.valueOf(o.toString()).shortValue();");
        RETURN_EXPRESSIONS.put("byte", "return Byte.valueOf(o.toString()).byteValue();");
    }

    public MockMethodCodeGenerator(MethodMeta method){
        this.method = method;
        this.returnType = method.getReturnType();
        this.methodName = method.getMethodName();
        this.argTypes = method.getParameterTypes();
        this.argNames = method.getParameterNames();
        this.modifiers = method.getModifiers();
    }

    @Override
    public String code() {
        StringBuilder sb = new StringBuilder();
        appendModifiers(sb);
        sb.append(this.returnType);
        sb.append(" ");
        sb.append(this.methodName);
        sb.append("(");
        appendParameters(sb);
        sb.append(")");
        sb.append("{");
        sb.append(method.code());
        appendReturnStatement(sb);
        sb.append("}");
        return sb.toString();
    }

    private void appendReturnStatement(StringBuilder sb) {
        if (!hasReturnValue()) return;
        String returnStatement = RETURN_EXPRESSIONS.get(returnType);
        if (StringUtils.isBlank(returnStatement)) {
            String objectReturnStatement = String.format("return (%s)o;", this.returnType);
            sb.append(objectReturnStatement);
        } else {
            sb.append(returnStatement);
        }
    }

    private boolean hasReturnValue() {
        return !this.returnType.equalsIgnoreCase("void");
    }

    private void appendModifiers(StringBuilder sb) {
        if (isFinal(this.modifiers)) {
            sb.append("final ");
        }
        if (isPrivate(this.modifiers)) {
            sb.append("private ");
        }
        if (isProtected(this.modifiers)) {
            sb.append("protected ");
        }
        if (isPublic(this.modifiers)) {
            sb.append("public ");
        }
        if (isStatic(this.modifiers)) {
            sb.append("static ");
        }
        if (isSynchronized(this.modifiers)) {
            sb.append("synchronized ");
        }
    }

    private void appendParameters(StringBuilder sb) {
        String[] parameterTypes = this.argTypes;
        for (int i = 0; i < this.argTypes.length; ++i) {
            appendParameter(parameterTypes, sb, i);
        }
    }

    private void appendParameter(String[] parameterTypes, StringBuilder sb, int i) {
        String parameterTypeName = parameterTypes[i];
        String parameterName = getArgName(i);
        sb.append(String.format("%s %s", parameterTypeName, parameterName));
        if (i < parameterTypes.length - 1)
            sb.append(",");
    }

    private String getArgName(int index) {
        return StringUtils.isNotBlank(argNames[index]) ? argNames[index] : "arg" + index;
    }
}
