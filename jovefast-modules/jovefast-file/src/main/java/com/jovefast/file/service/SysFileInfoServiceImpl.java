package com.jovefast.file.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jovefast.file.mapper.SysFileInfoMapper;
import com.jovefast.system.api.domain.SysFileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 测试案例 -分布式测试
 *
 */
@Service
public class SysFileInfoServiceImpl implements ISysFileInfoService
{
    private static final Logger log = LoggerFactory.getLogger(SysFileInfoServiceImpl.class);

    @Resource
    private SysFileInfoMapper sysFileInfoMapper;

    @DS("file")
    @Override
    public void insertFile(SysFileInfo fileInfo)
    {
        log.info("=============FILE START=================");
        log.info("=============文件输入插入=================");
        sysFileInfoMapper.insert(fileInfo);
        log.info("=============FILE END=================");
    }

}