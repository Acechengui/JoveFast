package com.jovefast.flowable.mapper;

import com.jovefast.flowable.domain.SysTaskCc;
import com.jovefast.flowable.domain.dto.CourtesyCopyDTO;
import com.jovefast.flowable.domain.dto.FlowTaskDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * .
 *
 * @author Acechengui
 * @version 1.0
 * @since 2023-11-20
 */

public interface SysTaskCcMapper {

    SysTaskCc selectSysTaskCcByinstanceId(@Param("instanceId") String instanceId);

    Integer selectSysTaskCcCountByinstanceId(@Param("instanceId") String instanceId);

    Integer deleteSysTaskCcByinstanceId(@Param("instanceId") String instanceId);
    Integer insertSysTaskCc(List<SysTaskCc> list);

    List<CourtesyCopyDTO> selectSysTaskCcList(FlowTaskDto param);

}
