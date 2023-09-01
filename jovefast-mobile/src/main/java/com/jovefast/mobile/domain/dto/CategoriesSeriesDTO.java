package com.jovefast.mobile.domain.dto;

import java.util.List;

/**
 * @Description 首页统计图转化类
 * @Author Acechengui
 * @Date Created in 2023/5/19
 */
public class CategoriesSeriesDTO {
    private List<String> categories;
    private List<CategoriesDTO> series;

    public CategoriesSeriesDTO() {
    }

    public CategoriesSeriesDTO(List<String> categories, List<CategoriesDTO> series) {
        this.categories = categories;
        this.series = series;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
    public List<String> getCategories() {
        return categories;
    }

    public void setSeries(List<CategoriesDTO> series) {
        this.series = series;
    }
    public List<CategoriesDTO> getSeries() {
        return series;
    }
}
