package com.jovefast.job.service;

/**
 * 物料消耗情况接口类
 */
public interface IVOutMaterilePurchaseService {

    /**
     * 每天各物料消耗情况
     * @param temp_path 系统临时文件存储路径
     */
    public void materialConsumptionDetails(String temp_path);

    /**
     * 每月各物料购买情况
     * @param temp_path 系统临时文件存储路径
     */
    public void materialPurchaseDetails(String temp_path);
}
