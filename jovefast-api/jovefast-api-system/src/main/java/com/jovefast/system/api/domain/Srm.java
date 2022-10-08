package com.jovefast.system.api.domain;

import java.io.Serializable;

/**
 * 生益互联下单或修改
 * @author Acechengui
 * @date 2022年03月04日 8:26
 */
public class Srm implements Serializable {
    private static final long serialVersionUID = 2965998887020431144L;
    private Integer rkey;//主键
    private String factorylocation;//装运地址
    private String bstnk="";//客户PO号码-必填
    private String kunwe="";//送达方一 -必填
    private String kunwe1="";//送达方二
    private String auart="";//订单类型-必填
    private String cpnb="";//业务范围
    private Srm[] orders;//订单行数据集
    private Integer shipl;//客户订单行号-必填
    private String poxmh="";//客户送货行号-必填
    private String vkorg="";//销售组织-必填  广东生益、苏州生益、陕西生益、常熟生益、江苏生益、江西生益、台湾生益、香港生益
    private String vtweg="";//销售渠道-必填 范围：内销、外销、转口、免税、征税
    private String scxh="";//生产型号
    private String khdd="";//工单号
    private String wlmc="";//物料名称(物料类别)-必填
    private String kdmat="";//物料编码-必填
    private String postx="";//物料长文本-必填
    private Double menge;//数量-必填
    private String meins="";//单位-必填
    private Double price;//价格-必填
    private String konwa="";//币别-必填
    private String kschl="";//是否含税-必填
    private String dlvdt="";//交期 -必填
    private String xsbz="";//下单备注
    private String uptop="";//加急标记
    private String zflag1="";//操作标记

    public Integer getRkey() {
        return rkey;
    }

    public void setRkey(Integer rkey) {
        this.rkey = rkey;
    }

    public String getFactorylocation() {
        return factorylocation;
    }

    public void setFactorylocation(String factorylocation) {
        this.factorylocation = factorylocation;
    }

    public String getBstnk() {
        return bstnk;
    }

    public void setBstnk(String bstnk) {
        this.bstnk = bstnk;
    }

    public String getKunwe() {
        return kunwe;
    }

    public void setKunwe(String kunwe) {
        this.kunwe = kunwe;
    }

    public String getKunwe1() {
        return kunwe1;
    }

    public void setKunwe1(String kunwe1) {
        this.kunwe1 = kunwe1;
    }

    public String getAuart() {
        return auart;
    }

    public void setAuart(String auart) {
        this.auart = auart;
    }

    public String getCpnb() {
        return cpnb;
    }

    public void setCpnb(String cpnb) {
        this.cpnb = cpnb;
    }

    public Srm[] getOrders() {
        return orders;
    }

    public void setOrders(Srm[] orders) {
        this.orders = orders;
    }

    public Integer getShipl() {
        return shipl;
    }

    public void setShipl(Integer shipl) {
        this.shipl = shipl;
    }

    public String getPoxmh() {
        return poxmh;
    }

    public void setPoxmh(String poxmh) {
        this.poxmh = poxmh;
    }

    public String getVkorg() {
        return vkorg;
    }

    public void setVkorg(String vkorg) {
        this.vkorg = vkorg;
    }

    public String getVtweg() {
        return vtweg;
    }

    public void setVtweg(String vtweg) {
        this.vtweg = vtweg;
    }

    public String getScxh() {
        return scxh;
    }

    public void setScxh(String scxh) {
        this.scxh = scxh;
    }

    public String getKhdd() {
        return khdd;
    }

    public void setKhdd(String khdd) {
        this.khdd = khdd;
    }

    public String getWlmc() {
        return wlmc;
    }

    public void setWlmc(String wlmc) {
        this.wlmc = wlmc;
    }

    public String getKdmat() {
        return kdmat;
    }

    public void setKdmat(String kdmat) {
        this.kdmat = kdmat;
    }

    public String getPostx() {
        return postx;
    }

    public void setPostx(String postx) {
        this.postx = postx;
    }

    public Double getMenge() {
        return menge;
    }

    public void setMenge(Double menge) {
        this.menge = menge;
    }

    public String getMeins() {
        return meins;
    }

    public void setMeins(String meins) {
        this.meins = meins;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getKonwa() {
        return konwa;
    }

    public void setKonwa(String konwa) {
        this.konwa = konwa;
    }

    public String getKschl() {
        return kschl;
    }

    public void setKschl(String kschl) {
        this.kschl = kschl;
    }

    public String getDlvdt() {
        return dlvdt;
    }

    public void setDlvdt(String dlvdt) {
        this.dlvdt = dlvdt;
    }

    public String getXsbz() {
        return xsbz;
    }

    public void setXsbz(String xsbz) {
        this.xsbz = xsbz;
    }

    public String getUptop() {
        return uptop;
    }

    public void setUptop(String uptop) {
        this.uptop = uptop;
    }

    public String getZflag1() {
        return zflag1;
    }

    public void setZflag1(String zflag1) {
        this.zflag1 = zflag1;
    }
}
