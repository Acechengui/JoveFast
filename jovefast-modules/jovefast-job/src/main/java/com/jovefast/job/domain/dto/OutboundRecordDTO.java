package com.jovefast.job.domain.dto;

import java.io.Serializable;

/**
 * @author MuTouYSK
 * @date 2022/4/26
 * 出库记录
 */
public class OutboundRecordDTO implements Serializable {
    private static final long serialVersionUID = 6893794020241130225L;

    /**
     * 领料单号
     */
    private String mirNo;
    /**
     * 配料单
     */
    private String wmNo;
    /**
     * MO号
     */
    private String mo;
    /**
     * PO号
     */
    private String poNumber;
    /**
     * 物料编码
     */
    private String partNumber;
    /**
     * 物料名称
     */
    private String partDescription;
    /**
     * 供应商
     */
    private String supplierName;
    /**
     * 领料部门
     */
    private String facNo;
    /**
     * 仓位
     */
    private String location;
    /**
     * 单位
     */
    private String unitName;
    /**
     * 出库数量
     */
    private Double qtyIssued;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 库存单位换算bom比例
     */
    private Integer ratio;
    /**
     * 币别码
     */
    private String currCode;
    /**
     * 币别
     */
    private String currName;
    /**
     * 单位价格
     */
    private Double priceCurr;
    /**
     * 汇率
     */
    private Integer toOther;
    /**
     * 本币单价
     */
    private Double price;
    /**
     * 本币总价
     */
    private Double priceSum;
    /**
     * 时间
     */
    private String dateRequired;
    /**
     * 税率
     */
    private Double taxRate;
    /**
     * ERP单价总金额
     */
    private Double erpAmount;
    /**
     * 原币不含税单价
     */
    private Double priceNoTax;
    /**
     * 不含税总价
     */
    private Double totalPriceTax;
    /**
     * 日期
     */
    private String tranDate;

    @Override
    public String toString() {
        return "OutboundRecordDTO{" +
                "mirNo='" + mirNo + '\'' +
                ", wmNo='" + wmNo + '\'' +
                ", mo='" + mo + '\'' +
                ", poNumber='" + poNumber + '\'' +
                ", partNumber='" + partNumber + '\'' +
                ", partDescription='" + partDescription + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", facNo='" + facNo + '\'' +
                ", location='" + location + '\'' +
                ", unitName='" + unitName + '\'' +
                ", qtyIssued=" + qtyIssued +
                ", batchNo='" + batchNo + '\'' +
                ", ratio=" + ratio +
                ", currCode='" + currCode + '\'' +
                ", currName='" + currName + '\'' +
                ", priceCurr=" + priceCurr +
                ", toOther=" + toOther +
                ", price=" + price +
                ", priceSum=" + priceSum +
                ", dateRequired='" + dateRequired + '\'' +
                ", taxRate=" + taxRate +
                ", erpAmount=" + erpAmount +
                ", priceNoTax=" + priceNoTax +
                ", totalPriceTax=" + totalPriceTax +
                ", tranDate='" + tranDate + '\'' +
                '}';
    }

    public String getProcess() {
        if (this.facNo.contains("开料")) {
            return "开料";
        } else if (this.facNo.contains("钻孔")) {
            return "钻孔";
        } else if (this.facNo.contains("板面电镀")) {
            return "板面电镀";
        } else if (this.facNo.contains("外层碱性蚀刻")) {
            return "外层碱性蚀刻";
        } else if (this.facNo.contains("沉铜")) {
            return "沉铜";
        } else if (this.facNo.contains("图形电镀")) {
            return "图形电镀";
        } else if (this.facNo.contains("金手指")) {
            return "金手指";
        } else if (this.facNo.contains("沉金")) {
            return "沉金";
        } else if (this.facNo.contains("干膜QC")) {
            return "干膜QC";
        } else if (this.facNo.contains("干膜")) {
            return "干膜";
        } else if (this.facNo.contains("阻焊")) {
            return "阻焊";
        } else if (this.facNo.contains("铣外形")) {
            return "铣外形";
        } else if (this.facNo.contains("E-T测试")) {
            return "E-T测试";
        } else if (this.getFacNo().contains("测试")) {
            return "测试";
        } else if (this.facNo.contains("最后检查")) {
            return "最后检查";
        } else if (this.facNo.contains("包装")) {
            return "包装";
        } else if (this.facNo.contains("层压")) {
            return "层压";
        } else if (this.facNo.contains("内层AOI")) {
            return "内层AOI";
        } else if (this.facNo.contains("外层AOI")) {
            return "外层AOI";
        } else if (this.facNo.contains("AOI")) {
            return "AOI";
        } else if (this.facNo.contains("内层")) {
            return "内层";
        } else if (this.facNo.contains("仓库")) {
            return "仓库";
        } else if (this.facNo.contains("人事行政")) {
            return "人事行政";
        } else if (this.facNo.contains("品质部")) {
            return "品质部";
        } else if (this.facNo.contains("资讯部")) {
            return "资讯部";
        } else if (this.facNo.contains("工程")) {
            return "工程";
        } else if (this.facNo.contains("工艺")) {
            return "工艺";
        } else if (this.facNo.contains("客诉")) {
            return "客诉";
        } else if (this.facNo.contains("研发")) {
            return "研发";
        } else if (this.facNo.contains("计划部")) {
            return "计划部";
        } else if (this.facNo.contains("生产部")) {
            return "生产部";
        } else if (this.facNo.contains("污水站")) {
            return "污水站";
        } else if (this.facNo.contains("维修部")) {
            return "维修部";
        } else if (this.facNo.contains("设备")) {
            return "设备";
        } else if (this.facNo.contains("电镀VCP线")) {
            return "电镀VCP线";
        } else if (this.facNo.contains("酸蚀")) {
            return "酸蚀";
        } else if (this.facNo.contains("退锡")) {
            return "退锡";
        } else if (this.facNo.contains("无铅喷锡")) {
            return "无铅喷锡";
        } else if (this.facNo.contains("有铅喷锡")) {
            return "有铅喷锡";
        } else if (this.facNo.contains("字符")) {
            return "字符";
        } else if (this.facNo.contains("冲床")) {
            return "冲床";
        } else if (this.facNo.contains("压合")) {
            return "压合";
        } else if (this.facNo.contains("采购")) {
            return "采购";
        } else if (this.facNo.contains("管理")) {
            return "管理";
        } else if (this.facNo.contains("财务")) {
            return "财务";
        } else if (this.facNo.contains("资料室")) {
            return "资料室";
        } else if (this.facNo.contains("市场部")) {
            return "市场部";
        } else {
            return null;
        }
    }

    public String getMirNo() {
        return mirNo;
    }

    public void setMirNo(String mirNo) {
        this.mirNo = mirNo;
    }

    public String getWmNo() {
        return wmNo;
    }

    public void setWmNo(String wmNo) {
        this.wmNo = wmNo;
    }

    public String getMo() {
        return mo;
    }

    public void setMo(String mo) {
        this.mo = mo;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getPartDescription() {
        return partDescription;
    }

    public void setPartDescription(String partDescription) {
        this.partDescription = partDescription;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getFacNo() {
        return facNo;
    }

    public void setFacNo(String facNo) {
        this.facNo = facNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Double getQtyIssued() {
        return qtyIssued;
    }

    public void setQtyIssued(Double qtyIssued) {
        this.qtyIssued = qtyIssued;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getRatio() {
        return ratio;
    }

    public void setRatio(Integer ratio) {
        this.ratio = ratio;
    }

    public String getCurrCode() {
        return currCode;
    }

    public void setCurrCode(String currCode) {
        this.currCode = currCode;
    }

    public String getCurrName() {
        return currName;
    }

    public void setCurrName(String currName) {
        this.currName = currName;
    }

    public Double getPriceCurr() {
        return priceCurr;
    }

    public void setPriceCurr(Double priceCurr) {
        this.priceCurr = priceCurr;
    }

    public Integer getToOther() {
        return toOther;
    }

    public void setToOther(Integer toOther) {
        this.toOther = toOther;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPriceSum() {
        return priceSum;
    }

    public void setPriceSum(Double priceSum) {
        this.priceSum = priceSum;
    }

    public String getDateRequired() {
        return dateRequired;
    }

    public void setDateRequired(String dateRequired) {
        this.dateRequired = dateRequired;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public Double getErpAmount() {
        return erpAmount;
    }

    public void setErpAmount(Double erpAmount) {
        this.erpAmount = erpAmount;
    }

    public Double getPriceNoTax() {
        return priceNoTax;
    }

    public void setPriceNoTax(Double priceNoTax) {
        this.priceNoTax = priceNoTax;
    }

    public Double getTotalPriceTax() {
        return totalPriceTax;
    }

    public void setTotalPriceTax(Double totalPriceTax) {
        this.totalPriceTax = totalPriceTax;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }
}
