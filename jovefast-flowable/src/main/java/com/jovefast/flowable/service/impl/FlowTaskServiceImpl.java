package com.jovefast.flowable.service.impl;


import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.jovefast.common.core.constant.HttpStatus;
import com.jovefast.common.core.constant.SecurityConstants;
import com.jovefast.common.core.domain.R;
import com.jovefast.common.core.exception.CheckedException;
import com.jovefast.common.core.utils.DateUtils;
import com.jovefast.common.core.utils.SpringUtils;
import com.jovefast.common.security.utils.SecurityUtils;
import com.jovefast.flowable.common.constant.ProcessConstants;
import com.jovefast.flowable.common.enums.FlowComment;
import com.jovefast.flowable.domain.SysForm;
import com.jovefast.flowable.domain.SysProcessTitle;
import com.jovefast.flowable.domain.SysTaskCc;
import com.jovefast.flowable.domain.dto.*;
import com.jovefast.flowable.domain.vo.FlowTaskVo;
import com.jovefast.flowable.factory.FlowServiceFactory;
import com.jovefast.flowable.flow.CustomProcessDiagramGenerator;
import com.jovefast.flowable.flow.FindNextNodeUtil;
import com.jovefast.flowable.flow.FlowableUtils;
import com.jovefast.flowable.mapper.FlowDeployMapper;
import com.jovefast.flowable.mapper.FlowHistericTaskMapper;
import com.jovefast.flowable.mapper.SysTaskCcMapper;
import com.jovefast.flowable.service.IFlowTaskService;
import com.jovefast.flowable.service.ISysDeployFormService;
import com.jovefast.system.api.RemoteUserService;
import com.jovefast.system.api.domain.SysRole;
import com.jovefast.system.api.domain.SysUser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.MultiInstanceLoopCharacteristics;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.cmd.AddMultiInstanceExecutionCmd;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.idm.api.Group;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Acecehgnui
 **/
@Service
public class FlowTaskServiceImpl extends FlowServiceFactory implements IFlowTaskService {

    @Resource
    private RemoteUserService remoteuserservice;

    @Resource
    private ISysDeployFormService sysInstanceFormService;

    @Resource
    private FlowDeployMapper flowDeployMapper;

    @Resource
    private FlowHistericTaskMapper flowHistericTaskMapper;

    @Resource
    private SysTaskCcMapper sysTaskCcMapper;


    /**
     * 任务归还
     * 被委派人完成任务之后，将任务归还委派人
     *
     * @param flowTaskVo 请求实体参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resolveTask(FlowTaskVo flowTaskVo) {
        taskService.resolveTask(flowTaskVo.getTaskId());
    }


    /**
     * 多实例加签
     * act_ru_task、act_ru_identitylink各生成一条记录
     *
     */
    @Override
    public void addMultiInstanceExecution(FlowTaskVo flowTaskVo) {
        managementService.executeCommand(new AddMultiInstanceExecutionCmd(flowTaskVo.getDefId(), flowTaskVo.getInstanceId(), flowTaskVo.getVariables()));
    }


    @Override
    public void deleteMultiInstanceExecution(FlowTaskVo flowTaskVo) {

    }

    @Override
    public Map<String, Object> flowXmlAndNode(String procInsId, String deployId) throws IOException {
        List<FlowViewerDto> flowViewerList = new ArrayList<>();
        // 获取已经完成的节点
        List<HistoricActivityInstance> listFinished = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInsId)
                .finished()
                .list();

        // 保存已经完成的流程节点编号
        listFinished.forEach(s -> {
            FlowViewerDto flowViewerDto = new FlowViewerDto();
            flowViewerDto.setKey(s.getActivityId());
            flowViewerDto.setCompleted(true);
            flowViewerList.add(flowViewerDto);
        });

        // 获取代办节点
        List<HistoricActivityInstance> listUnFinished = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInsId)
                .unfinished()
                .list();

        // 保存需要代办的节点编号
        listUnFinished.forEach(s -> {
            FlowViewerDto flowViewerDto = new FlowViewerDto();
            flowViewerDto.setKey(s.getActivityId());
            flowViewerDto.setCompleted(false);
            flowViewerList.add(flowViewerDto);
        });
        Map<String, Object> result = new HashMap<>();
        // xmlData 数据
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
        InputStream inputStream = repositoryService.getResourceAsStream(definition.getDeploymentId(), definition.getResourceName());
        String xmlData = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        result.put("nodeData", flowViewerList);
        result.put("xmlData", xmlData);
        return result;
    }


    /**
     * 单个完成任务
     *
     * @param taskVo 请求实体参数
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean complete(FlowTaskVo taskVo) {
        Task task = taskService.createTaskQuery().taskId(taskVo.getTaskId()).singleResult();
        if (Objects.isNull(task)) {
            throw new CheckedException("任务不存在");
        }
        if (DelegationState.PENDING.equals(task.getDelegationState())) {
            taskService.addComment(taskVo.getTaskId(), taskVo.getInstanceId(), FlowComment.DELEGATE.getType(), taskVo.getComment());
            taskService.resolveTask(taskVo.getTaskId(), taskVo.getValues());
        } else {
            taskService.addComment(taskVo.getTaskId(), taskVo.getInstanceId(), FlowComment.NORMAL.getType(), taskVo.getComment());
            Long userId = SecurityUtils.getLoginUser().getSysUser().getUserId();
            taskService.setAssignee(taskVo.getTaskId(), userId.toString());
            //更新全局变量
            taskService.setVariables(taskVo.getTaskId(),taskVo.getVariables());
            taskService.complete(taskVo.getTaskId(), taskVo.getValues());
        }
        return true;
    }

    /**
     * 批量完成任务
     *
     * @param taskIds 请求实体参数
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean batchComplete(String[] taskIds) {
        Long userId = SecurityUtils.getLoginUser().getSysUser().getUserId();
        for (String ts:taskIds) {
            taskService.setAssignee(ts, userId.toString());
            taskService.complete(ts);
        }
        return true;
    }

    /**
     * 抄送任务
     *
     * @param task 请求实体参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean courtesyCopy(FlowTaskVo task) {
        //根据任务id匹配,若已存在任务id,便覆盖,不存在,则插入
        Integer count = sysTaskCcMapper.selectSysTaskCcCountByinstanceId(task.getInstanceId());
        if(count > 0){
            sysTaskCcMapper.deleteSysTaskCcByinstanceId(task.getInstanceId());
        }
        List<SysTaskCc> ccList= Arrays.stream(task.getUserId().split(",")).map(s -> new SysTaskCc(task.getInstanceId(), s)).toList();

        //在此处可发送企业微信消息等通知,自行结合业务实现

        return sysTaskCcMapper.insertSysTaskCc(ccList) > 0;
    }

    /**
     * 驳回任务
     *
     * @param flowTaskVo 参数
     */
    @Override
    public void taskReject(FlowTaskVo flowTaskVo) {
        if (taskService.createTaskQuery().taskId(flowTaskVo.getTaskId()).singleResult().isSuspended()) {
            throw new CheckedException("任务处于挂起状态!");
        }
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(flowTaskVo.getTaskId()).singleResult();
        // 获取流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        // 获取所有节点信息
        Process process = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses().get(0);
        // 获取全部节点列表，包含子节点
        Collection<FlowElement> allElements = FlowableUtils.getAllElements(process.getFlowElements(), null);
        // 获取当前任务节点元素
        FlowElement source = null;
        if (allElements != null) {
            for (FlowElement flowElement : allElements) {
                // 类型为用户节点
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    // 获取节点信息
                    source = flowElement;
                }
            }
        }

        // 目的获取所有跳转到的节点 targetIds
        // 获取当前节点的所有父级用户任务节点
        // 深度优先算法思想：延边迭代深入
        List<UserTask> parentUserTaskList = FlowableUtils.iteratorFindParentUserTasks(source, null, null);
        if (parentUserTaskList == null || parentUserTaskList.size() == 0) {
            throw new CheckedException("当前节点为初始任务节点，不能驳回");
        }
        // 获取活动 ID 即节点 Key
        List<String> parentUserTaskKeyList = new ArrayList<>();
        parentUserTaskList.forEach(item -> parentUserTaskKeyList.add(item.getId()));
        // 获取全部历史节点活动实例，即已经走过的节点历史，数据采用开始时间升序
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(task.getProcessInstanceId()).orderByHistoricTaskInstanceStartTime().asc().list();
        // 数据清洗，将回滚导致的脏数据清洗掉
        List<String> lastHistoricTaskInstanceList = FlowableUtils.historicTaskInstanceClean(allElements, historicTaskInstanceList);
        // 此时历史任务实例为倒序，获取最后走的节点
        List<String> targetIds = new ArrayList<>();
        // 循环结束标识，遇到当前目标节点的次数
        int number = 0;
        StringBuilder parentHistoricTaskKey = new StringBuilder();
        for (String historicTaskInstanceKey : lastHistoricTaskInstanceList) {
            // 当会签时候会出现特殊的，连续都是同一个节点历史数据的情况，这种时候跳过
            if (parentHistoricTaskKey.toString().equals(historicTaskInstanceKey)) {
                continue;
            }
            parentHistoricTaskKey = new StringBuilder(historicTaskInstanceKey);
            if (historicTaskInstanceKey.equals(task.getTaskDefinitionKey())) {
                number++;
            }
            // 在数据清洗后，历史节点就是唯一一条从起始到当前节点的历史记录，理论上每个点只会出现一次
            // 在流程中如果出现循环，那么每次循环中间的点也只会出现一次，再出现就是下次循环
            // number == 1，第一次遇到当前节点
            // number == 2，第二次遇到，代表最后一次的循环范围
            if (number == 2) {
                break;
            }
            // 如果当前历史节点，属于父级的节点，说明最后一次经过了这个点，需要退回这个点
            if (parentUserTaskKeyList.contains(historicTaskInstanceKey)) {
                targetIds.add(historicTaskInstanceKey);
            }
        }


        // 目的获取所有需要被跳转的节点 currentIds
        // 取其中一个父级任务，因为后续要么存在公共网关，要么就是串行公共线路
        UserTask oneUserTask = parentUserTaskList.get(0);
        // 获取所有正常进行的任务节点 Key，这些任务不能直接使用，需要找出其中需要撤回的任务
        List<Task> runTaskList = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        List<String> runTaskKeyList = new ArrayList<>();
        runTaskList.forEach(item -> runTaskKeyList.add(item.getTaskDefinitionKey()));
        // 需驳回任务列表
        List<String> currentIds = new ArrayList<>();
        // 通过父级网关的出口连线，结合 runTaskList 比对，获取需要撤回的任务
        List<UserTask> currentUserTaskList = FlowableUtils.iteratorFindChildUserTasks(oneUserTask, runTaskKeyList, null, null);
        currentUserTaskList.forEach(item -> currentIds.add(item.getId()));


        // 规定：并行网关之前节点必须需存在唯一用户任务节点，如果出现多个任务节点，则并行网关节点默认为结束节点，原因为不考虑多对多情况
        if (targetIds.size() > 1 && currentIds.size() > 1) {
            throw new CheckedException("任务出现多对多情况，无法撤回");
        }

        // 循环获取那些需要被撤回的节点的ID，用来设置驳回原因
        List<String> currentTaskIds = new ArrayList<>();
        currentIds.forEach(currentId -> runTaskList.forEach(runTask -> {
            if (currentId.equals(runTask.getTaskDefinitionKey())) {
                currentTaskIds.add(runTask.getId());
            }
        }));
        // 设置驳回意见
        currentTaskIds.forEach(item -> taskService.addComment(item, task.getProcessInstanceId(), FlowComment.REJECT.getType(), flowTaskVo.getComment()));

        try {
            // 如果父级任务多于 1 个，说明当前节点不是并行节点，原因为不考虑多对多情况
            if (targetIds.size() > 1) {
                // 1 对 多任务跳转，currentIds 当前节点(1)，targetIds 跳转到的节点(多)
                runtimeService.createChangeActivityStateBuilder()
                        .processInstanceId(task.getProcessInstanceId()).
                        moveSingleActivityIdToActivityIds(currentIds.get(0), targetIds).changeState();
            }
            // 如果父级任务只有一个，因此当前任务可能为网关中的任务
            if (targetIds.size() == 1) {
                // 1 对 1 或 多 对 1 情况，currentIds 当前要跳转的节点列表(1或多)，targetIds.get(0) 跳转到的节点(1)
                runtimeService.createChangeActivityStateBuilder()
                        .processInstanceId(task.getProcessInstanceId())
                        .moveActivityIdsToSingleActivityId(currentIds, targetIds.get(0)).changeState();
            }
        } catch (FlowableObjectNotFoundException e) {
            throw new CheckedException("未找到流程实例，流程可能已发生变化");
        } catch (FlowableException e) {
            throw new CheckedException("无法取消或开始活动");
        }


    }

    /**
     * 退回任务
     *
     * @param flowTaskVo 请求实体参数
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void taskReturn(FlowTaskVo flowTaskVo) {
        if (taskService.createTaskQuery().taskId(flowTaskVo.getTaskId()).singleResult().isSuspended()) {
            throw new CheckedException("任务处于挂起状态");
        }
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(flowTaskVo.getTaskId()).singleResult();
        // 获取流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        // 获取所有节点信息
        Process process = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses().get(0);
        // 获取全部节点列表，包含子节点
        Collection<FlowElement> allElements = FlowableUtils.getAllElements(process.getFlowElements(), null);
        // 获取当前任务节点元素
        FlowElement source = null;
        // 获取跳转的节点元素
        FlowElement target = null;
        if (allElements != null) {
            for (FlowElement flowElement : allElements) {
                // 当前任务节点元素
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    source = flowElement;
                }
                // 跳转的节点元素
                if (flowElement.getId().equals(flowTaskVo.getTargetKey())) {
                    target = flowElement;
                }
            }
        }

        // 从当前节点向前扫描
        // 如果存在路线上不存在目标节点，说明目标节点是在网关上或非同一路线上，不可跳转
        // 否则目标节点相对于当前节点，属于串行
        Boolean isSequential = FlowableUtils.iteratorCheckSequentialReferTarget(source, flowTaskVo.getTargetKey(), null, null);
        if (!isSequential) {
            throw new CheckedException("当前节点相对于目标节点，不属于串行关系，无法回退");
        }


        // 获取所有正常进行的任务节点 Key，这些任务不能直接使用，需要找出其中需要撤回的任务
        List<Task> runTaskList = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        List<String> runTaskKeyList = new ArrayList<>();
        runTaskList.forEach(item -> runTaskKeyList.add(item.getTaskDefinitionKey()));
        // 需退回任务列表
        List<String> currentIds = new ArrayList<>();
        // 通过父级网关的出口连线，结合 runTaskList 比对，获取需要撤回的任务
        List<UserTask> currentUserTaskList = FlowableUtils.iteratorFindChildUserTasks(target, runTaskKeyList, null, null);
        currentUserTaskList.forEach(item -> currentIds.add(item.getId()));

        // 循环获取那些需要被撤回的节点的ID，用来设置驳回原因
        List<String> currentTaskIds = new ArrayList<>();
        currentIds.forEach(currentId -> runTaskList.forEach(runTask -> {
            if (currentId.equals(runTask.getTaskDefinitionKey())) {
                currentTaskIds.add(runTask.getId());
            }
        }));
        // 设置回退意见
        currentTaskIds.forEach(currentTaskId -> taskService.addComment(currentTaskId, task.getProcessInstanceId(), FlowComment.REBACK.getType(), flowTaskVo.getComment()));

        try {
            // 1 对 1 或 多 对 1 情况，currentIds 当前要跳转的节点列表(1或多)，targetKey 跳转到的节点(1)
            runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(task.getProcessInstanceId())
                    .moveActivityIdsToSingleActivityId(currentIds, flowTaskVo.getTargetKey()).changeState();
        } catch (FlowableObjectNotFoundException e) {
            throw new CheckedException("未找到流程实例，流程可能已发生变化");
        } catch (FlowableException e) {
            throw new CheckedException("无法取消或开始活动");
        }
    }


    /**
     * 获取所有可回退的节点
     *
     * @param flowTaskVo 参数
     */
    @Override
    public List<UserTask> findReturnTaskList(FlowTaskVo flowTaskVo) {
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(flowTaskVo.getTaskId()).singleResult();
        // 获取流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        // 获取所有节点信息，暂不考虑子流程情况
        Process process = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses().get(0);
        Collection<FlowElement> flowElements = process.getFlowElements();
        // 获取当前任务节点元素
        UserTask source = null;
        if (flowElements != null) {
            for (FlowElement flowElement : flowElements) {
                // 类型为用户节点
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    source = (UserTask) flowElement;
                }
            }
        }
        // 获取节点的所有路线
        List<List<UserTask>> roads = FlowableUtils.findRoad(source, null, null, null);
        // 可回退的节点列表
        List<UserTask> userTaskList = new ArrayList<>();
        for (List<UserTask> road : roads) {
            if (userTaskList.isEmpty()) {
                // 还没有可回退节点直接添加
                userTaskList = road;
            } else {
                // 如果已有回退节点，则比对取交集部分
                userTaskList.retainAll(road);
            }
        }
        return userTaskList;
    }

    /**
     * 删除任务
     *
     * @param flowTaskVo 请求实体参数
     */
    @Override
    public void deleteTask(FlowTaskVo flowTaskVo) {
        taskService.deleteTask(flowTaskVo.getTaskId(), flowTaskVo.getComment());
    }

    /**
     * 认领/签收任务
     *
     * @param flowTaskVo 请求实体参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void claim(FlowTaskVo flowTaskVo) {
        taskService.claim(flowTaskVo.getTaskId(), flowTaskVo.getUserId());
    }

    /**
     * 取消认领/签收任务
     *
     * @param flowTaskVo 请求实体参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unClaim(FlowTaskVo flowTaskVo) {
        taskService.unclaim(flowTaskVo.getTaskId());
    }

    /**
     * 委派任务
     *
     * @param flowTaskVo 请求实体参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delegateTask(FlowTaskVo flowTaskVo) {
        taskService.delegateTask(flowTaskVo.getTaskId(), flowTaskVo.getAssignee());
    }


    /**
     * 转办任务
     *
     * @param flowTaskVo 请求实体参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignTask(FlowTaskVo flowTaskVo) {
        if(ObjectUtils.allNull(flowTaskVo.getValues().get(ProcessConstants.PROCESS_APPROVAL))){
            throw new CheckedException("未指定转办人");
        }
        if(String.valueOf(flowTaskVo.getValues().get(ProcessConstants.PROCESS_APPROVAL)).contains(",")){
            throw new CheckedException("只能转办给一人");
        }
        //添加审批意见
        taskService.addComment(flowTaskVo.getTaskId(), flowTaskVo.getInstanceId(), FlowComment.NORMAL.getType(), flowTaskVo.getComment());
        taskService.setAssignee(flowTaskVo.getTaskId(),String.valueOf(flowTaskVo.getValues().get(ProcessConstants.PROCESS_APPROVAL)));
        //更新全局变量
        taskService.setVariables(flowTaskVo.getTaskId(),flowTaskVo.getVariables());
    }

    /**
     * 我发起的流程
     *
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @param flowTaskDto 参数
     */
    @Override
    public Map<String, Object> myProcess(Integer pageNum, Integer pageSize,FlowTaskDto flowTaskDto) {
        Long userId = SecurityUtils.getLoginUser().getSysUser().getUserId();
        Map<String, Object> params = flowTaskDto.getParams();
        if(!SecurityUtils.isAdmin(userId)){
            params.put("assignee",userId);
        }
        params.put("pageNum",pageSize * (pageNum - 1));
        params.put("pageSize",pageSize);
        flowTaskDto.setParams(params);
        Integer count = flowHistericTaskMapper.selectFlowHistoricProcessInstanceCount(flowTaskDto);
        List<HistoricProcessInstanceDTO> historicProcessInstances = flowHistericTaskMapper.selectFlowHistoricProcessInstance(flowTaskDto);
        List<FlowTaskDto> flowList = new ArrayList<>();
        for (HistoricProcessInstanceDTO hisIns : historicProcessInstances) {
            FlowTaskDto flowTask = new FlowTaskDto();
            flowTask.setCreateTime(hisIns.getStartTime());
            flowTask.setFinishTime(hisIns.getEndTime());
            flowTask.setProcInsId(hisIns.getProcInsId());
            flowTask.setProcessTitle(hisIns.getProcessTitle());
            flowTask.setFormId(hisIns.getFormId());
            // 计算耗时
            long time;
            if (Objects.nonNull(hisIns.getEndTime())) {
                time = hisIns.getEndTime().getTime() - hisIns.getStartTime().getTime();
            } else {
                time = System.currentTimeMillis() - hisIns.getStartTime().getTime();
            }
            flowTask.setDuration(DateUtils.getProcessCompletionTime(time));
            // 流程定义信息
            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(hisIns.getProcDefId())
                    .singleResult();
            flowTask.setDeployId(pd.getDeploymentId());
            flowTask.setProcDefName(pd.getName());
            flowTask.setProcDefVersion(pd.getVersion());
            flowTask.setCategory(pd.getCategory());
            flowTask.setProcDefVersion(pd.getVersion());
            // 当前所处流程
            R<SysUser> startUser = remoteuserservice.selectUserInFoById(hisIns.getStartUserId(), SecurityConstants.INNER);
            if(startUser.getCode() == HttpStatus.ERROR){
                throw new CheckedException("获取用户信息失败");
            }
            flowTask.setStartUserId(String.valueOf(startUser.getData().getUserId()));
            flowTask.setStartUserName(startUser.getData().getNickName());
            flowTask.setStartDeptName(startUser.getData().getDept().getDeptName());
            List<Task> taskList = taskService.createTaskQuery().processInstanceId(hisIns.getProcInsId()).orderByTaskCreateTime().desc().list();
            if (CollectionUtils.isNotEmpty(taskList)) {
                Task currtask = taskList.get(0);
                flowTask.setTaskId(currtask.getId());
                flowTask.setTaskName(currtask.getName());
                if(currtask.getAssignee() !=null){
                    R<SysUser> assigneeName = remoteuserservice.selectUserInFoById(Long.parseLong(currtask.getAssignee()), SecurityConstants.INNER);
                    if(assigneeName.getCode() == HttpStatus.ERROR){
                        throw new CheckedException("获取用户信息失败");
                    }
                    flowTask.setAssigneeName(assigneeName.getData().getNickName());
                }

            }else{
                List<HistoricTaskInstance> historicTaskInstance = historyService.createHistoricTaskInstanceQuery().
                        processInstanceId(hisIns.getProcInsId()).orderByHistoricTaskInstanceEndTime().desc().list();
                if(CollectionUtils.isNotEmpty(historicTaskInstance)){
                    HistoricTaskInstance histask = historicTaskInstance.get(0);
                    flowTask.setTaskId(histask.getId());
                    flowTask.setTaskName(histask.getName());
                    if(histask.getAssignee()!=null){
                        R<SysUser> assigneeName = remoteuserservice.selectUserInFoById(Long.parseLong(histask.getAssignee()), SecurityConstants.INNER);
                        if(assigneeName.getCode() == HttpStatus.ERROR){
                            throw new CheckedException("获取用户信息失败");
                        }
                        flowTask.setAssigneeName(assigneeName.getData().getNickName());
                    }
                }
            }
            flowList.add(flowTask);
        }
        Map<String, Object> re=new HashMap<>(2);
        re.put("data",flowList);
        re.put("total",count);
        return re;
    }

    /**
     * 取消申请
     * 目前实现方式: 直接将当前流程变更为已完成
     * @param flowTaskVo 参数
     */
    @Override
    public boolean stopProcess(FlowTaskVo flowTaskVo) {
        List<Task> task = taskService.createTaskQuery().processInstanceId(flowTaskVo.getInstanceId()).list();
        if (CollectionUtils.isEmpty(task)) {
            throw new CheckedException("流程未启动或已执行完成，取消申请失败");
        }
        // 获取当前需撤回的流程实例
        ProcessInstance processInstance =
                runtimeService.createProcessInstanceQuery()
                        .processInstanceId(flowTaskVo.getInstanceId())
                        .singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        if (Objects.nonNull(bpmnModel)) {
            Process process = bpmnModel.getMainProcess();
            List<EndEvent> endNodes = process.findFlowElementsOfType(EndEvent.class, false);
            if (CollectionUtils.isNotEmpty(endNodes)) {
                SysUser loginUser = SecurityUtils.getLoginUser().getSysUser();
                Authentication.setAuthenticatedUserId(loginUser.getUserId().toString());
                // 获取当前流程最后一个节点
                String endId = endNodes.get(0).getId();
                List<Execution> executions =
                        runtimeService.createExecutionQuery().parentId(processInstance.getProcessInstanceId()).list();
                List<String> executionIds = new ArrayList<>();
                executions.forEach(execution -> executionIds.add(execution.getId()));
                // 变更流程为已结束状态
                runtimeService.createChangeActivityStateBuilder()
                        .moveExecutionsToSingleActivityId(executionIds, endId).changeState();
            }
        }

        return true;
    }

    /**
     * 撤回任务到上一步
     * @param flowTaskVo 参数
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean revokeProcess(FlowTaskVo flowTaskVo) {
        String processInstanceId=flowTaskVo.getInstanceId();
        // 获取当前流程实例的任务列表
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .orderByTaskCreateTime()
                .desc()
                .list();

        if (tasks.size() < 2) {
            throw new CheckedException("无法撤回到上一步");
        }

        // 获取上一步任务
        Task previousTask = tasks.get(1);
        String previousTaskDefinitionKey = previousTask.getTaskDefinitionKey();

        // 获取上一步任务的候选人员和候选组
        List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(previousTask.getId());
        List<String> candidateUsers = new ArrayList<>();
        List<String> candidateGroups = new ArrayList<>();

        for (IdentityLink identityLink : identityLinks) {
            if (identityLink.getUserId() != null) {
                candidateUsers.add(identityLink.getUserId());
            }
            if (identityLink.getGroupId() != null) {
                candidateGroups.add(identityLink.getGroupId());
            }
        }

        // 检查当前用户是否属于候选人员或候选组
        String currentUser = SecurityUtils.getUsername();
        if (!candidateUsers.contains(currentUser) && !isUserInAnyGroup(currentUser, candidateGroups)) {
            throw new CheckedException("您无权撤回到上一步");
        }

        // 执行撤回操作
        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(processInstanceId)
                .moveActivityIdTo(previousTask.getTaskDefinitionKey(), previousTaskDefinitionKey)
                .changeState();
        return true;
    }

    // 检查用户是否属于任何候选组
    private boolean isUserInAnyGroup(String user, List<String> candidateGroups) {
        List<Group> groups = identityService.createGroupQuery().groupMember(user).list();
        for (Group group : groups) {
            if (candidateGroups.contains(group.getId())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 待办任务列表
     *
     * @param pageNum  当前页码
     * @param pageSize 每页条数
     * @param flowtaskdto 参数
     */
    @Override
    public Map<String, Object> todoList(Integer pageNum, Integer pageSize,FlowTaskDto flowtaskdto) {
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        List<String> roleList = new ArrayList<>();
        TaskQuery taskQuery = taskService.createTaskQuery();

        taskQuery
                .active()
                .includeProcessVariables();
        if(StringUtils.isNotBlank(flowtaskdto.getProcDefName())){
            taskQuery.processDefinitionNameLike(flowtaskdto.getProcDefName());
        }
        if(ObjectUtils.isNotEmpty(flowtaskdto.getParams().get("beginTime"))){
            taskQuery.taskCreatedAfter(DateUtils.parseDate(flowtaskdto.getParams().get("beginTime")));
        }
        if(ObjectUtils.isNotEmpty(flowtaskdto.getParams().get("endTime"))){
            taskQuery.taskCreatedBefore(DateUtils.parseDate(flowtaskdto.getParams().get("endTime")));
        }
        taskQuery.or().taskAssignee(String.valueOf(sysUser.getUserId()))
                .taskCandidateOrAssigned(String.valueOf(sysUser.getUserId()));
        if(!sysUser.getRoles().isEmpty()){
            sysUser.getRoles().forEach(f->
                    roleList.add(String.valueOf(f.getRoleId()))
            );
            taskQuery.taskCandidateGroupIn(roleList).endOr();
        }else {
            taskQuery.endOr();
        }
        List<Task> taskList = taskQuery.orderByTaskCreateTime().desc().listPage(pageSize * (pageNum - 1), pageSize);
        Map<String, Object> re=new HashMap<>(2);
        re.put("data",todoListIntegration(taskList));
        re.put("total",(int) taskQuery.count());
        return re;
    }

    /**
     *  待办任务列表整合
     */
    private List<FlowTaskDto> todoListIntegration(List<Task> taskList){
        List<FlowTaskDto> flowList = new ArrayList<>();
        for (Task task : Objects.requireNonNull(taskList)) {
            FlowTaskDto flowTask = new FlowTaskDto();
            // 当前流程信息
            flowTask.setTaskId(task.getId());
            flowTask.setTaskDefKey(task.getTaskDefinitionKey());
            flowTask.setCreateTime(task.getCreateTime());
            flowTask.setProcDefId(task.getProcessDefinitionId());
            flowTask.setExecutionId(task.getExecutionId());
            flowTask.setTaskName(task.getName());
            //获取流程标题
            SysProcessTitle pt = flowDeployMapper.selectSysProcessTitle(task.getProcessInstanceId());
            if(pt!=null){
                flowTask.setProcessTitle(pt.getProcessTitle());
            }
            // 流程定义信息
            FlowProcDefDto pd = flowDeployMapper.selectActReProcDef(task.getProcessDefinitionId());
            flowTask.setDeployId(pd.getDeploymentId());
            flowTask.setProcDefName(pd.getName());
            flowTask.setProcDefVersion(pd.getVersion());
            flowTask.setProcInsId(task.getProcessInstanceId());
            flowTask.setFormId(pd.getFormId());
            // 流程发起人信息
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult();
            R<SysUser> startUser = remoteuserservice.selectUserInFoById(Long.parseLong(historicProcessInstance.getStartUserId()), SecurityConstants.INNER);
            if(startUser.getCode() == HttpStatus.ERROR){
                throw new CheckedException("获取用户信息失败:"+startUser.getMsg());
            }
            flowTask.setStartUserId(String.valueOf(startUser.getData().getUserId()));
            flowTask.setStartUserName(startUser.getData().getNickName());
            flowTask.setStartDeptName(startUser.getData().getDept().getDeptName());
            flowList.add(flowTask);
        }
        return flowList;
    }

    /**
     * 已办任务列表
     *
     * @param pageNum  当前页码
     * @param pageSize 每页条数
     * @param flowTaskDto 参数
     */
    @Override
    public Map<String, Object> finishedList(Integer pageNum, Integer pageSize,FlowTaskDto flowTaskDto) {
        Map<String, Object> params = flowTaskDto.getParams();
        params.put("assignee",SecurityUtils.getLoginUser().getSysUser().getUserId());
        params.put("pageNum",pageSize * (pageNum - 1));
        params.put("pageSize",pageSize);
        flowTaskDto.setParams(params);

        List<HistoricTaskInstanceDTO> historicTaskInstanceList = flowHistericTaskMapper.selectFlowHistericTaskInstance(flowTaskDto);
        Integer hcount = flowHistericTaskMapper.selectFlowHistericTaskInstanceCount(flowTaskDto);
        List<FlowTaskDto> hisTaskList = Lists.newArrayList();
        for (HistoricTaskInstanceDTO inst : historicTaskInstanceList) {
            AtomicReference<FlowTaskDto> flowTasks = new AtomicReference<>(new FlowTaskDto());
            flowTasks.get().setTaskId(inst.getIdRev());
            flowTasks.get().setCreateTime(inst.getStartTime());
            flowTasks.get().setFinishTime(inst.getEndTime());
            flowTasks.get().setDuration(DateUtils.getProcessCompletionTime(inst.getDuration()));
            flowTasks.get().setProcDefId(inst.getProcDefId());
            flowTasks.get().setTaskDefKey(inst.getTaskDefKey());
            flowTasks.get().setTaskName(inst.getNname());
            flowTasks.get().setExecutionId(inst.getExecutionId());
            flowTasks.get().setProcessTitle(inst.getProcessTitle());
            flowTasks.get().setStartDeptName(inst.getStartDeptName());
            flowTasks.get().setStartUserId(inst.getStartUserId());
            flowTasks.get().setStartUserName(inst.getStartUserName());

            // 流程定义信息
            FlowProcDefDto pd = flowDeployMapper.selectActReProcDef(inst.getProcDefId());
            flowTasks.get().setDeployId(pd.getDeploymentId());
            flowTasks.get().setProcDefName(pd.getName());
            flowTasks.get().setProcDefVersion(pd.getVersion());
            flowTasks.get().setProcInsId(inst.getProcInstId());
            flowTasks.get().setHisProcInsId(inst.getProcInstId());
            flowTasks.get().setFormId(pd.getFormId());
            hisTaskList.add(flowTasks.get());
        }
        Map<String, Object> re=new HashMap<>(2);
        re.put("data",hisTaskList);
        re.put("total",hcount);
        return re;
    }

    /**
     * 抄送列表
     *
     * @param pageNum     当前页码
     * @param pageSize    每页条数
     * @param flowTaskDto 参数
     */
    @Override
    public Map<String, Object> ccList(Integer pageNum, Integer pageSize, FlowTaskDto flowTaskDto) {
        Map<String, Object> params = flowTaskDto.getParams();
        params.put("userId",SecurityUtils.getLoginUser().getSysUser().getUserId());
        params.put("pageNum",pageSize * (pageNum - 1));
        params.put("pageSize",pageSize);
        flowTaskDto.setParams(params);
        List<CourtesyCopyDTO> copyDTOList = sysTaskCcMapper.selectSysTaskCcList(flowTaskDto);
        List<FlowTaskDto> ccList = Lists.newArrayList();
        if(!copyDTOList.isEmpty()){
            for (CourtesyCopyDTO inst : copyDTOList) {
                AtomicReference<FlowTaskDto> flowTasks = new AtomicReference<>(new FlowTaskDto());
                flowTasks.get().setTaskId(inst.getTaskId());
                flowTasks.get().setCreateTime(inst.getStartTime());
                flowTasks.get().setProcDefId(inst.getProcDefId());
                flowTasks.get().setTaskName(inst.getNname());
                flowTasks.get().setExecutionId(inst.getExecutionId());
                flowTasks.get().setProcessTitle(inst.getProcessTitle());
                flowTasks.get().setStartDeptName(inst.getStartDeptName());
                flowTasks.get().setStartUserId(inst.getStartUserId());
                flowTasks.get().setStartUserName(inst.getStartUserName());
                // 流程定义信息
                FlowProcDefDto pd = flowDeployMapper.selectActReProcDef(inst.getProcDefId());
                flowTasks.get().setDeployId(pd.getDeploymentId());
                flowTasks.get().setProcDefName(pd.getName());
                flowTasks.get().setProcDefVersion(pd.getVersion());
                flowTasks.get().setProcInsId(inst.getProcInstId());
                flowTasks.get().setFormId(pd.getFormId());
                ccList.add(flowTasks.get());
            }
        }

        Map<String, Object> re=new HashMap<>(2);
        re.put("data",ccList);
        re.put("total",copyDTOList.size());
        return re;
    }

    /**
     * 流程历史流转记录
     *
     * @param procInsId 流程实例ID
     * @param deployId 部署id
     */
    @Override
    public Map<String, Object> flowRecord(String procInsId, String deployId) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(procInsId)) {
            List<HistoricActivityInstance> list = historyService
                    .createHistoricActivityInstanceQuery()
                    .processInstanceId(procInsId)
                    .orderByHistoricActivityInstanceStartTime()
                    .desc().list();
            List<FlowTaskDto> hisFlowList = new ArrayList<>();
            for (HistoricActivityInstance histIns : list) {
                if (StringUtils.isNotBlank(histIns.getTaskId())) {
                    FlowTaskDto flowTask = new FlowTaskDto();
                    flowTask.setTaskId(histIns.getTaskId());
                    flowTask.setTaskName(histIns.getActivityName());
                    flowTask.setCreateTime(histIns.getStartTime());
                    flowTask.setFinishTime(histIns.getEndTime());
                    if (StringUtils.isNotBlank(histIns.getAssignee())) {
                        SysUser sysUser = remoteuserservice.selectUserInFoById(Long.parseLong(histIns.getAssignee()), SecurityConstants.INNER).getData();
                        flowTask.setAssigneeId(sysUser.getUserId());
                        flowTask.setAssigneeName(sysUser.getNickName());
                        if(sysUser.getDept()!=null){
                            flowTask.setDeptName(sysUser.getDept().getDeptName());
                        }
                    }
                    // 展示审批人员
                    List<HistoricIdentityLink> linksForTask = historyService.getHistoricIdentityLinksForTask(histIns.getTaskId());
                    StringBuilder stringBuilder = new StringBuilder();
                    for (HistoricIdentityLink identityLink : linksForTask) {
                        // 获选人,候选组/角色(多个)
                        if ("candidate".equals(identityLink.getType())) {
                            if (StringUtils.isNotBlank(identityLink.getUserId())) {
                                R<SysUser> sysUserR = remoteuserservice.selectUserInFoById(Long.parseLong(identityLink.getUserId()), SecurityConstants.INNER);
                                if(sysUserR.getCode() == HttpStatus.ERROR){
                                    throw new CheckedException("获取用户信息失败");
                                }
                                stringBuilder.append(sysUserR.getData().getNickName()).append(",");
                            }
                            if (StringUtils.isNotBlank(identityLink.getGroupId())) {
                                R<SysRole> sysRoleR = remoteuserservice.selectRoleById(Long.parseLong(identityLink.getGroupId()), SecurityConstants.INNER);
                                if(sysRoleR.getCode() == HttpStatus.ERROR){
                                    throw new CheckedException("获取角色信息失败");
                                }
                                stringBuilder.append(sysRoleR.getData().getRoleName()).append(",");
                            }
                        }
                    }
                    if (StringUtils.isNotBlank(stringBuilder)) {
                        flowTask.setCandidate(stringBuilder.substring(0, stringBuilder.length() - 1));
                    }

                    flowTask.setDuration(histIns.getDurationInMillis() == null || histIns.getDurationInMillis() == 0 ? null : DateUtils.getProcessCompletionTime(histIns.getDurationInMillis()));
                    // 获取意见评论内容
                    List<Comment> commentList = taskService.getProcessInstanceComments(histIns.getProcessInstanceId());
                    StringBuilder stl=new StringBuilder();
                    commentList.forEach(comment -> {
                        if (histIns.getTaskId().equals(comment.getTaskId())) {
                            if(comment.getFullMessage() !=null){
                                stl.append(comment.getFullMessage()).append("; ");
                            }
                        }
                        flowTask.setComment(FlowCommentDto.builder().type(comment.getType()).comment(String.valueOf(stl)).build());
                    });
                    hisFlowList.add(flowTask);
                }
            }
            map.put("flowList", hisFlowList);
        }
        // 如果是第一次流程发起，获取初始化配置的表单
        if(StringUtils.isNotBlank(deployId)) {
            SysForm sysForm = sysInstanceFormService.selectSysDeployFormByDeployId(deployId);
            if (Objects.isNull(sysForm)) {
                throw new CheckedException("请先配置流程发起默认需要的流程表单");
            }
            map.put("formData", JSONObject.parseObject(sysForm.getFormContent()));
        }
        return map;
    }


    /**
     * 获取流程过程图
     *
     * @param processId 参数
     */
    @Override
    public InputStream diagram(String processId) {
        String processDefinitionId;
        // 获取当前的流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        // 如果流程已经结束，则得到结束节点
        if (Objects.isNull(processInstance)) {
            HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().processInstanceId(processId).singleResult();

            processDefinitionId = pi.getProcessDefinitionId();
        } else {// 如果流程没有结束，则取当前活动节点
            // 根据流程实例ID获得当前处于活动状态的ActivityId合集
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
            processDefinitionId = pi.getProcessDefinitionId();
        }

        // 获得活动的节点
        List<HistoricActivityInstance> highLightedFlowList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processId).orderByHistoricActivityInstanceStartTime().asc().list();

        List<String> highLightedFlows = new ArrayList<>();
        List<String> highLightedNodes = new ArrayList<>();
        //高亮线
        for (HistoricActivityInstance tempActivity : highLightedFlowList) {
            if ("sequenceFlow".equals(tempActivity.getActivityType())) {
                //高亮线
                highLightedFlows.add(tempActivity.getActivityId());
            } else {
                //高亮节点
                highLightedNodes.add(tempActivity.getActivityId());
            }
        }

        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        ProcessEngineConfiguration configuration = processEngine.getProcessEngineConfiguration();
        //获取自定义图片生成器
        ProcessDiagramGenerator diagramGenerator = new CustomProcessDiagramGenerator();
        return diagramGenerator.generateDiagram(bpmnModel, "png", highLightedNodes, highLightedFlows, configuration.getActivityFontName(),
                configuration.getLabelFontName(), configuration.getAnnotationFontName(), configuration.getClassLoader(), 1.0, true);

    }

    /**
     * 获取流程执行过程
     *
     * @param procInsId 流程实例id
     */
    @Override
    public List<FlowViewerDto> getFlowViewer(String procInsId, String executionId) {
        List<FlowViewerDto> flowViewerList = new ArrayList<>();
        FlowViewerDto flowViewerDto;
        // 获取任务开始节点(临时处理方式)
        List<HistoricActivityInstance> startNodeList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInsId)
                .orderByHistoricActivityInstanceStartTime()
                .asc().listPage(0,100);
        for (HistoricActivityInstance startInstance : startNodeList) {
            if (!"sequenceFlow".equals(startInstance.getActivityType())) {
                flowViewerDto = new FlowViewerDto();
                if (!"sequenceFlow".equals(startInstance.getActivityType())) {
                    flowViewerDto.setKey(startInstance.getActivityId());
                    // 根据流程节点处理时间校验该节点是否已完成
                    flowViewerDto.setCompleted(!Objects.isNull(startInstance.getEndTime()));
                    flowViewerList.add(flowViewerDto);
                }
            }
        }
        // 历史节点
        List<HistoricActivityInstance> hisActIns = historyService.createHistoricActivityInstanceQuery()
                .executionId(executionId)
                .orderByHistoricActivityInstanceStartTime()
                .asc().list();
        for (HistoricActivityInstance activityInstance : hisActIns) {
            if (!"sequenceFlow".equals(activityInstance.getActivityType())) {
                flowViewerDto = new FlowViewerDto();
                flowViewerDto.setKey(activityInstance.getActivityId());
                // 根据流程节点处理时间校验该节点是否已完成
                flowViewerDto.setCompleted(!Objects.isNull(activityInstance.getEndTime()));
                flowViewerList.add(flowViewerDto);
            }
        }
        return flowViewerList;
    }

    /**
     * 获取流程变量
     *
     * @param taskId 任务ID
     */
    @Override
    public Map<String, Object> processVariables(String taskId) {
        // 流程变量
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().includeProcessVariables().finished().taskId(taskId).singleResult();
        if (Objects.nonNull(historicTaskInstance)) {
            return historicTaskInstance.getProcessVariables();
        } else {
            return taskService.getVariables(taskId);
        }
    }

    /**
     * 获取下一节点
     *
     * @param flowTaskVo 任务
     * @return
     */
    @Override
    public FlowNextDto getNextFlowNodeByStart(FlowTaskVo flowTaskVo) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(flowTaskVo.getDeploymentId()).singleResult();
        // Step 1. 获取当前节点并找到下一步节点
        FlowNextDto flowNextDto = new FlowNextDto();
        // Step 2. 获取当前流程所有流程变量(网关节点时需要校验表达式)
        List<UserTask> nextUserTask = FindNextNodeUtil.getNextUserTasksByStart(repositoryService, processDefinition, flowTaskVo.getVariables());
        if (CollectionUtils.isNotEmpty(nextUserTask)) {
            for (UserTask userTask : nextUserTask) {
                MultiInstanceLoopCharacteristics multiInstance = userTask.getLoopCharacteristics();
                // 会签节点
                if (Objects.nonNull(multiInstance)) {
                    flowNextDto.setVars(multiInstance.getInputDataItem());
                    flowNextDto.setType(ProcessConstants.PROCESS_MULTI_INSTANCE);
                    flowNextDto.setDataType(ProcessConstants.DATA_TYPE);
                } else {
                    // 读取自定义节点属性 判断是否是否需要动态指定任务接收人员、组
                    String dataType = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.PROCESS_CUSTOM_DATA_TYPE);
                    String userType = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.PROCESS_CUSTOM_USER_TYPE);
                    flowNextDto.setVars(ProcessConstants.PROCESS_APPROVAL);
                    flowNextDto.setType(userType);
                    flowNextDto.setDataType(dataType);
                }
            }
        }
        return flowNextDto;
    }

    /**
     * 获取下一节点
     *
     * @param flowTaskVo 任务
     */
    @Override
    public FlowNextDto getNextFlowNode(FlowTaskVo flowTaskVo) {
        // Step 1. 获取当前节点并找到下一步节点
        Task task = taskService.createTaskQuery().taskId(flowTaskVo.getTaskId()).singleResult();
        FlowNextDto flowNextDto = new FlowNextDto();
        if (Objects.nonNull(task)) {
            // Step 2. 获取当前流程所有流程变量(网关节点时需要校验表达式)
            Map<String, Object> variables = taskService.getVariables(task.getId());
            List<UserTask> nextUserTask = FindNextNodeUtil.getNextUserTasks(repositoryService, task, variables);
            if (CollectionUtils.isNotEmpty(nextUserTask)) {
                for (UserTask userTask : nextUserTask) {
                    MultiInstanceLoopCharacteristics multiInstance = userTask.getLoopCharacteristics();
                    // 会签节点
                    if (Objects.nonNull(multiInstance)) {
                        List<SysUser> list = remoteuserservice.selectUserList(new SysUser(), SecurityConstants.INNER).getData();

                        flowNextDto.setVars(ProcessConstants.PROCESS_MULTI_INSTANCE_USER);
                        flowNextDto.setType(ProcessConstants.PROCESS_MULTI_INSTANCE);
                        flowNextDto.setUserList(list);
                    } else {

                        // 读取自定义节点属性 判断是否是否需要动态指定任务接收人员、组
                        String dataType = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.PROCESS_CUSTOM_DATA_TYPE);
                        String userType = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.PROCESS_CUSTOM_USER_TYPE);

                        // 处理加载动态指定下一节点接收人员信息
                        if (ProcessConstants.DATA_TYPE.equals(dataType)) {
                            // 指定单个人员
                            if (ProcessConstants.USER_TYPE_ASSIGNEE.equals(userType)) {
                                List<SysUser> list = remoteuserservice.selectUserList(new SysUser(), SecurityConstants.INNER).getData();
                                flowNextDto.setVars(ProcessConstants.PROCESS_APPROVAL);
                                flowNextDto.setType(ProcessConstants.USER_TYPE_ASSIGNEE);
                                flowNextDto.setUserList(list);
                            }
                            // 候选人员(多个)
                            if (ProcessConstants.USER_TYPE_USERS.equals(userType)) {
                                List<SysUser> list = remoteuserservice.selectUserList(new SysUser(), SecurityConstants.INNER).getData();

                                flowNextDto.setVars(ProcessConstants.PROCESS_APPROVAL);
                                flowNextDto.setType(ProcessConstants.USER_TYPE_USERS);
                                flowNextDto.setUserList(list);
                            }
                            // 候选组
                            if (ProcessConstants.USER_TYPE_ROUPS.equals(userType)) {
                                List<SysRole> sysRoles = remoteuserservice.selectRoleAll(new SysRole(), SecurityConstants.INNER).getData();

                                flowNextDto.setVars(ProcessConstants.PROCESS_APPROVAL);
                                flowNextDto.setType(ProcessConstants.USER_TYPE_ROUPS);
                                flowNextDto.setRoleList(sysRoles);
                            }
                        } else {
                            flowNextDto.setType(ProcessConstants.FIXED);
                        }
                    }
                }
            }
        }
        return flowNextDto;
    }

}