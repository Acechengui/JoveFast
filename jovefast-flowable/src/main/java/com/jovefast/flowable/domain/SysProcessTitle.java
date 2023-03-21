package com.jovefast.flowable.domain;

import java.io.Serializable;

/**
 * @Description 流程实体标题
 * @Author Acechengui
 * @Date Created in 2023/3/20
 */
public class SysProcessTitle implements Serializable {
    private static final long serialVersionUID = 2671559230504380549L;

    /**
     * 自增长列ID
     */
    private Long id;

    /**
     * 流程实例ID
     */
    private String procInsId;

    /**
     * 流程标题
     */
    private String processTitle;

    public SysProcessTitle() {
    }

    public SysProcessTitle(String procInsId, String processTitle) {
        this.procInsId = procInsId;
        this.processTitle = processTitle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    public String getProcessTitle() {
        return processTitle;
    }

    public void setProcessTitle(String processTitle) {
        this.processTitle = processTitle;
    }
}
