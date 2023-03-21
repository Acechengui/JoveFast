package com.jovefast.flowable.service.impl;


import com.jovefast.common.security.utils.SecurityUtils;
import com.jovefast.flowable.factory.FlowServiceFactory;
import com.jovefast.flowable.mapper.FlowDeployMapper;
import com.jovefast.flowable.service.IFlowInstanceService;
import org.apache.commons.lang3.ObjectUtils;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>工作流流程实例管理<p>
 *
 * @author Acecehgnui
 */
@Service
public class FlowInstanceServiceImpl extends FlowServiceFactory implements IFlowInstanceService {

    @Autowired
    private FlowDeployMapper flowDeployMapper;

    @Override
    public List<Task> queryListByInstanceId(String instanceId) {
        return taskService.createTaskQuery().processInstanceId(instanceId).active().list();
    }

    /**
     * 激活或挂起流程实例
     *
     * @param state      状态
     * @param instanceId 流程实例ID
     */
    @Override
    public void updateState(Integer state, String instanceId) {

        // 激活
        if (state == 1) {
            runtimeService.activateProcessInstanceById(instanceId);
        }
        // 挂起
        if (state == 2) {
            runtimeService.suspendProcessInstanceById(instanceId);
        }
    }

    /**
     * 删除流程实例ID
     *
     * @param instanceId   流程实例ID
     * @param deleteReason 删除原因
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String instanceId, String deleteReason) {

        // 查询历史数据
        HistoricProcessInstance historicProcessInstance = getHistoricProcessInstanceById(instanceId);
        if (historicProcessInstance.getEndTime() != null) {
            flowDeployMapper.deleteSysProcessTitleByProcInsId(historicProcessInstance.getId());
            historyService.deleteHistoricProcessInstance(historicProcessInstance.getId());
            return;
        }
        //删除流程标题
        flowDeployMapper.deleteSysProcessTitleByProcInsId(historicProcessInstance.getId());
        // 删除流程实例
        runtimeService.deleteProcessInstance(instanceId, deleteReason);

        // 删除历史流程实例
        historyService.deleteHistoricProcessInstance(instanceId);
    }

    /**
     * 根据实例ID查询历史实例数据
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public HistoricProcessInstance getHistoricProcessInstanceById(String processInstanceId) {
        HistoricProcessInstance historicProcessInstance =
                historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (Objects.isNull(historicProcessInstance)) {
            throw new FlowableObjectNotFoundException("流程实例不存在: " + processInstanceId);
        }
        return historicProcessInstance;
    }

    /**
     * 根据流程定义ID启动流程实例
     *
     * @param procDefId 流程定义Id
     * @param variables 流程变量
     * @return
     */
    @Override
    @Transactional
    public boolean startProcessInstanceById(String procDefId,Map<String, Object> variables) {
        // 设置流程发起人Id到流程中
        Long userId = SecurityUtils.getLoginUser().getUserid();
        variables.put("initiator",userId);
        variables.put("_FLOWABLE_SKIP_EXPRESSION_ENABLED", true);
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(procDefId, variables);
        return ObjectUtils.allNotNull(processInstance);
    }
}