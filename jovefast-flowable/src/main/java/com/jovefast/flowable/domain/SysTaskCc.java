package com.jovefast.flowable.domain;

/**
 * 任务与抄送关联表.
 *
 * @author Acechengui
 * @version 1.0
 * @since 2023-11-20
 */

public class SysTaskCc {

    /**
     * 流程实例ID
     */
    private String instanceId;

    /**
     * 抄送用户ID
     */
    private String ccUid;

    public SysTaskCc() {
    }

    public SysTaskCc(String instanceId, String ccUid) {
        this.instanceId = instanceId;
        this.ccUid = ccUid;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getCcUid() {
        return ccUid;
    }

    public void setCcUid(String ccUid) {
        this.ccUid = ccUid;
    }
}
