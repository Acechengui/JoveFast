package com.jovefast.job.domain.dto;

import java.io.Serializable;

/**
 * @author MuTouYSK
 * @date 2022/4/26
 * 导出模板 MaterialConsumptionTableModel 类的，采购物料消耗月表。
 */
public class MaterialConsumptionDTO implements Serializable {
    private static final long serialVersionUID = -2358373337871132485L;

    /**
     * 部门
     */
    private String department;
    /**
     * 产量
     */
    private Double yield;
    /**
     * 消耗
     */
    private Double consume;
    /**
     * 元
     */
    private Double money;
    /**
     * 月份
     */
    private int moon;
    /**
     * 厂别
     */
    private int plant;

    @Override
    public String toString() {
        return "MaterialConsumptionDTO{" +
                "department='" + department + '\'' +
                ", yield=" + yield +
                ", consume=" + consume +
                ", money=" + money +
                ", moon=" + moon +
                ", plant=" + plant +
                '}';
    }

    public double getMoney() {
        return this.consume / this.yield;
    }

    public int getPlant() {
        if (this.department.contains("1厂")) {
            return 1;
        }

        if (this.department.contains("2厂")) {
            return 2;
        }

        if (this.department.contains("3厂")) {
            return 3;
        }

        return 0;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Double getYield() {
        return yield;
    }

    public void setYield(Double yield) {
        this.yield = yield;
    }

    public Double getConsume() {
        return consume;
    }

    public void setConsume(Double consume) {
        this.consume = consume;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public int getMoon() {
        return moon;
    }

    public void setMoon(int moon) {
        this.moon = moon;
    }

    public void setPlant(int plant) {
        this.plant = plant;
    }
}
