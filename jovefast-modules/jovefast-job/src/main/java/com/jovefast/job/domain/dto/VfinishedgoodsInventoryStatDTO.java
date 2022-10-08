package com.jovefast.job.domain.dto;

import java.io.Serializable;

/**
 * @Description 成品库存统计
 * @Author Acechengui
 * @Date Created in 2022/9/24
 */
public class VfinishedgoodsInventoryStatDTO implements Serializable {
    private static final long serialVersionUID = -2234394001545416974L;

    private Integer d53Ptr;
    private Integer d16Rkey;
    private String salesPartNumber;
    private String surface;
    private String workOrderNumber;
    private Integer d50Reky;
    private Integer shelfLife;
    private String customerPartNumber;
    private String customerPartDesc;
    private String custCode;
    private String customerName;
    private String analysisCode3;
    private String prodCycle;
    private String schedate;
    private String mfgDate;
    private Integer prodPartNumber;
    private Integer qtyOnHand;
    private String location;
    private Double sumQty;
    private Double setl;
    private Double setw;
    private Double pcsSet;
    private String areaFg;
    private String expireDate;
    private String holdStatus;
    private String note1;
    private String notePadLine1;

    public Integer getD53Ptr() {
        return d53Ptr;
    }

    public void setD53Ptr(Integer d53Ptr) {
        this.d53Ptr = d53Ptr;
    }

    public Integer getD16Rkey() {
        return d16Rkey;
    }

    public void setD16Rkey(Integer d16Rkey) {
        this.d16Rkey = d16Rkey;
    }

    public String getSalesPartNumber() {
        return salesPartNumber;
    }

    public void setSalesPartNumber(String salesPartNumber) {
        this.salesPartNumber = salesPartNumber;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }

    public Integer getD50Reky() {
        return d50Reky;
    }

    public void setD50Reky(Integer d50Reky) {
        this.d50Reky = d50Reky;
    }

    public Integer getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Integer shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getCustomerPartNumber() {
        return customerPartNumber;
    }

    public void setCustomerPartNumber(String customerPartNumber) {
        this.customerPartNumber = customerPartNumber;
    }

    public String getCustomerPartDesc() {
        return customerPartDesc;
    }

    public void setCustomerPartDesc(String customerPartDesc) {
        this.customerPartDesc = customerPartDesc;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAnalysisCode3() {
        return analysisCode3;
    }

    public void setAnalysisCode3(String analysisCode3) {
        this.analysisCode3 = analysisCode3;
    }

    public String getProdCycle() {
        return prodCycle;
    }

    public void setProdCycle(String prodCycle) {
        this.prodCycle = prodCycle;
    }

    public String getSchedate() {
        return schedate;
    }

    public void setSchedate(String schedate) {
        this.schedate = schedate;
    }

    public String getMfgDate() {
        return mfgDate;
    }

    public void setMfgDate(String mfgDate) {
        this.mfgDate = mfgDate;
    }

    public Integer getProdPartNumber() {
        return prodPartNumber;
    }

    public void setProdPartNumber(Integer prodPartNumber) {
        this.prodPartNumber = prodPartNumber;
    }

    public Integer getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(Integer qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getSumQty() {
        return sumQty;
    }

    public void setSumQty(Double sumQty) {
        this.sumQty = sumQty;
    }

    public Double getSetl() {
        return setl;
    }

    public void setSetl(Double setl) {
        this.setl = setl;
    }

    public Double getSetw() {
        return setw;
    }

    public void setSetw(Double setw) {
        this.setw = setw;
    }

    public Double getPcsSet() {
        return pcsSet;
    }

    public void setPcsSet(Double pcsSet) {
        this.pcsSet = pcsSet;
    }

    public String getAreaFg() {
        return areaFg;
    }

    public void setAreaFg(String areaFg) {
        this.areaFg = areaFg;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getHoldStatus() {
        return holdStatus;
    }

    public void setHoldStatus(String holdStatus) {
        this.holdStatus = holdStatus;
    }

    public String getNote1() {
        return note1;
    }

    public void setNote1(String note1) {
        this.note1 = note1;
    }

    public String getNotePadLine1() {
        return notePadLine1;
    }

    public void setNotePadLine1(String notePadLine1) {
        this.notePadLine1 = notePadLine1;
    }

    @Override
    public String toString() {
        return "VfinishedgoodsInventoryStatDTO{" +
                "d53Ptr=" + d53Ptr +
                ", d16Rkey=" + d16Rkey +
                ", salesPartNumber='" + salesPartNumber + '\'' +
                ", surface='" + surface + '\'' +
                ", workOrderNumber='" + workOrderNumber + '\'' +
                ", d50Reky=" + d50Reky +
                ", shelfLife=" + shelfLife +
                ", customerPartNumber='" + customerPartNumber + '\'' +
                ", customerPartDesc='" + customerPartDesc + '\'' +
                ", custCode='" + custCode + '\'' +
                ", customerName='" + customerName + '\'' +
                ", analysisCode3='" + analysisCode3 + '\'' +
                ", prodCycle='" + prodCycle + '\'' +
                ", schedate=" + schedate +
                ", mfgDate=" + mfgDate +
                ", prodPartNumber=" + prodPartNumber +
                ", qtyOnHand=" + qtyOnHand +
                ", location='" + location + '\'' +
                ", sumQty=" + sumQty +
                ", setl=" + setl +
                ", setw=" + setw +
                ", pcsSet=" + pcsSet +
                ", areaFg='" + areaFg + '\'' +
                ", expireDate=" + expireDate +
                ", holdStatus='" + holdStatus + '\'' +
                ", note1='" + note1 + '\'' +
                ", notePadLine1='" + notePadLine1 + '\'' +
                '}';
    }
}
