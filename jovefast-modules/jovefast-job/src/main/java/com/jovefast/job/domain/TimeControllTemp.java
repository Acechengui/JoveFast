package com.jovefast.job.domain;

import java.io.Serializable;
import java.util.Date;

public class TimeControllTemp implements Serializable {

    private static final long serialVersionUID = 5483092230740046995L;

    private long rkey;
    private String wo;  // 工卡号
    private String mo; // mo号
    private String mfg; // 生产型号
    private String custcode;  // 客户代码
    private Date warndate;   // 预警时间(起始时间+文档规定的预警时间)
    private Date alarmsdate;  // 报警时间(起始时间+文档规定的报警时间)
    private int factoryname;  // 厂别
    private String auditor;     // 企业微信推送人id
    private String party;       // 企业微信部门编号
    private String startdepartment;  // 开始工序
    private String enddepartment;  // 结束工序
    private Date startdate;  // 起始时间
    private Date enddate;  // 结束时间
    private int warn;  // 文档规定的预警时间
    private int alarms;  // 文档规定的报警时间
    private int overtime;  // 停留时间（小时）
    private int outtime;  // 超时时间（小时）
    private int status;   // 状态
    private Date lastwarningtime;   // 最后预警时间
    private int son;   //区分内外层工卡

    @Override
    public String toString() {
        return "TimeControllTempBean{" +
                "rkey=" + rkey +
                ", wo='" + wo + '\'' +
                ", mo='" + mo + '\'' +
                ", mfg='" + mfg + '\'' +
                ", custcode='" + custcode + '\'' +
                ", warndate=" + warndate +
                ", alarmsdate=" + alarmsdate +
                ", factoryname=" + factoryname +
                ", auditor='" + auditor + '\'' +
                ", party='" + party + '\'' +
                ", startdepartment='" + startdepartment + '\'' +
                ", enddepartment='" + enddepartment + '\'' +
                ", startdate=" + startdate +
                ", enddate=" + enddate +
                ", warn=" + warn +
                ", alarms=" + alarms +
                ", overtime=" + overtime +
                ", outtime=" + outtime +
                ", status=" + status +
                ", lastwarningtime=" + lastwarningtime +
                ", son=" + son +
                '}';
    }

    public long getRkey() {
        return rkey;
    }

    public void setRkey(long rkey) {
        this.rkey = rkey;
    }

    public String getWo() {
        return wo;
    }

    public void setWo(String wo) {
        this.wo = wo;
    }

    public String getMo() {
        return mo;
    }

    public void setMo(String mo) {
        this.mo = mo;
    }

    public String getMfg() {
        return mfg;
    }

    public void setMfg(String mfg) {
        this.mfg = mfg;
    }

    public String getCustcode() {
        return custcode;
    }

    public void setCustcode(String custcode) {
        this.custcode = custcode;
    }

    public Date getWarndate() {
        return warndate;
    }

    public void setWarndate(Date warndate) {
        this.warndate = warndate;
    }

    public Date getAlarmsdate() {
        return alarmsdate;
    }

    public void setAlarmsdate(Date alarmsdate) {
        this.alarmsdate = alarmsdate;
    }

    public int getFactoryname() {
        return factoryname;
    }

    public void setFactoryname(int factoryname) {
        this.factoryname = factoryname;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getStartdepartment() {
        return startdepartment;
    }

    public void setStartdepartment(String startdepartment) {
        this.startdepartment = startdepartment;
    }

    public String getEnddepartment() {
        return enddepartment;
    }

    public void setEnddepartment(String enddepartment) {
        this.enddepartment = enddepartment;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public int getWarn() {
        return warn;
    }

    public void setWarn(int warn) {
        this.warn = warn;
    }

    public int getAlarms() {
        return alarms;
    }

    public void setAlarms(int alarms) {
        this.alarms = alarms;
    }

    public int getOvertime() {
        return overtime;
    }

    public void setOvertime(int overtime) {
        this.overtime = overtime;
    }

    public int getOuttime() {
        return outtime;
    }

    public void setOuttime(int outtime) {
        this.outtime = outtime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getLastwarningtime() {
        return lastwarningtime;
    }

    public void setLastwarningtime(Date lastwarningtime) {
        this.lastwarningtime = lastwarningtime;
    }

    public int getSon() {
        return son;
    }

    public void setSon(int son) {
        this.son = son;
    }
}
