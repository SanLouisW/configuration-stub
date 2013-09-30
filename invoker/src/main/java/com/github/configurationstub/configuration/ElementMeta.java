package com.github.configurationstub.configuration;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: zhaohuiyu
 * Date: 11/8/12
 */
public class ElementMeta {
    protected String name;

    protected String invoker;

    protected ElementMeta parent;

    public ElementMeta(String name) {
        this.name = name;
    }

    public void setInvoker(String invoker) {
        this.invoker = invoker;
    }

    public String getName() {
        return name;
    }

    public void setParent(ElementMeta parent) {
        this.parent = parent;
    }

    public String getInvoker() {
        if (this.invoker != null) return this.invoker;
        if (parent != null) return parent.getInvoker();
        return null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(this.name)
                .append(this.invoker)
                .append(this.parent)
                .toString();
    }
}
