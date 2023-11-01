package com.jovefast.flowable.domain.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description Description
 * @Author Acechengui
 * @Date Created in 2023-07-31
 */
public class HistoricProcessInstanceDTO implements Serializable {

    private static final long serialVersionUID = 8934837085233966781L;

    private Long startUserId;

    private String processTitle;

    private String resId;

    private String procDefId;

    private Date startTime;

    private Date endTime;

    private String procInsId;

    private String procDefKey;

    private String porcDefName;

    private Long formId;

    public Long getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(Long startUserId) {
        this.startUserId = startUserId;
    }

    public String getProcessTitle() {
        return processTitle;
    }

    public void setProcessTitle(String processTitle) {
        this.processTitle = processTitle;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    public String getProcDefKey() {
        return procDefKey;
    }

    public void setProcDefKey(String procDefKey) {
        this.procDefKey = procDefKey;
    }

    public String getPorcDefName() {
        return porcDefName;
    }

    public void setPorcDefName(String porcDefName) {
        this.porcDefName = porcDefName;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }
}
