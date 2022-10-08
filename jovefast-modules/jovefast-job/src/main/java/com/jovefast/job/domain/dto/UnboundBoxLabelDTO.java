package com.jovefast.job.domain.dto;

import java.io.Serializable;

/**
 * 未与货架绑定的入仓签
 *
 * @author Dijkstra
 */
public class UnboundBoxLabelDTO implements Serializable {

    private static final long serialVersionUID = -6613900930950793679L;

    /**
     * 厂别
     * 1：沙井厂；
     * 2：松岗厂；
     * 3：鹤山厂
     */
    private Integer plant;
    /**
     * MO
     */
    private String mo;
    /**
     * LOT号
     */
    private String lotNum;
    /**
     * 序列号
     */
    private String labelId;
    /**
     * 客户代码
     */
    private String custCode;
    /**
     * 客户名称
     */
    private String custName;
    /**
     * 客户型号
     */
    private String custPn;
    /**
     * 型号描述
     */
    private String custDesc;
    /**
     * 生产编号
     */
    private String mfg;
    /**
     * 数量
     */
    private Integer qty;
    /**
     * 拼板数
     */
    private String pcsSet;
    /**
     * 生产周期
     */
    private String dateCode;
    /**
     * 操作员
     */
    private String operator;
    /**
     * 打印日期
     */
    private String printDate;
    /**
     * 打印时间
     */
    private String printTime;
    /**
     * 客户版本
     */
    private String custVer;
    /**
     * 表面处理
     */
    private String surface;
    /**
     * 板厚
     */
    private String plateThick;

    public Integer getPlant() {
        return plant;
    }

    public void setPlant(Integer plant) {
        this.plant = plant;
    }

    public String getMo() {
        return mo;
    }

    public void setMo(String mo) {
        this.mo = mo;
    }

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustPn() {
        return custPn;
    }

    public void setCustPn(String custPn) {
        this.custPn = custPn;
    }

    public String getCustDesc() {
        return custDesc;
    }

    public void setCustDesc(String custDesc) {
        this.custDesc = custDesc;
    }

    public String getMfg() {
        return mfg;
    }

    public void setMfg(String mfg) {
        this.mfg = mfg;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getPcsSet() {
        return pcsSet;
    }

    public void setPcsSet(String pcsSet) {
        this.pcsSet = pcsSet;
    }

    public String getDateCode() {
        return dateCode;
    }

    public void setDateCode(String dateCode) {
        this.dateCode = dateCode;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getPrintDate() {
        return printDate;
    }

    public void setPrintDate(String printDate) {
        this.printDate = printDate;
    }

    public String getPrintTime() {
        return printTime;
    }

    public void setPrintTime(String printTime) {
        this.printTime = printTime;
    }

    public String getCustVer() {
        return custVer;
    }

    public void setCustVer(String custVer) {
        this.custVer = custVer;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getPlateThick() {
        return plateThick;
    }

    public void setPlateThick(String plateThick) {
        this.plateThick = plateThick;
    }

    @Override
    public String toString() {
        return "UnboundBoxLabelDTO{" +
                "plant=" + plant +
                ", mo='" + mo + '\'' +
                ", lotNum='" + lotNum + '\'' +
                ", labelId='" + labelId + '\'' +
                ", custCode='" + custCode + '\'' +
                ", custName='" + custName + '\'' +
                ", custPn='" + custPn + '\'' +
                ", custDesc='" + custDesc + '\'' +
                ", mfg='" + mfg + '\'' +
                ", qty=" + qty +
                ", pcsSet='" + pcsSet + '\'' +
                ", dateCode='" + dateCode + '\'' +
                ", operator='" + operator + '\'' +
                ", printDate='" + printDate + '\'' +
                ", printTime='" + printTime + '\'' +
                ", custVer='" + custVer + '\'' +
                ", surface='" + surface + '\'' +
                ", plateThick='" + plateThick + '\'' +
                '}';
    }
}
