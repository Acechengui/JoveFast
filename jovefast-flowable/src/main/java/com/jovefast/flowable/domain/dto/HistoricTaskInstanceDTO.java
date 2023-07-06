package com.jovefast.flowable.domain.dto;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Description Description
 * @Author Acechengui
 * @Date Created in 2023/6/23
 */
public class HistoricTaskInstanceDTO  implements Serializable {
    private static final long serialVersionUID = -459072742027329245L;

    private String idRev;
    private String processTitle;
    private String procDefId;
    private String taskDefId;
    private String taskDefKey;
    private String procInstId;
    private String executionId;
    private String scopeId;
    private String subScopeId;
    private String scopeDefinitionId;
    private String propagatedStageInstId;
    private String nname;
    private String parentTaskId;
    private String description;
    private Integer owners;
    private Integer assignee;
    private Timestamp startTime;
    private Timestamp claimTime;
    private Timestamp endTime;
    private Long duration;
    private String deleteReason;
    private Integer priority;
    private String dueDate;
    private String formKey;
    private String category;
    private String tenantId;
    private Timestamp LastUpdatedTime;

    private String startUserName;

    private String startUserId;
    private String startDeptName;

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

    public String getProcessTitle() {
        return processTitle;
    }

    public void setProcessTitle(String processTitle) {
        this.processTitle = processTitle;
    }

    public String getIdRev() {
        return idRev;
    }

    public void setIdRev(String idRev) {
        this.idRev = idRev;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public String getTaskDefId() {
        return taskDefId;
    }

    public void setTaskDefId(String taskDefId) {
        this.taskDefId = taskDefId;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
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

    public String getScopeId() {
        return scopeId;
    }

    public void setScopeId(String scopeId) {
        this.scopeId = scopeId;
    }

    public String getSubScopeId() {
        return subScopeId;
    }

    public void setSubScopeId(String subScopeId) {
        this.subScopeId = subScopeId;
    }

    public String getScopeDefinitionId() {
        return scopeDefinitionId;
    }

    public void setScopeDefinitionId(String scopeDefinitionId) {
        this.scopeDefinitionId = scopeDefinitionId;
    }

    public String getPropagatedStageInstId() {
        return propagatedStageInstId;
    }

    public void setPropagatedStageInstId(String propagatedStageInstId) {
        this.propagatedStageInstId = propagatedStageInstId;
    }

    public String getNname() {
        return nname;
    }

    public void setNname(String nname) {
        this.nname = nname;
    }

    public String getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Timestamp getLastUpdatedTime() {
        return LastUpdatedTime;
    }

    public void setLastUpdatedTime(Timestamp lastUpdatedTime) {
        LastUpdatedTime = lastUpdatedTime;
    }
}
