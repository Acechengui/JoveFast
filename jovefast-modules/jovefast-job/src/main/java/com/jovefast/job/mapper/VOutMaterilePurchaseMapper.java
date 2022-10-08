package com.jovefast.job.mapper;

import com.jovefast.job.domain.VRptWarehouse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

public interface VOutMaterilePurchaseMapper {

    /**
     * 获取物料入库数据
     * @param parms
     * @return
     */
    List<VRptWarehouse> selectVRptWarehouseIn(VRptWarehouse parms);

    /**
     * 获取物料出库数据
     * @param parms
     * @return
     */
    List<VRptWarehouse> selectVRptWarehouseOut(VRptWarehouse parms);

    /**
     * 查询任务最后运行时间（可以用来防止重复推送）
     * @param parms
     * @return
     */
    String selectPlanjobsTaskLastRunDate(Map<String,Object> parms);

    /**
     * 添加任务运行记录
     * @param parms
     * @return
     */
    Integer insertPlanjobsTaskRunLog(Map<String,Object> parms);

}
