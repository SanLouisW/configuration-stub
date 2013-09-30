package com.github.configurationstub.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zhaohuiyu
 * Date: 11/8/12
 */
public class RootElementMeta extends ElementMeta {
    private List<ClassMeta> classes = new ArrayList<ClassMeta>();

    public RootElementMeta() {
        super("ROOT");
        this.invoker = "com.qunar.autotest.com.qunar.autotest.mock.HttpInvoker";
    }

    public void add(ClassMeta classMeta) {
        classMeta.setParent(this);
        this.classes.add(classMeta);
    }

    public List<ClassMeta> getClasses() {
        return classes;
    }
}
