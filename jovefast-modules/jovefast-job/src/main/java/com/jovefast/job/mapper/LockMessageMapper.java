package com.jovefast.job.mapper;

import com.jovefast.job.domain.LockMessage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

public interface LockMessageMapper {

    /**
     * 获取锁定超时数据
     * @param map
     * @return
     */
    List<LockMessage> getTheLockTimeoutDatas(Map<String, Object> map);

    /**
     * 获取锁定数据
     * @param map
     * @return
     */
    List<LockMessage> getLockMessage(Map<String, Object> map);

    /**
     * 更新推送状态
     * @param map
     * @return
     */
    Integer updateStatus(Map<String, Object> map);

}
