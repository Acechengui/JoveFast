package com.jovefast.mobile.domain.dto;

import java.util.List;

/**
 * @Description 首页统计图转化类
 * @Author Acechengui
 * @Date Created in 2023/5/19
 */
public class CategoriesDTO {
    private String name;
    private List<Integer> data;
    private String type;
    private String style;
    private String color;

    public CategoriesDTO() {
    }

    public CategoriesDTO(String name, List<Integer> data, String type, String style, String color) {
        this.name = name;
        this.data = data;
        this.type = type;
        this.style = style;
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
    public List<Integer> getData() {
        return data;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public void setStyle(String style) {
        this.style = style;
    }
    public String getStyle() {
        return style;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public String getColor() {
        return color;
    }
}
