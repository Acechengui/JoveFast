package com.jovefast.job.service;

public interface IPackRelatedService {

    /**
     * 将发货指令为【已创建】的记录推送给指定人员
     */
    void sendDeliveryInstruction();

}
