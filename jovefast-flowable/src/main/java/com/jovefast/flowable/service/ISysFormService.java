package com.jovefast.flowable.service;


import com.jovefast.flowable.domain.SysForm;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 表单
 * @author Acechengui
 */
public interface ISysFormService 
{
    /**
     * 上传附件
     * @param file 文件
     */
    public String uploadfiles(MultipartFile file);
    /**
     * 查询流程表单
     * 
     * @param formId 流程表单ID
     * @return 流程表单
     */
    public SysForm selectSysFormById(Long formId);

    /**
     * 查询流程表单列表
     * 
     * @param sysForm 流程表单
     * @return 流程表单集合
     */
    public List<SysForm> selectSysFormList(SysForm sysForm);

    /**
     * 新增流程表单
     * 
     * @param sysForm 流程表单
     * @return 结果
     */
    public int insertSysForm(SysForm sysForm);

    /**
     * 修改流程表单
     * 
     * @param sysForm 流程表单
     * @return 结果
     */
    public int updateSysForm(SysForm sysForm);

    /**
     * 批量删除流程表单
     * 
     * @param formIds 需要删除的流程表单ID
     * @return 结果
     */
    public int deleteSysFormByIds(Long[] formIds);

    /**
     * 删除流程表单信息
     * 
     * @param formId 流程表单ID
     * @return 结果
     */
    public int deleteSysFormById(Long formId);
}
