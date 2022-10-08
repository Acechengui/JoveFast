package com.jovefast.job.domain.dto;

import java.io.Serializable;
import java.sql.Date;

/**
 * 市场跟单表
 * @author Acechengui
 */
public class VCustomerWipDTO implements Serializable {
    private static final long serialVersionUID = -1887158216690063867L;

    /**
     * 编号
     */
    private Integer rkey;
    /**
     * 生产型号
     */
    private String productionmodel;
    /**
     * 客户型号
     */
    private String customermodel;
    /**
     * 补投数量
     */
    private Double quansch;
    /**
     * 补投时间
     */
    private String remadetime;
    /**
     * 版本
     */
    private String versions;
    /**
     * 厂别
     */
    private Integer plant;
    /**
     * 单价
     */
    private double partprice;
    /**
     * 工卡号
     */
    private String workerscard;
    /**
     * MO号
     */
    private String salesorder;
    /**
     * 工单指针
     */
    private String woptr;
    /**
     * 结存数
     */
    private String qtybacklog;
    /**
     * 所在工序流程号
     */
    private String dgwptr;
    /**
     * 0则为外层工单:>0则为内层工单
     */
    private String rootptr;
    /**
     * 订单类型
     */
    private String ordertype;
    /**
     * 工单状态
     */
    private String prodstatus;
    /**
     * 订单状态名称
     */
    private String deptname;
    /**
     * 订单状态
     */
    private String orderstatus;
    /**
     * 客户代码
     */
    private String customertype;
    /**
     * 最终PO
     */
    private String finalpo;
    /**
     * 客户名称
     */
    private String customername;
    /**
     * 下单日期
     */
    private String orderdate;
    /**
     * 客户交期
     */
    private String deliveryresponse;
    /**
     * 订单数
     */
    private Integer numberorder;
    /**
     * 已交数
     */
    private Integer paid;
    /**
     * 未交量
     */
    private Integer unpaidamount;
    /**
     * 订单面积
     */
    private double orderarea;
    /**
     * 批次
     */
    private String lotnumcount;
    /**
     * 在线状态
     */
    private String onlinestatus;
    /**
     * 计划回复
     */
    private String planresponse;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 计划下卡前细化状态
     */
    private String marketstate;
    /**
     * 夹具状态
     */
    private String fixturestatus;
    /**
     * 工程状态
     */
    private String dictlabel;
    /**
     * 成品库存
     */
    private double qtyonhand;
    /**
     * 当前所在工序编号
     */
    private Integer step;
    /**
     * 市场备注
     */
    private String marketnotes;
    private String marketnotesa;
    private String marketnotesb;

    /**
     * 计划下卡前状态
     */
    private String transdescription1;
    /**
     * 在市场工程
     */
    private String transdescription2;
    /**
     * 在工程
     */
    private String transdescription3;
    /**
     * 生产型号
     */
    private String micreate;//MI生成时间
    /**
     * 几大工序处理日期
     */
    private String proc1time;
    private String proc2time;
    private String proc3time;
    private String proc4time;
    private String proc5time;
    private String proc6time;
    private String proc7time;
    /**
     * 预警状态
     */
    private String earlywarnstatus;

    /**
     * 几大工序标准工时
     */
    private Integer proc1;
    private Integer proc2;
    private Integer proc3;
    private Integer proc4;
    private Integer proc5;
    private Integer proc6;
    private Integer proc7;

    /**
     * 几大工序所在MI的制程顺序
     */
    private Integer stepnumber1;
    private Integer stepnumber2;
    private Integer stepnumber3;
    private Integer stepnumber4;
    private Integer stepnumber5;
    private Integer stepnumber6;
    private Integer stepnumber7;

    /**
     * 采购部确认回复交期
     */
    private Date deliverytime;
    /**
     * 组别别称
     */
    private String analysiscode3;
    /**
     * 交期状态
     */
    private String lowmaterialstatus;


    public void setLowmaterialstatus(String lowmaterialstatus){
     this.lowmaterialstatus=lowmaterialstatus;
    }

    public String getLowmaterialstatus(){
        if(this.lowmaterialstatus!=null){
            return "欠料";
        }else{
            return "";
        }
    }

    public String getAnalysiscode3() {
        return analysiscode3;
    }

    public void setAnalysiscode3(String analysiscode3) {
        this.analysiscode3 = analysiscode3;
    }

    public Date getDELIVERYTIME() {
        return deliverytime;
    }

    public void setgetDELIVERYTIME(Date deliverytime) {
        this.deliverytime = deliverytime;
    }

    public Integer getRkey() {
        return rkey;
    }

    public void setRkey(Integer rkey) {
        this.rkey = rkey;
    }

    public String getProductionmodel() {
        return productionmodel;
    }

    public void setProductionmodel(String productionmodel) {
        this.productionmodel = productionmodel;
    }

    public String getCustomermodel() {
        return customermodel;
    }

    public void setCustomermodel(String customermodel) {
        this.customermodel = customermodel;
    }

    public Double getQuansch() {
        return quansch;
    }

    public void setQuansch(Double quansch) {
        this.quansch = quansch;
    }

    public String getRemadetime() {
        return remadetime;
    }

    public void setRemadetime(String remadetime) {
        this.remadetime = remadetime;
    }

    public String getVersions() {
        return versions;
    }

    public void setVersions(String versions) {
        this.versions = versions;
    }

    public Integer getPlant() {
        return plant;
    }

    public void setPlant(Integer plant) {
        this.plant = plant;
    }

    public double getPartprice() {
        return partprice;
    }

    public void setPartprice(double partprice) {
        this.partprice = partprice;
    }

    public String getWorkerscard() {
        return workerscard;
    }

    public void setWorkerscard(String workerscard) {
        this.workerscard = workerscard;
    }

    public String getSalesorder() {
        return salesorder;
    }

    public void setSalesorder(String salesorder) {
        this.salesorder = salesorder;
    }

    public String getWoptr() {
        return woptr;
    }

    public void setWoptr(String woptr) {
        this.woptr = woptr;
    }

    public String getQtybacklog() {
        return qtybacklog;
    }

    public void setQtybacklog(String qtybacklog) {
        this.qtybacklog = qtybacklog;
    }

    public String getDgwptr() {
        return dgwptr;
    }

    public void setDgwptr(String dgwptr) {
        this.dgwptr = dgwptr;
    }

    public String getRootptr() {
        return rootptr;
    }

    public void setRootptr(String rootptr) {
        this.rootptr = rootptr;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public String getProdstatus() {
        return prodstatus;
    }

    public void setProdstatus(String prodstatus) {
        this.prodstatus = prodstatus;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getCustomertype() {
        return customertype;
    }

    public void setCustomertype(String customertype) {
        this.customertype = customertype;
    }

    public String getFinalpo() {
        return finalpo;
    }

    public void setFinalpo(String finalpo) {
        this.finalpo = finalpo;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getDeliveryresponse() {
        return deliveryresponse;
    }

    public void setDeliveryresponse(String deliveryresponse) {
        this.deliveryresponse = deliveryresponse;
    }

    public Integer getNumberorder() {
        return numberorder;
    }

    public void setNumberorder(Integer numberorder) {
        this.numberorder = numberorder;
    }

    public Integer getPaid() {
        return paid;
    }

    public void setPaid(Integer paid) {
        this.paid = paid;
    }

    public Integer getUnpaidamount() {
        return unpaidamount;
    }

    public void setUnpaidamount(Integer unpaidamount) {
        this.unpaidamount = unpaidamount;
    }

    public double getOrderarea() {
        return orderarea;
    }

    public void setOrderarea(double orderarea) {
        this.orderarea = orderarea;
    }

    public String getLotnumcount() {
        return lotnumcount;
    }

    public void setLotnumcount(String lotnumcount) {
        this.lotnumcount = lotnumcount;
    }

    public String getOnlinestatus() {
        return onlinestatus;
    }

    public void setOnlinestatus(String onlinestatus) {
        this.onlinestatus = onlinestatus;
    }

    public String getPlanresponse() {
        return planresponse;
    }

    public void setPlanresponse(String planresponse) {
        this.planresponse = planresponse;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMarketstate() {
        return marketstate;
    }

    public void setMarketstate(String marketstate) {
        this.marketstate = marketstate;
    }

    public String getFixturestatus() {
        return fixturestatus;
    }

    public void setFixturestatus(String fixturestatus) {
        this.fixturestatus = fixturestatus;
    }

    public String getDictlabel() {
        return dictlabel;
    }

    public void setDictlabel(String dictlabel) {
        this.dictlabel = dictlabel;
    }

    public double getQtyonhand() {
        return qtyonhand;
    }

    public void setQtyonhand(double qtyonhand) {
        this.qtyonhand = qtyonhand;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getMarketnotes() {
        return marketnotes;
    }

    public void setMarketnotes(String marketnotes) {
        this.marketnotes = marketnotes;
    }

    public String getMarketnotesa() {
        return marketnotesa;
    }

    public void setMarketnotesa(String marketnotesa) {
        this.marketnotesa = marketnotesa;
    }

    public String getMarketnotesb() {
        return marketnotesb;
    }

    public void setMarketnotesb(String marketnotesb) {
        this.marketnotesb = marketnotesb;
    }

    public String getTransdescription1() {
        return transdescription1;
    }

    public void setTransdescription1(String transdescription1) {
        this.transdescription1 = transdescription1;
    }

    public String getTransdescription2() {
        return transdescription2;
    }

    public void setTransdescription2(String transdescription2) {
        this.transdescription2 = transdescription2;
    }

    public String getTransdescription3() {
        return transdescription3;
    }

    public void setTransdescription3(String transdescription3) {
        this.transdescription3 = transdescription3;
    }

    public String getMicreate() {
        return micreate;
    }

    public void setMicreate(String micreate) {
        this.micreate = micreate;
    }

    public String getProc1time() {
        return proc1time;
    }

    public void setProc1time(String proc1time) {
        this.proc1time = proc1time;
    }

    public String getProc2time() {
        return proc2time;
    }

    public void setProc2time(String proc2time) {
        this.proc2time = proc2time;
    }

    public String getProc3time() {
        return proc3time;
    }

    public void setProc3time(String proc3time) {
        this.proc3time = proc3time;
    }

    public String getProc4time() {
        return proc4time;
    }

    public void setProc4time(String proc4time) {
        this.proc4time = proc4time;
    }

    public String getProc5time() {
        return proc5time;
    }

    public void setProc5time(String proc5time) {
        this.proc5time = proc5time;
    }

    public String getProc6time() {
        return proc6time;
    }

    public void setProc6time(String proc6time) {
        this.proc6time = proc6time;
    }

    public String getProc7time() {
        return proc7time;
    }

    public void setProc7time(String proc7time) {
        this.proc7time = proc7time;
    }

    public String getEarlywarnstatus() {
        return earlywarnstatus;
    }

    public void setEarlywarnstatus(String earlywarnstatus) {
        this.earlywarnstatus = earlywarnstatus;
    }

    public Integer getProc1() {
        return proc1;
    }

    public void setProc1(Integer proc1) {
        this.proc1 = proc1;
    }

    public Integer getProc2() {
        return proc2;
    }

    public void setProc2(Integer proc2) {
        this.proc2 = proc2;
    }

    public Integer getProc3() {
        return proc3;
    }

    public void setProc3(Integer proc3) {
        this.proc3 = proc3;
    }

    public Integer getProc4() {
        return proc4;
    }

    public void setProc4(Integer proc4) {
        this.proc4 = proc4;
    }

    public Integer getProc5() {
        return proc5;
    }

    public void setProc5(Integer proc5) {
        this.proc5 = proc5;
    }

    public Integer getProc6() {
        return proc6;
    }

    public void setProc6(Integer proc6) {
        this.proc6 = proc6;
    }

    public Integer getProc7() {
        return proc7;
    }

    public void setProc7(Integer proc7) {
        this.proc7 = proc7;
    }

    public Integer getStepnumber1() {
        return stepnumber1;
    }

    public void setStepnumber1(Integer stepnumber1) {
        this.stepnumber1 = stepnumber1;
    }

    public Integer getStepnumber2() {
        return stepnumber2;
    }

    public void setStepnumber2(Integer stepnumber2) {
        this.stepnumber2 = stepnumber2;
    }

    public Integer getStepnumber3() {
        return stepnumber3;
    }

    public void setStepnumber3(Integer stepnumber3) {
        this.stepnumber3 = stepnumber3;
    }

    public Integer getStepnumber4() {
        return stepnumber4;
    }

    public void setStepnumber4(Integer stepnumber4) {
        this.stepnumber4 = stepnumber4;
    }

    public Integer getStepnumber5() {
        return stepnumber5;
    }

    public void setStepnumber5(Integer stepnumber5) {
        this.stepnumber5 = stepnumber5;
    }

    public Integer getStepnumber6() {
        return stepnumber6;
    }

    public void setStepnumber6(Integer stepnumber6) {
        this.stepnumber6 = stepnumber6;
    }

    public Integer getStepnumber7() {
        return stepnumber7;
    }

    public void setStepnumber7(Integer stepnumber7) {
        this.stepnumber7 = stepnumber7;
    }

    @Override
    public String toString() {
        return "VCustomerWipDTO{" +
                "rkey=" + rkey +
                ", productionmodel='" + productionmodel + '\'' +
                ", customermodel='" + customermodel + '\'' +
                ", quansch=" + quansch +
                ", remadetime='" + remadetime + '\'' +
                ", versions='" + versions + '\'' +
                ", plant=" + plant +
                ", partprice=" + partprice +
                ", workerscard='" + workerscard + '\'' +
                ", salesorder='" + salesorder + '\'' +
                ", woptr='" + woptr + '\'' +
                ", qtybacklog='" + qtybacklog + '\'' +
                ", dgwptr='" + dgwptr + '\'' +
                ", rootptr='" + rootptr + '\'' +
                ", ordertype='" + ordertype + '\'' +
                ", prodstatus='" + prodstatus + '\'' +
                ", deptname='" + deptname + '\'' +
                ", orderstatus='" + orderstatus + '\'' +
                ", customertype='" + customertype + '\'' +
                ", finalpo='" + finalpo + '\'' +
                ", customername='" + customername + '\'' +
                ", orderdate='" + orderdate + '\'' +
                ", deliveryresponse='" + deliveryresponse + '\'' +
                ", numberorder=" + numberorder +
                ", paid=" + paid +
                ", unpaidamount=" + unpaidamount +
                ", orderarea=" + orderarea +
                ", lotnumcount='" + lotnumcount + '\'' +
                ", onlinestatus='" + onlinestatus + '\'' +
                ", planresponse='" + planresponse + '\'' +
                ", remarks='" + remarks + '\'' +
                ", marketstate='" + marketstate + '\'' +
                ", fixturestatus='" + fixturestatus + '\'' +
                ", dictlabel='" + dictlabel + '\'' +
                ", qtyonhand=" + qtyonhand +
                ", step=" + step +
                ", marketnotes='" + marketnotes + '\'' +
                ", marketnotesa='" + marketnotesa + '\'' +
                ", marketnotesb='" + marketnotesb + '\'' +
                ", transdescription1='" + transdescription1 + '\'' +
                ", transdescription2='" + transdescription2 + '\'' +
                ", transdescription3='" + transdescription3 + '\'' +
                ", micreate='" + micreate + '\'' +
                ", proc1time='" + proc1time + '\'' +
                ", proc2time='" + proc2time + '\'' +
                ", proc3time='" + proc3time + '\'' +
                ", proc4time='" + proc4time + '\'' +
                ", proc5time='" + proc5time + '\'' +
                ", proc6time='" + proc6time + '\'' +
                ", proc7time='" + proc7time + '\'' +
                ", earlywarnstatus='" + earlywarnstatus + '\'' +
                ", proc1=" + proc1 +
                ", proc2=" + proc2 +
                ", proc3=" + proc3 +
                ", proc4=" + proc4 +
                ", proc5=" + proc5 +
                ", proc6=" + proc6 +
                ", proc7=" + proc7 +
                ", stepnumber1=" + stepnumber1 +
                ", stepnumber2=" + stepnumber2 +
                ", stepnumber3=" + stepnumber3 +
                ", stepnumber4=" + stepnumber4 +
                ", stepnumber5=" + stepnumber5 +
                ", stepnumber6=" + stepnumber6 +
                ", stepnumber7=" + stepnumber7 +
                ", deliverytime=" + deliverytime +
                ", analysiscode3='" + analysiscode3 + '\'' +
                ", lowmaterialstatus='" + lowmaterialstatus + '\'' +
                '}';
    }
}
