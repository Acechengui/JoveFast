package com.jovefast.job.service;

import com.jovefast.job.domain.dto.VfinishedgoodsInventoryStatDTO;

import java.util.List;

/**
 * 超时停留业务接口
 */
public interface ITimeControllTempService {

    /**
     * 预警扫描
     */
    public void  scanningEarlyWarningTimeControllTemp();

    /**
     * 成品库存超周期预警
     */
    public List<VfinishedgoodsInventoryStatDTO> finishedGoodInventoryOverCycleAlert(String custCode, Integer days,Integer defaultDays);

    /**
     * 报警扫描
     */
    public void scanningCallPoliceTimeControllTemp();


}
