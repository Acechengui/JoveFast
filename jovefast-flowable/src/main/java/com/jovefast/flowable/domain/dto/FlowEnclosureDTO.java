package com.jovefast.flowable.domain.dto;

import com.jovefast.common.core.web.domain.BaseEntity;

import java.io.Serializable;

/**
 * @Description 附件表
 * @Author Acechengui
 * @Date Created in 2023/3/13
 */
public class FlowEnclosureDTO extends BaseEntity {
    private static final long serialVersionUID = 2166296814945535235L;

    private String hid;
    private String hrev;

    private Long huserId;

    private String hname;

    private String hdescription;

    private String htype;

    private String htaskId;

    private String hprocinstId;

    private String hurl;

    private String hcontentId;

    private String htime;

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getHrev() {
        return hrev;
    }

    public void setHrev(String hrev) {
        this.hrev = hrev;
    }

    public Long getHuserId() {
        return huserId;
    }

    public void setHuserId(Long huserId) {
        this.huserId = huserId;
    }

    public String getHname() {
        return hname;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }

    public String getHdescription() {
        return hdescription;
    }

    public void setHdescription(String hdescription) {
        this.hdescription = hdescription;
    }

    public String getHtype() {
        return htype;
    }

    public void setHtype(String htype) {
        this.htype = htype;
    }

    public String getHtaskId() {
        return htaskId;
    }

    public void setHtaskId(String htaskId) {
        this.htaskId = htaskId;
    }

    public String getHprocinstId() {
        return hprocinstId;
    }

    public void setHprocinstId(String hprocinstId) {
        this.hprocinstId = hprocinstId;
    }

    public String getHurl() {
        return hurl;
    }

    public void setHurl(String hurl) {
        this.hurl = hurl;
    }

    public String getHcontentId() {
        return hcontentId;
    }

    public void setHcontentId(String hcontentId) {
        this.hcontentId = hcontentId;
    }

    public String getHtime() {
        return htime;
    }

    public void setHtime(String htime) {
        this.htime = htime;
    }
}
