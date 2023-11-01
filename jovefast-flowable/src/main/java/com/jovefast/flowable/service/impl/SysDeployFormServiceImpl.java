package com.jovefast.flowable.service.impl;

import java.util.List;
import java.util.Objects;

import com.jovefast.flowable.domain.SysDeployForm;
import com.jovefast.flowable.domain.SysForm;
import com.jovefast.flowable.mapper.SysDeployFormMapper;
import com.jovefast.flowable.service.ISysDeployFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 流程实例关联表单Service业务层处理
 * 
 * @author Acechengui
 */
@Service
public class SysDeployFormServiceImpl implements ISysDeployFormService
{
    @Autowired
    private SysDeployFormMapper sysDeployFormMapper;


    /**
     * 新增流程实例关联表单
     * 
     * @param sysDeployForm 流程实例关联表单
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertSysDeployForm(SysDeployForm sysDeployForm)
    {
        SysForm sysForm = sysDeployFormMapper.selectSysDeployFormByDeployId(sysDeployForm.getDeployId());
        if (Objects.isNull(sysForm)) {
            return sysDeployFormMapper.insertSysDeployForm(sysDeployForm) > 0;
        }else {
            return sysDeployFormMapper.updateSysDeployForm(sysDeployForm) > 0;
        }
    }


    /**
     * 查询流程挂着的表单
     *
     * @param deployId 部署id
     */
    @Override
    public SysForm selectSysDeployFormByDeployId(String deployId) {
        return sysDeployFormMapper.selectSysDeployFormByDeployId(deployId);
    }

}
