package com.jovefast.job.domain.dto;

import java.io.Serializable;

/**
 欠料清单
 */
public class DetailCustomerWipDTO implements Serializable {
    private static final long serialVersionUID = -1881093050404222850L;

    /**
     *MO号
     */
    private String salesorder;
    /**
     *工单
     */
    private String workorder;
    /**
     *计划交期
     */
    private String schdate;
    /**
     *组别
     */
    private String groupname;
    /**
     *客户代码
     */
    private String custcode;
    /**
     *需求数
     */
    private Double demandnumber;
    /**
     *净需求
     */
    private Double netdemand;
    /**
     *物料名称
     */
    private String productname;
    /**
     *部件号码
     */
    private String invpartnumber;
    /**
     *部件描述
     */
    private String partdescription;
    /**
     *生产型号
     */
    private String customerpartnumber;
    /**
     *库存数
     */
    private Double inventory;
    /**
     *有效库存
     */
    private Double avlstock;
    /**
     *厂1
     */
    private Integer plant1;
    /**
     *厂2
     */
    private Integer plant2;
    /**
     *厂3
     */
    private Integer plant3;
    /**
     *单位
     */
    private String unitname;
    /**
     *要求交期
     */
    private String vrt;
    /**
     *到货日期
     */
    private String vtt;
    /**
     *欠料状态  netdemand  avlstock
     */
    private String lowmaterialstatus;
    /**
     * 备注
     */
    private String remark;

    public String getSchdate() {
        return schdate;
    }

    public void setSchdate(String schdate) {
        this.schdate = schdate;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getCustcode() {
        return custcode;
    }

    public void setCustcode(String custcode) {
        this.custcode = custcode;
    }

    public String getSalesorder() {
        return salesorder;
    }

    public void setSalesorder(String salesorder) {
        this.salesorder = salesorder;
    }

    public String getWorkorder() {
        return workorder;
    }

    public void setWorkorder(String workorder) {
        this.workorder = workorder;
    }

    public Double getDemandnumber() {
        return demandnumber;
    }

    public void setDemandnumber(Double demandnumber) {
        this.demandnumber = demandnumber;
    }

    public Double getNetdemand() {
        return netdemand;
    }

    public void setNetdemand(Double netdemand) {
        this.netdemand = netdemand;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getInvpartnumber() {
        return invpartnumber;
    }

    public void setInvpartnumber(String invpartnumber) {
        this.invpartnumber = invpartnumber;
    }

    public String getPartdescription() {
        return partdescription;
    }

    public void setPartdescription(String partdescription) {
        this.partdescription = partdescription;
    }

    public String getCustomerpartnumber() {
        return customerpartnumber;
    }

    public void setCustomerpartnumber(String customerpartnumber) {
        this.customerpartnumber = customerpartnumber;
    }

    public Double getInventory() {
        return inventory;
    }

    public void setInventory(Double inventory) {
        this.inventory = inventory;
    }

    public Double getAvlstock() {
        return avlstock;
    }

    public void setAvlstock(Double avlstock) {
        this.avlstock = avlstock;
    }

    public Integer getPlant1() {
        return plant1;
    }

    public void setPlant1(Integer plant1) {
        this.plant1 = plant1;
    }

    public Integer getPlant2() {
        return plant2;
    }

    public void setPlant2(Integer plant2) {
        this.plant2 = plant2;
    }

    public Integer getPlant3() {
        return plant3;
    }

    public void setPlant3(Integer plant3) {
        this.plant3 = plant3;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public String getVrt() {
        return vrt;
    }

    public void setVrt(String vrt) {
        this.vrt = vrt;
    }

    public String getVtt() {
        return vtt;
    }

    public void setVtt(String vtt) {
        this.vtt = vtt;
    }

    public String getLowmaterialstatus() {
        return lowmaterialstatus;
    }

    public void setLowmaterialstatus(String lowmaterialstatus) {
        this.lowmaterialstatus = lowmaterialstatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "DetailCustomerWipDTO{" +
                "salesorder='" + salesorder + '\'' +
                ", workorder='" + workorder + '\'' +
                ", schdate='" + schdate + '\'' +
                ", groupname='" + groupname + '\'' +
                ", custcode='" + custcode + '\'' +
                ", demandnumber=" + demandnumber +
                ", netdemand=" + netdemand +
                ", productname='" + productname + '\'' +
                ", invpartnumber='" + invpartnumber + '\'' +
                ", partdescription='" + partdescription + '\'' +
                ", customerpartnumber='" + customerpartnumber + '\'' +
                ", inventory=" + inventory +
                ", avlstock=" + avlstock +
                ", plant1=" + plant1 +
                ", plant2=" + plant2 +
                ", plant3=" + plant3 +
                ", unitname='" + unitname + '\'' +
                ", vrt='" + vrt + '\'' +
                ", vtt='" + vtt + '\'' +
                ", lowmaterialstatus='" + lowmaterialstatus + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
