package com.jovefast.job.service;

import com.jovefast.job.domain.dto.OutboundRecordDTO;
import com.jovefast.job.domain.dto.ProductionProcessOverDTO;

import java.util.List;

/**
 * ERP采购
 *
 * @author MuTouYSK
 * @date 2022年01月19日 11:44
 */
public interface IErpPurchaseService {

    /**
     * 搜索生产工序过数记录
     *
     * @param fromTime   开始时间
     * @param arriveTime 截至时间
     * @return list集合
     */
    List<ProductionProcessOverDTO> searchProductionProcessOverList(String fromTime, String arriveTime);

    /**
     * 搜索出库记录
     *
     * @param fromTime   开始时间
     * @param arriveTime 截至时间
     * @return list集合
     */
    List<OutboundRecordDTO> searchOutboundRecordList(String fromTime, String arriveTime);

}
