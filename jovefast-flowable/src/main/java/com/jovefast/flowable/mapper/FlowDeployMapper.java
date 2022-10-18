package com.jovefast.flowable.mapper;

import com.jovefast.flowable.domain.dto.FlowProcDefDto;

import java.util.List;

/**
 * 流程定义查询
 *
 * @author Acechegui
 **/
public interface FlowDeployMapper {

    /**
     * 流程定义列表
     * @param name
     * @return
     */
    List<FlowProcDefDto> selectDeployList(String name);
}