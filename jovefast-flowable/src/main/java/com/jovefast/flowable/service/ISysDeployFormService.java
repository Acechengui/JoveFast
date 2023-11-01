package com.jovefast.flowable.service;

import com.jovefast.flowable.domain.SysDeployForm;
import com.jovefast.flowable.domain.SysForm;

/**
 * 流程实例关联表单Service接口
 * 
 * @author Acecehgnui
 */
public interface ISysDeployFormService 
{

    /**
     * 新增流程实例关联表单
     * 
     * @param sysDeployForm 流程实例关联表单
     * @return 结果
     */
    Boolean insertSysDeployForm(SysDeployForm sysDeployForm);

    /**
     * 查询流程挂着的表单
     * @param deployId 部署ID
     */
    SysForm selectSysDeployFormByDeployId(String deployId);

}
