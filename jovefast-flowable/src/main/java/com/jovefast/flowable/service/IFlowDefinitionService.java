package com.jovefast.flowable.service;

import com.github.pagehelper.Page;
import com.jovefast.common.core.web.domain.AjaxResult;
import com.jovefast.flowable.domain.dto.FlowProcDefDto;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Acecehgnui
 */
public interface IFlowDefinitionService {

    boolean exist(String processDefinitionKey);


    /**
     * 流程定义列表
     *
     * @param name  参数
     * @return 流程定义分页列表数据
     */
    List<FlowProcDefDto> list(String name);

    /**
     * 导入流程文件
     *
     * @param name
     * @param category
     * @param in
     */
    void importFile(String name, String category, InputStream in);

    /**
     * 读取xml
     * @param deployId 部署ID
     */
    String readXml(String deployId) throws IOException;

    /**
     * 根据流程定义ID启动流程实例
     *
     * @param procDefId 实例ID
     * @param variables 流程变量
     * @return 结果
     */
    boolean startProcessInstanceById(String procDefId, Map<String, Object> variables);


    /**
     * 根据流程定义ID更新流程实例中的流程变量
     *
     * @param procDefId 实例ID
     * @param variables 流程变量
     * @return 结果
     */
    boolean editProcessInstanceById(String procDefId, Map<String, Object> variables);


    /**
     * 激活或挂起流程定义
     *
     * @param state    状态
     * @param deployId 流程部署ID
     */
    void updateState(Integer state, String deployId);


    /**
     * 删除流程定义
     *
     * @param deployId 流程部署ID act_ge_bytearray 表中 deployment_id值
     */
    void delete(String[] deployId);


    /**
     * 读取图片文件
     * @param deployId
     * @return
     */
    InputStream readImage(String deployId);
}
