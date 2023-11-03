package com.jovefast.flowable.service;

import com.jovefast.flowable.domain.dto.FlowNextDto;
import com.jovefast.flowable.domain.dto.FlowTaskDto;
import com.jovefast.flowable.domain.dto.FlowViewerDto;
import com.jovefast.flowable.domain.vo.FlowTaskVo;
import org.flowable.bpmn.model.UserTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Acecehgnui
 */
public interface IFlowTaskService {

    /**
     * 任务归还
     *
     * @param flowTaskVo 请求实体参数
     */
    void resolveTask(FlowTaskVo flowTaskVo);


    /**
     * 多实例加签
     */
    void addMultiInstanceExecution(FlowTaskVo flowTaskVo);

    /**
     * 多实例减签
     */
    void deleteMultiInstanceExecution(FlowTaskVo flowTaskVo);


    /**
     * 流程节点信息
     */
    Map<String, Object> flowXmlAndNode(String procInsId,String deployId) throws IOException;

    /**
     * 单个审批任务
     *
     * @param task 请求实体参数
     */
    Boolean complete(FlowTaskVo task);

    /**
     * 批量审批任务
     * @param taskIds 请求参数
     */
    Boolean batchComplete(String[] taskIds);

    /**
     * 驳回任务
     *
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
     */
    List<UserTask> findReturnTaskList(FlowTaskVo flowTaskVo);

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
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @param params 参数
     */
    Map<String, Object> myProcess(Integer pageNum, Integer pageSize,FlowTaskDto params);

    /**
     * 取消申请
     * @param flowTaskVo 参数
     */
    boolean stopProcess(FlowTaskVo flowTaskVo);


    /**
     * 代办任务列表
     *
     * @param pageNum  当前页码
     * @param pageSize 每页条数
     * @param flowtaskdto 参数
     * @return 代办任务列表
     */
    List<FlowTaskDto> todoList(Integer pageNum, Integer pageSize,FlowTaskDto flowtaskdto);


    /**
     * 已办任务列表
     *
     * @param pageNum  当前页码
     * @param pageSize 每页条数
     * @param flowTaskDto 参数
     */
    Map<String, Object> finishedList(Integer pageNum, Integer pageSize,FlowTaskDto flowTaskDto);

    /**
     * 流程历史流转记录
     *
     * @param procInsId 流程实例Id
     * @param deployId 部署id
     */
    Map<String, Object> flowRecord(String procInsId, String deployId);


    /**
     * 获取流程过程图
     * @param processId 流程ID
     */
    InputStream diagram(String processId);

    /**
     * 获取流程执行节点
     * @param procInsId 流程实例ID
     */
    List<FlowViewerDto> getFlowViewer(String procInsId, String executionId);

    /**
     * 获取流程变量
     * @param taskId 任务ID
     */
    Map<String, Object> processVariables(String taskId);

    /**
     * 获取下一节点
     * @param flowTaskVo 任务
     */
    FlowNextDto getNextFlowNode(FlowTaskVo flowTaskVo);

    FlowNextDto getNextFlowNodeByStart(FlowTaskVo flowTaskVo);
}
