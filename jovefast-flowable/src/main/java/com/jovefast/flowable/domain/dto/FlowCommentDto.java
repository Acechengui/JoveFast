package com.jovefast.flowable.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Acecehgnui
 */
@Data
@Builder
public class FlowCommentDto implements Serializable {

    private static final long serialVersionUID = -396052407856367618L;
    /**
     * 意见类别 0 正常意见  1 退回意见 2 驳回意见
     */
    private String type;

    /**
     * 意见内容
     */
    private String comment;
}
