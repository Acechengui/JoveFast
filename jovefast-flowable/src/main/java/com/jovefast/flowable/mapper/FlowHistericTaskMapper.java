package com.jovefast.flowable.mapper;

import com.jovefast.flowable.domain.dto.FlowTaskDto;
import com.jovefast.flowable.domain.dto.HistoricTaskInstanceDTO;

import java.util.List;

/**
 * @Description 历史任务数据层
 * @Author Acechengui
 * @Date Created in 2023/6/23
 */
public interface FlowHistericTaskMapper {

    List<HistoricTaskInstanceDTO>  selectFlowHistericTaskInstance(FlowTaskDto param);
}
