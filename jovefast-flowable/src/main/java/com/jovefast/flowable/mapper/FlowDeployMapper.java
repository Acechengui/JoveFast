package com.jovefast.flowable.mapper;

import com.jovefast.flowable.domain.SysProcessTitle;
import com.jovefast.flowable.domain.dto.FlowProcDefDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流程定义查询
 *
 * @author Acechegui
 **/
public interface FlowDeployMapper {

    /**
     * 流程定义列表
     * @return
     */
    List<FlowProcDefDto> selectDeployList(String name);

    /**
     * 各个流程定义最新版本列表
     * @param name 流程名称
     * @param tenantId 租户标识
     */
    List<FlowProcDefDto> selectDeployListLast(@Param("name") String name);

    /**
     * 流程标题信息
     * @param procInsId 流程ID
     */
    SysProcessTitle selectSysProcessTitle(@Param("procInsId") String procInsId);

    /**
     * 增加流程标题
     * @param param 参数
     */
    int insertSysProcessTitle(SysProcessTitle param);

    /**
     * 根据流程ID删除相关流程标题
     * @param procInsId 流程ID
     */
    int deleteSysProcessTitleByProcInsId(@Param("procInsId") String procInsId);
}