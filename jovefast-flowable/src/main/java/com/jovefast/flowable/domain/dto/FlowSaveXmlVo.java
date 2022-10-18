package com.jovefast.flowable.domain.dto;

import java.io.Serializable;

/**
 * @author Acecehgnui
 */
public class FlowSaveXmlVo implements Serializable {

    private static final long serialVersionUID = 1501653559723991306L;
    /**
     * 流程名称
     */
    private String name;

    /**
     * 流程分类
     */
    private String category;

    /**
     * xml 文件
     */
    private String xml;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }
}
