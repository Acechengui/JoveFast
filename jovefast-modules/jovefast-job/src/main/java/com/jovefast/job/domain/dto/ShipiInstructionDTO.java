package com.jovefast.job.domain.dto;

import java.io.Serializable;

public class ShipiInstructionDTO implements Serializable {

    private static final long serialVersionUID = 1023164734612393193L;
    private Long rkey;
    private String po;
    private String mo;
    private String custPn;
    private String mfg;
    private String shipQty;
    private String factoryName;
    private String shipDate;
    private String custCode;
    private String shipMethod;
    private String shipMethodDesc;
    private String remark;
    private String initName;
    private String launchDate;
    private String launchTime;
    private String shipStatus;
    private String endInitName;
    private String endLaunchDate;
    private String endLaunchTime;
    private String packRemark;
    private String d60RKEY;
    private String shipNum;
    private String invoiceNum;
    private String expressNum;
    private String shippedQty;
    private String restartRemark;
    private String transferPlantRemark;

    @Override
    public String toString() {
        return "ShipiInstructionDTO{" +
                "rkey=" + rkey +
                ", po='" + po + '\'' +
                ", mo='" + mo + '\'' +
                ", custPn='" + custPn + '\'' +
                ", mfg='" + mfg + '\'' +
                ", shipQty='" + shipQty + '\'' +
                ", factoryName='" + factoryName + '\'' +
                ", shipDate='" + shipDate + '\'' +
                ", custCode='" + custCode + '\'' +
                ", shipMethod='" + shipMethod + '\'' +
                ", shipMethodDesc='" + shipMethodDesc + '\'' +
                ", remark='" + remark + '\'' +
                ", initName='" + initName + '\'' +
                ", launchDate='" + launchDate + '\'' +
                ", launchTime='" + launchTime + '\'' +
                ", shipStatus='" + shipStatus + '\'' +
                ", endInitName='" + endInitName + '\'' +
                ", endLaunchDate='" + endLaunchDate + '\'' +
                ", endLaunchTime='" + endLaunchTime + '\'' +
                ", packRemark='" + packRemark + '\'' +
                ", d60RKEY='" + d60RKEY + '\'' +
                ", shipNum='" + shipNum + '\'' +
                ", invoiceNum='" + invoiceNum + '\'' +
                ", expressNum='" + expressNum + '\'' +
                ", shippedQty='" + shippedQty + '\'' +
                ", restartRemark='" + restartRemark + '\'' +
                ", transferPlantRemark='" + transferPlantRemark + '\'' +
                '}';
    }

    public Long getRkey() {
        return rkey;
    }

    public void setRkey(Long rkey) {
        this.rkey = rkey;
    }

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getMo() {
        return mo;
    }

    public void setMo(String mo) {
        this.mo = mo;
    }

    public String getCustPn() {
        return custPn;
    }

    public void setCustPn(String custPn) {
        this.custPn = custPn;
    }

    public String getMfg() {
        return mfg;
    }

    public void setMfg(String mfg) {
        this.mfg = mfg;
    }

    public String getShipQty() {
        return shipQty;
    }

    public void setShipQty(String shipQty) {
        this.shipQty = shipQty;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getShipDate() {
        return shipDate;
    }

    public void setShipDate(String shipDate) {
        this.shipDate = shipDate;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getShipMethod() {
        return shipMethod;
    }

    public void setShipMethod(String shipMethod) {
        this.shipMethod = shipMethod;
    }

    public String getShipMethodDesc() {
        return shipMethodDesc;
    }

    public void setShipMethodDesc(String shipMethodDesc) {
        this.shipMethodDesc = shipMethodDesc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInitName() {
        return initName;
    }

    public void setInitName(String initName) {
        this.initName = initName;
    }

    public String getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(String launchDate) {
        this.launchDate = launchDate;
    }

    public String getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(String launchTime) {
        this.launchTime = launchTime;
    }

    public String getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(String shipStatus) {
        this.shipStatus = shipStatus;
    }

    public String getEndInitName() {
        return endInitName;
    }

    public void setEndInitName(String endInitName) {
        this.endInitName = endInitName;
    }

    public String getEndLaunchDate() {
        return endLaunchDate;
    }

    public void setEndLaunchDate(String endLaunchDate) {
        this.endLaunchDate = endLaunchDate;
    }

    public String getEndLaunchTime() {
        return endLaunchTime;
    }

    public void setEndLaunchTime(String endLaunchTime) {
        this.endLaunchTime = endLaunchTime;
    }

    public String getPackRemark() {
        return packRemark;
    }

    public void setPackRemark(String packRemark) {
        this.packRemark = packRemark;
    }

    public String getD60RKEY() {
        return d60RKEY;
    }

    public void setD60RKEY(String d60RKEY) {
        this.d60RKEY = d60RKEY;
    }

    public String getShipNum() {
        return shipNum;
    }

    public void setShipNum(String shipNum) {
        this.shipNum = shipNum;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getExpressNum() {
        return expressNum;
    }

    public void setExpressNum(String expressNum) {
        this.expressNum = expressNum;
    }

    public String getShippedQty() {
        return shippedQty;
    }

    public void setShippedQty(String shippedQty) {
        this.shippedQty = shippedQty;
    }

    public String getRestartRemark() {
        return restartRemark;
    }

    public void setRestartRemark(String restartRemark) {
        this.restartRemark = restartRemark;
    }

    public String getTransferPlantRemark() {
        return transferPlantRemark;
    }

    public void setTransferPlantRemark(String transferPlantRemark) {
        this.transferPlantRemark = transferPlantRemark;
    }
}
