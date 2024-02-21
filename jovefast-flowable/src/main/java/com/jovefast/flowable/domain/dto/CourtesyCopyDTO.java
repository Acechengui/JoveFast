package com.jovefast.flowable.domain.dto;

import java.sql.Timestamp;

/**
 * 抄送信息.
 *
 * @author Acechengui
 * @version 1.0
 * @since 2023-11-20
 */

public class CourtesyCopyDTO {

    private String taskId;
    private String processTitle;
    private String procDefId;
    private String procInstId;
    private String executionId;
    private String nname;
    private Integer owners;
    private Integer assignee;
    private Timestamp startTime;
    private Timestamp claimTime;
    private String tenantId;
    private String startUserName;
    private String startUserId;
    private String startDeptName;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProcessTitle() {
        return processTitle;
    }

    public void setProcessTitle(String processTitle) {
        this.processTitle = processTitle;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getNname() {
        return nname;
    }

    public void setNname(String nname) {
        this.nname = nname;
    }

    public Integer getOwners() {
        return owners;
    }

    public void setOwners(Integer owners) {
        this.owners = owners;
    }

    public Integer getAssignee() {
        return assignee;
    }

    public void setAssignee(Integer assignee) {
        this.assignee = assignee;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getClaimTime() {
        return claimTime;
    }

    public void setClaimTime(Timestamp claimTime) {
        this.claimTime = claimTime;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getStartUserName() {
        return startUserName;
    }

    public void setStartUserName(String startUserName) {
        this.startUserName = startUserName;
    }

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    public String getStartDeptName() {
        return startDeptName;
    }

    public void setStartDeptName(String startDeptName) {
        this.startDeptName = startDeptName;
    }
}
