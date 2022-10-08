package com.jovefast.job.service.impl;

import com.jovefast.common.datasource.annotation.Slave;
import com.jovefast.job.domain.dto.OutboundRecordDTO;
import com.jovefast.job.domain.dto.ProductionProcessOverDTO;
import com.jovefast.job.mapper.PurchaseMapper;
import com.jovefast.job.service.IErpPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description Description
 * @Author Acechengui
 * @Date Created in 2022/8/2
 */
@Service
@Slave
public class ErpPurchaseServiceImpl implements IErpPurchaseService {

    @Autowired
    private PurchaseMapper purchaseMapper;

    /**
     * 搜索生产工序过数记录
     *
     * @param fromTime   开始时间
     * @param arriveTime 截至时间
     * @return list集合
     */
    @Override
    public List<ProductionProcessOverDTO> searchProductionProcessOverList(String fromTime, String arriveTime) {
        return purchaseMapper.selectProductionProcessOver(fromTime, arriveTime);
    }

    /**
     * 搜索出库记录
     *
     * @param fromTime   开始时间
     * @param arriveTime 截至时间
     * @return list集合
     */
    @Override
    public List<OutboundRecordDTO> searchOutboundRecordList(String fromTime, String arriveTime) {
        return purchaseMapper.selectOutboundRecord(fromTime, arriveTime);
    }
}
