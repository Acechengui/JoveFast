package com.jovefast.flowable.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.jovefast.common.core.constant.HttpStatus;
import com.jovefast.common.core.domain.R;
import com.jovefast.common.core.exception.CheckedException;
import com.jovefast.common.core.utils.DateUtils;
import com.jovefast.common.core.utils.StringUtils;
import com.jovefast.flowable.common.constant.ProcessConstants;
import com.jovefast.flowable.domain.SysForm;
import com.jovefast.flowable.domain.vo.FlowTaskVo;
import com.jovefast.flowable.mapper.SysFormMapper;
import com.jovefast.system.api.RemoteFileService;
import com.jovefast.system.api.domain.SysFile;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jovefast.flowable.service.ISysFormService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程表单Service业务层处理
 * 
 * @author Acechengui
 */
@Service
public class SysFormServiceImpl implements ISysFormService 
{
    @Autowired
    private SysFormMapper sysFormMapper;

    @Autowired
    private RemoteFileService remoteFileService;

    @Resource
    protected RepositoryService repositoryService;

    @Resource
    protected RuntimeService runtimeService;

    @Resource
    protected TaskService taskService;

    /**
     * 上传附件
     *
     * @param file 文件
     * @return 访问路径
     */
    @Override
    public String uploadfiles(MultipartFile file) {
        R<SysFile> fileR = remoteFileService.upload(file);
        if(fileR.getCode() != HttpStatus.SUCCESS){
            throw new CheckedException("文件上传异常");
        }
        return fileR.getData().getUrl();
    }

    /**
     * 查询流程表单
     * 
     * @param formId 流程表单ID
     * @return 流程表单
     */
    @Override
    public SysForm selectSysFormById(Long formId)
    {
        return sysFormMapper.selectSysFormById(formId);
    }

    /**
     * 查询流程表单列表
     * 
     * @param sysForm 流程表单
     * @return 流程表单
     */
    @Override
    public List<SysForm> selectSysFormList(SysForm sysForm)
    {
        return sysFormMapper.selectSysFormList(sysForm);
    }

    /**
     * 新增流程表单
     * 
     * @param sysForm 流程表单
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSysForm(SysForm sysForm)
    {
        sysForm.setCreateTime(DateUtils.getNowDate());
        return sysFormMapper.insertSysForm(sysForm);
    }

    /**
     * 修改流程表单
     * 
     * @param sysForm 流程表单
     * @return 结果
     */
    @Override
    public int updateSysForm(SysForm sysForm)
    {
        sysForm.setUpdateTime(DateUtils.getNowDate());
        return sysFormMapper.updateSysForm(sysForm);
    }

    /**
     * 批量删除流程表单
     * 
     * @param formIds 需要删除的流程表单ID
     * @return 结果
     */
    @Override
    public int deleteSysFormByIds(Long[] formIds)
    {
        return sysFormMapper.deleteSysFormByIds(formIds);
    }

    /**
     * 删除流程表单信息
     * 
     * @param formId 流程表单ID
     * @return 结果
     */
    @Override
    public int deleteSysFormById(Long formId)
    {
        return sysFormMapper.deleteSysFormById(formId);
    }

    /**
     * 根据流程ID和表单ID查询流程表单
     *
     * @param flowTaskVo 流程ID和表单ID等参数
     * @return 流程表单
     */
    @Override
    public SysForm selectSysFormByProcInsId(FlowTaskVo flowTaskVo) {
        // 查询基础表单
        SysForm sysForm = sysFormMapper.selectSysFormById(flowTaskVo.getFormId());
        // 根据流程实例ID获取流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(flowTaskVo.getInstanceId())
                .singleResult();
        if (processInstance != null) {
            // 获取流程定义ID
            String processDefinitionId = processInstance.getProcessDefinitionId();
            //根据流程定义ID获取BpmnModel
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
            Task task = taskService.createTaskQuery()
                    .taskId(flowTaskVo.getTaskId())
                    .singleResult();
            String taskDefinitionKey = task.getTaskDefinitionKey();
            FlowElement flowElement = bpmnModel.getFlowElement(taskDefinitionKey);
            // 根据流程ID设置表单每个字段的权限,只需处理用户任务
            if (flowElement instanceof UserTask userTask) {
                String formReadOnly = userTask.getAttributeValue(ProcessConstants.NAMASPASE, "formReadOnly");
                //值为空或者为N均表示false
                if("Y".equals(formReadOnly)){
                    //若为整个表单设置为只读，那么则无需做其他处理，直接返回即可
                    sysForm.setFormPreview(true);
                    return sysForm;
                }
                String formFieldReadOnly = userTask.getAttributeValue(ProcessConstants.NAMASPASE, "formFieldReadOnly");
                if (!StringUtils.isEmpty(formFieldReadOnly)) {
                    // 使用逗号分割字符串
                    List<String> modelList = Arrays.asList(formFieldReadOnly.split(","));
                    // 解析JSON字符串
                    JSONObject jsonObject = (JSONObject) JSON.parse(sysForm.getFormContent());
                    JSONArray listArray = jsonObject.getJSONArray("list");
                    // 匹配每个层级的model属性，并修改指定属性值
                    matchModelProperties(listArray, modelList);
                    // 将处理后的数据重新赋值给sysForm的formContent属性
                    sysForm.setFormContent(jsonObject.toJSONString());
                }
            }
        }
        return sysForm;
    }

    /**
     *  匹配每个层级的model属性，修改指定属性值
     * @param jsonArray  原始数据集
     * @param modelList  匹配数据集
     */
    private static void matchModelProperties(JSONArray jsonArray, List<String> modelList){
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String model = jsonObject.getString("model");
            if (modelList.contains(model)) {
                jsonObject.getJSONObject("options").put("disabled", true);
            }
            JSONArray subListArray = jsonObject.getJSONArray("list");
            if (subListArray != null) {
                matchModelProperties(subListArray, modelList);
            }
        }
    }
}
