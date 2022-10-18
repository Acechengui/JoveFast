package com.jovefast.flowable.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Acechengui
 */
@Data
public class FlowViewerDto implements Serializable {

    /**
     * 流程key
     */
    private String key;

    /**
     * 是否完成(已经审批)
     */
    private boolean completed;
}
