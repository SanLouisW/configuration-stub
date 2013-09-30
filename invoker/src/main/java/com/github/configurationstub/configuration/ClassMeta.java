package com.github.configurationstub.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zhaohuiyu
 * Date: 11/8/12
 */
public class ClassMeta extends ElementMeta {
    private List<MethodMeta> methods;

    public ClassMeta(String name) {
        super(name);
        this.methods = new ArrayList<MethodMeta>();
    }

    public void addMethod(MethodMeta method) {
        method.setParent(this);
        this.methods.add(method);
    }

    public List<MethodMeta> getMethods() {
        return methods;
    }

}
