package com.jovefast.flowable.service;

import com.jovefast.common.core.web.domain.AjaxResult;
import com.jovefast.flowable.domain.dto.FlowTaskDto;
import com.jovefast.flowable.domain.dto.FlowViewerDto;
import com.jovefast.flowable.domain.vo.FlowTaskVo;
import org.flowable.task.api.Task;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Acecehgnui
 */
public interface IFlowTaskService {

    /**
     * 审批任务
     *
     * @param task 请求实体参数
     */
    AjaxResult complete(FlowTaskVo task);

    /**
     * 驳回任务
     *
     * @param flowTaskVo
     */
    void taskReject(FlowTaskVo flowTaskVo);


    /**
     * 退回任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void taskReturn(FlowTaskVo flowTaskVo);

    /**
     * 获取所有可回退的节点
     *
     * @param flowTaskVo
     * @return
     */
    AjaxResult findReturnTaskList(FlowTaskVo flowTaskVo);

    /**
     * 删除任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void deleteTask(FlowTaskVo flowTaskVo);

    /**
     * 认领/签收任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void claim(FlowTaskVo flowTaskVo);

    /**
     * 取消认领/签收任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void unClaim(FlowTaskVo flowTaskVo);

    /**
     * 委派任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void delegateTask(FlowTaskVo flowTaskVo);


    /**
     * 转办任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void assignTask(FlowTaskVo flowTaskVo);

    /**
     * 我发起的流程
     * @param pageNum
     * @param pageSize
     */
    List<FlowTaskDto> myProcess(Integer pageNum, Integer pageSize);

    /**
     * 取消申请
     * @param flowTaskVo 参数
     */
    AjaxResult stopProcess(FlowTaskVo flowTaskVo);


    /**
     * 代办任务列表
     *
     * @param pageNum  当前页码
     * @param pageSize 每页条数
     * @return 代办任务列表
     */
    List<FlowTaskDto> todoList(Integer pageNum, Integer pageSize);


    /**
     * 已办任务列表
     *
     * @param pageNum  当前页码
     * @param pageSize 每页条数
     */
    List<FlowTaskDto> finishedList(Integer pageNum, Integer pageSize);

    /**
     * 流程历史流转记录
     *
     * @param procInsId 流程实例Id
     * @param deployId 部署id
     */
    Map<String, Object> flowRecord(String procInsId, String deployId);

    /**
     * 根据任务ID查询挂载的表单信息
     *
     * @param taskId 任务Id
     */
    Task getTaskForm(String taskId);

    /**
     * 获取流程过程图
     * @param processId 流程ID
     */
    InputStream diagram(String processId);

    /**
     * 获取流程执行过程
     * @param procInsId 流程实例ID
     */
    List<FlowViewerDto> getFlowViewer(String procInsId, String executionId);

    /**
     * 获取流程变量
     * @param taskId 任务ID
     */
    AjaxResult processVariables(String taskId);

    /**
     * 获取下一节点
     * @param flowTaskVo 任务
     */
    AjaxResult getNextFlowNode(FlowTaskVo flowTaskVo);
}
