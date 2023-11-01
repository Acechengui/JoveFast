package com.jovefast.flowable.mapper;

import com.jovefast.flowable.domain.SysTaskForm;

import java.util.List;

/**
 * 流程任务关联单Mapper接口
 */
public interface SysTaskFormMapper 
{
    /**
     * 查询流程任务关联单
     * 
     * @param id 流程任务关联单ID
     * @return 流程任务关联单
     */
    SysTaskForm selectSysTaskFormById(Long id);

    /**
     * 查询流程任务关联单列表
     * 
     * @param sysTaskForm 流程任务关联单
     * @return 流程任务关联单集合
     */
    List<SysTaskForm> selectSysTaskFormList(SysTaskForm sysTaskForm);

    /**
     * 新增流程任务关联单
     * 
     * @param sysTaskForm 流程任务关联单
     * @return 结果
     */
    int insertSysTaskForm(SysTaskForm sysTaskForm);

    /**
     * 修改流程任务关联单
     * 
     * @param sysTaskForm 流程任务关联单
     * @return 结果
     */
    int updateSysTaskForm(SysTaskForm sysTaskForm);

    /**
     * 删除流程任务关联单
     * 
     * @param id 流程任务关联单ID
     * @return 结果
     */
    int deleteSysTaskFormById(Long id);

    /**
     * 批量删除流程任务关联单
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteSysTaskFormByIds(Long[] ids);

}