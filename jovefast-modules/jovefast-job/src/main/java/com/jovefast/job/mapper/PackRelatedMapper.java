package com.jovefast.job.mapper;

import com.jovefast.job.domain.dto.ShipiInstructionDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 包装相关信息推送
 */
public interface PackRelatedMapper {

    /**
     * 查询发货指令状态为 0【已创建】的数据
     * @return
     */
    public List<ShipiInstructionDTO> getShipiInstructionByStatusOnCreate();

}
