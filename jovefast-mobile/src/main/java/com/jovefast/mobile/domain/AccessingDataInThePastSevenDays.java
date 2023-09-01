package com.jovefast.mobile.domain;

import java.io.Serializable;

/**
 * @Description 近七天访问数据
 * @Author Acechengui
 * @Date Created in 2023/5/19
 */
public class AccessingDataInThePastSevenDays implements Serializable {
    private static final long serialVersionUID = 8345990166147586565L;

    /**
     * 近七天的所在本月的日数
     */
    private String fromDay;

    /**
     * 访问数
     */
    private Integer visits;

    public String getFromDay() {
        return fromDay;
    }

    public void setFromDay(String fromDay) {
        this.fromDay = fromDay;
    }

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }
}
