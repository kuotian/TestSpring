package com.hdu.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("some.service")
public class SomeServiceProperties {
    // 读取配置文件中的如下两个属性值
    // some.service.prefix
    // some.service.surfix
    private String prefix;
    private String surfix;

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSurfix(String surfix) {
        this.surfix = surfix;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSurfix() {
        return surfix;
    }
}
