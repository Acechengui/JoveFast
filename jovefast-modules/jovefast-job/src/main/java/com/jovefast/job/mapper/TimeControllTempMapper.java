package com.jovefast.job.mapper;

import com.jovefast.job.domain.TimeControllTemp;
import com.jovefast.job.domain.dto.VfinishedgoodsInventoryStatDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 超时管控
 *
 * @author JOVE
 */
public interface TimeControllTempMapper {
    /**
     * 预警数据查询
     * 扫描Time_Controll_Temp表中enddate结束时间为NULL的数据
     */
    public List<TimeControllTemp> scanningEarlyWarningTimeControllTemp();

    /**
     * 报警数据查询
     * 扫描Time_Controll_Temp表中enddate结束时间为NULL的数据
     */
    public List<TimeControllTemp> scanningCallPoliceTimeControllTemp();


    public int lockWorkOrderNumber(TimeControllTemp param);

    public int updateTimeControll(TimeControllTemp param);


    public List<TimeControllTemp> scanningEarlyWarningTimeControllTempFourHour();

    public List<TimeControllTemp> scanningCallPoliceTimeControllTempFourHour();

    public int updateTimeControllByLastWarningTime(TimeControllTemp param);

    public List<TimeControllTemp> scanningTimeControllTempByRange(Map<String,String> map);

    int saveFirstEarlyWarningRecord(TimeControllTemp param);

    List<VfinishedgoodsInventoryStatDTO> selectVfinishedgoodsInventoryStat();
}
