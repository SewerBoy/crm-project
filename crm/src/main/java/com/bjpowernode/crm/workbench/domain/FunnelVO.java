package com.bjpowernode.crm.workbench.domain;

/**
 * @author ccstart
 * @Description
 * @create 2022-06-12 18:59
 */
public class FunnelVO {//用于生成json字符串给统计图表用
    private String value;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "FunnelVO{" +
                "value='" + value + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
