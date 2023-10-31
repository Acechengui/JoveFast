package com.jovefast.flowable.service.impl;

import com.jovefast.common.core.constant.HttpStatus;
import com.jovefast.common.core.domain.R;
import com.jovefast.common.core.exception.CheckedException;
import com.jovefast.common.core.utils.DateUtils;
import com.jovefast.flowable.domain.SysForm;
import com.jovefast.flowable.mapper.SysFormMapper;
import com.jovefast.system.api.RemoteFileService;
import com.jovefast.system.api.domain.SysFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jovefast.flowable.service.ISysFormService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
}
