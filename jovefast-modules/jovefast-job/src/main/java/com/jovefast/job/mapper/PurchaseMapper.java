package com.jovefast.job.mapper;

import com.jovefast.job.domain.dto.OutboundRecordDTO;
import com.jovefast.job.domain.dto.ProductionProcessOverDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购相关数据操作
 */
public interface PurchaseMapper {
    /**
     * 搜索生产工序过数记录
     *
     * @param fromTime   开始时间
     * @param arriveTime 截至时间
     * @return list集合
     */
    List<ProductionProcessOverDTO> selectProductionProcessOver(@Param("from") String fromTime, @Param("arrive") String arriveTime);

    /**
     * 搜索出库记录
     *
     * @param fromTime   开始时间
     * @param arriveTime 截至时间
     * @return list集合
     */
    List<OutboundRecordDTO> selectOutboundRecord(@Param("from") String fromTime, @Param("arrive") String arriveTime);
}
