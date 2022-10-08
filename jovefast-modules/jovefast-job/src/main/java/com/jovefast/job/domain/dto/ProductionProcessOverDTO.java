package com.jovefast.job.domain.dto;

import java.io.Serializable;

/**
 * @author MuTouYSK
 * @date 2022/4/26
 * 生产工序过数记录
 */
public class ProductionProcessOverDTO implements Serializable {

    private static final long serialVersionUID = 7473364400341400019L;

    /**
     * 工卡编号
     */
    private String workers;
    /**
     * 下卡时间
     */
    private String entTime;
    /**
     * 步骤
     */
    private Integer step;
    /**
     * 收板工序
     */
    private Integer recRoute;
    /**
     * 收板工序名称
     */
    private String recName;
    /**
     * 收板时间
     */
    private String recTime;
    /**
     * 收板员
     */
    private String recEmp;
    /**
     * 出板工序
     */
    private Integer outRoute;
    /**
     * 出板工序名称
     */
    private String outName;
    /**
     * 出板时间
     */
    private String outTime;
    /**
     * 结存数 PCS
     */
    private Double qtyBacklog;
    /**
     * 收板数 PCS
     */
    private Double qtyProduced;
    /**
     * 报废数 PCS
     */
    private Double qtyRejected;
    /**
     * MO
     */
    private String salesOrder;
    /**
     * MO状态
     */
    private Integer status;
    /**
     * 订单 PCS 数
     */
    private Double partsOrdered;
    /**
     * 计划交期
     */
    private String schDate;
    /**
     * 客户交期
     */
    private String dueDate;
    /**
     * 客户代码
     */
    private String cutCode;
    /**
     * 生产编号
     */
    private String number;
    /**
     * PNL 长
     */

    private Double pnlLong;
    /**
     * PNL 宽
     */
    private Double pnlWidth;
    /**
     * PNL 高
     */
    private Double setPnl;
    /**
     * SET 长
     */
    private Double setLong;
    /**
     * SET 宽
     */
    private Double setWidth;
    /**
     * PCS/SET
     */
    private Double pcsSet;
    /**
     * 生产厂别
     */

    private Integer prodPlant;
    /**
     * 收板厂别
     */
    private Integer recPlant;
    /**
     * 出板厂别
     */
    private Integer outPlant;

    /**
     * 计算set面积
     *
     * @return 面积
     */
    public double calculateSetArea() {
        return ((this.qtyProduced / this.pcsSet) * (this.setLong * this.setWidth)) / 1000000;
    }

    public Integer recProdPlant() {
        if (this.recPlant == 0) {
            return this.prodPlant;
        }
        return this.recPlant;
    }

    @Override
    public String toString() {
        return "ProductionProcessOverDTO{" +
                "workers='" + workers + '\'' +
                ", entTime='" + entTime + '\'' +
                ", step=" + step +
                ", recRoute=" + recRoute +
                ", recName='" + recName + '\'' +
                ", recTime='" + recTime + '\'' +
                ", recEmp='" + recEmp + '\'' +
                ", outRoute=" + outRoute +
                ", outName='" + outName + '\'' +
                ", outTime='" + outTime + '\'' +
                ", qtyBacklog=" + qtyBacklog +
                ", qtyProduced=" + qtyProduced +
                ", qtyRejected=" + qtyRejected +
                ", salesOrder='" + salesOrder + '\'' +
                ", status=" + status +
                ", partsOrdered=" + partsOrdered +
                ", schDate='" + schDate + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", cutCode='" + cutCode + '\'' +
                ", number='" + number + '\'' +
                ", pnlLong=" + pnlLong +
                ", pnlWidth=" + pnlWidth +
                ", setPnl=" + setPnl +
                ", setLong=" + setLong +
                ", setWidth=" + setWidth +
                ", pcsSet=" + pcsSet +
                ", prodPlant=" + prodPlant +
                ", recPlant=" + recPlant +
                ", outPlant=" + outPlant +
                '}';
    }

    public String getWorkers() {
        return workers;
    }

    public void setWorkers(String workers) {
        this.workers = workers;
    }

    public String getEntTime() {
        return entTime;
    }

    public void setEntTime(String entTime) {
        this.entTime = entTime;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getRecRoute() {
        return recRoute;
    }

    public void setRecRoute(Integer recRoute) {
        this.recRoute = recRoute;
    }

    public String getRecName() {
        return recName;
    }

    public void setRecName(String recName) {
        this.recName = recName;
    }

    public String getRecTime() {
        return recTime;
    }

    public void setRecTime(String recTime) {
        this.recTime = recTime;
    }

    public String getRecEmp() {
        return recEmp;
    }

    public void setRecEmp(String recEmp) {
        this.recEmp = recEmp;
    }

    public Integer getOutRoute() {
        return outRoute;
    }

    public void setOutRoute(Integer outRoute) {
        this.outRoute = outRoute;
    }

    public String getOutName() {
        return outName;
    }

    public void setOutName(String outName) {
        this.outName = outName;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public Double getQtyBacklog() {
        return qtyBacklog;
    }

    public void setQtyBacklog(Double qtyBacklog) {
        this.qtyBacklog = qtyBacklog;
    }

    public Double getQtyProduced() {
        return qtyProduced;
    }

    public void setQtyProduced(Double qtyProduced) {
        this.qtyProduced = qtyProduced;
    }

    public Double getQtyRejected() {
        return qtyRejected;
    }

    public void setQtyRejected(Double qtyRejected) {
        this.qtyRejected = qtyRejected;
    }

    public String getSalesOrder() {
        return salesOrder;
    }

    public void setSalesOrder(String salesOrder) {
        this.salesOrder = salesOrder;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getPartsOrdered() {
        return partsOrdered;
    }

    public void setPartsOrdered(Double partsOrdered) {
        this.partsOrdered = partsOrdered;
    }

    public String getSchDate() {
        return schDate;
    }

    public void setSchDate(String schDate) {
        this.schDate = schDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCutCode() {
        return cutCode;
    }

    public void setCutCode(String cutCode) {
        this.cutCode = cutCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Double getPnlLong() {
        return pnlLong;
    }

    public void setPnlLong(Double pnlLong) {
        this.pnlLong = pnlLong;
    }

    public Double getPnlWidth() {
        return pnlWidth;
    }

    public void setPnlWidth(Double pnlWidth) {
        this.pnlWidth = pnlWidth;
    }

    public Double getSetPnl() {
        return setPnl;
    }

    public void setSetPnl(Double setPnl) {
        this.setPnl = setPnl;
    }

    public Double getSetLong() {
        return setLong;
    }

    public void setSetLong(Double setLong) {
        this.setLong = setLong;
    }

    public Double getSetWidth() {
        return setWidth;
    }

    public void setSetWidth(Double setWidth) {
        this.setWidth = setWidth;
    }

    public Double getPcsSet() {
        return pcsSet;
    }

    public void setPcsSet(Double pcsSet) {
        this.pcsSet = pcsSet;
    }

    public Integer getProdPlant() {
        return prodPlant;
    }

    public void setProdPlant(Integer prodPlant) {
        this.prodPlant = prodPlant;
    }

    public Integer getRecPlant() {
        return recPlant;
    }

    public void setRecPlant(Integer recPlant) {
        this.recPlant = recPlant;
    }

    public Integer getOutPlant() {
        return outPlant;
    }

    public void setOutPlant(Integer outPlant) {
        this.outPlant = outPlant;
    }
}
