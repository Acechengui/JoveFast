package com.jovefast.job.domain;

import java.io.Serializable;

/**
 * @author Acechengui
 */
public class Data9995 implements Serializable {
    private static final long serialVersionUID = 7198370938692066125L;
    /**
     主键
     */
    private Long rkey;
    /**
     机器码
     */
    private String qrCode;
    /**
     创建时间
     */
    private String createDate;
    /**
     点检图
     */
    private String imagesAddress;
    /**
     点检人
     */
    private String userName;
    /**
     二维码图
     */
    private String qrImagesAddress;
    /**
     点检图
     */
    private String imagesAddress2;
    /**
     点检图
     */
    private String imagesAddress3;
    /**
     点检图
     */
    private String imagesAddress4;
    /**
     点检图
     */
    private String imagesAddress5;
    /**
     备注
     */
    private String breaks;
    /**
     部门
     */
    private String deptName;
    /**
     地址
     */
    private String deptAddress;
    /**
     厂别
     */
    private String plant;
    /**
     线别
     */
    private String xianbie;

    public Long getRkey() {
        return rkey;
    }

    public void setRkey(Long rkey) {
        this.rkey = rkey;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getImagesAddress() {
        return imagesAddress;
    }

    public void setImagesAddress(String imagesAddress) {
        this.imagesAddress = imagesAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getQrImagesAddress() {
        return qrImagesAddress;
    }

    public void setQrImagesAddress(String qrImagesAddress) {
        this.qrImagesAddress = qrImagesAddress;
    }

    public String getImagesAddress2() {
        return imagesAddress2;
    }

    public void setImagesAddress2(String imagesAddress2) {
        this.imagesAddress2 = imagesAddress2;
    }

    public String getImagesAddress3() {
        return imagesAddress3;
    }

    public void setImagesAddress3(String imagesAddress3) {
        this.imagesAddress3 = imagesAddress3;
    }

    public String getImagesAddress4() {
        return imagesAddress4;
    }

    public void setImagesAddress4(String imagesAddress4) {
        this.imagesAddress4 = imagesAddress4;
    }

    public String getImagesAddress5() {
        return imagesAddress5;
    }

    public void setImagesAddress5(String imagesAddress5) {
        this.imagesAddress5 = imagesAddress5;
    }

    public String getBreaks() {
        return breaks;
    }

    public void setBreaks(String breaks) {
        this.breaks = breaks;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptAddress() {
        return deptAddress;
    }

    public void setDeptAddress(String deptAddress) {
        this.deptAddress = deptAddress;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getXianbie() {
        return xianbie;
    }

    public void setXianbie(String xianbie) {
        this.xianbie = xianbie;
    }
}
