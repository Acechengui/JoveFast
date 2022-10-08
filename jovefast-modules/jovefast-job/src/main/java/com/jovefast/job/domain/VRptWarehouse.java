package com.jovefast.job.domain;

public class VRptWarehouse {

    /** 类目 */
    private String productName;

    /** 物料类目代码 */
    private String productCode;

    /** 物料编码 */
    private String invPartNumber;

    /** 物料描述 */
    private String invPartDescription;

    /** 所在区域 */
    private String location;

    /** 入/出库日期 */
    private String tdate;

    /** 开始日 */
    private String time1;

    /** 截至日 */
    private String time2;

    /** 入、出库数量 */
    private Double qtyIssued;

    /** 本币总价 */
    private Double pricesum;

    /** 占比 */
    private Double proportion;

    /** 单位 */
    private String unitName;

    /** 查询代码（用来区分需要过滤哪些数据） */
    private String queryCode;

    public VRptWarehouse(){}

    public VRptWarehouse(String productName, Double qtyIssued, Double pricesum, Double proportion) {
        this.productName = productName;
        this.qtyIssued = qtyIssued;
        this.pricesum = pricesum;
        this.proportion = proportion;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getInvPartNumber() {
        return invPartNumber;
    }

    public void setInvPartNumber(String invPartNumber) {
        this.invPartNumber = invPartNumber;
    }

    public String getInvPartDescription() {
        return invPartDescription;
    }

    public void setInvPartDescription(String invPartDescription) {
        this.invPartDescription = invPartDescription;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTdate() {
        return tdate;
    }

    public void setTdate(String tdate) {
        this.tdate = tdate;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public Double getQtyIssued() {
        return qtyIssued;
    }

    public void setQtyIssued(Double qtyIssued) {
        this.qtyIssued = qtyIssued;
    }

    public Double getPricesum() {
        return pricesum;
    }

    public void setPricesum(Double pricesum) {
        this.pricesum = pricesum;
    }

    public Double getProportion() {
        return proportion;
    }

    public void setProportion(Double proportion) {
        this.proportion = proportion;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getQueryCode() {
        return queryCode;
    }

    public void setQueryCode(String queryCode) {
        this.queryCode = queryCode;
    }
}
