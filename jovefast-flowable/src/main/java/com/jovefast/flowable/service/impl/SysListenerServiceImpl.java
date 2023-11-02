package com.jovefast.flowable.service.impl;

import java.util.List;

import com.jovefast.common.core.utils.DateUtils;
import com.jovefast.flowable.domain.SysListener;
import com.jovefast.flowable.mapper.SysListenerMapper;
import com.jovefast.flowable.service.ISysListenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 流程监听Service业务层处理
 * 
 * @author Acechengui
 */
@Service
public class SysListenerServiceImpl implements ISysListenerService
{
    @Autowired
    private SysListenerMapper sysListenerMapper;

    /**
     * 查询流程监听
     * 
     * @param id 流程监听主键
     * @return 流程监听
     */
    @Override
    public SysListener selectSysListenerById(Long id)
    {
        return sysListenerMapper.selectSysListenerById(id);
    }

    /**
     * 查询流程监听列表
     * 
     * @param sysListener 流程监听
     * @return 流程监听
     */
    @Override
    public List<SysListener> selectSysListenerList(SysListener sysListener)
    {
        return sysListenerMapper.selectSysListenerList(sysListener);
    }

    /**
     * 新增流程监听
     * 
     * @param sysListener 流程监听
     * @return 结果
     */
    @Override
    public int insertSysListener(SysListener sysListener)
    {
        sysListener.setCreateTime(DateUtils.getNowDate());
        return sysListenerMapper.insertSysListener(sysListener);
    }

    /**
     * 修改流程监听
     * 
     * @param sysListener 流程监听
     * @return 结果
     */
    @Override
    public int updateSysListener(SysListener sysListener)
    {
        sysListener.setUpdateTime(DateUtils.getNowDate());
        return sysListenerMapper.updateSysListener(sysListener);
    }

    /**
     * 批量删除流程监听
     * 
     * @param ids 需要删除的流程监听主键
     * @return 结果
     */
    @Override
    public int deleteSysListenerByIds(Long[] ids)
    {
        return sysListenerMapper.deleteSysListenerByIds(ids);
    }

    /**
     * 删除流程监听信息
     * 
     * @param id 流程监听主键
     * @return 结果
     */
    @Override
    public int deleteSysListenerById(Long id)
    {
        return sysListenerMapper.deleteSysListenerById(id);
    }
}
