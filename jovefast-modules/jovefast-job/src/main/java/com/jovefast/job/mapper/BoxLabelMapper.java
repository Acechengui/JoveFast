package com.jovefast.job.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jovefast.job.domain.dto.UnboundBoxLabelDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 入仓签（装箱贴）
 *
 * @author Dijkstra
 */
@DS("labelformat")
public interface BoxLabelMapper {

    /**
     * 根据指定日期和厂别查询未与货架绑定的入仓签
     *
     * @param date  日期 yyyy-MM-dd
     * @param plant 厂别 1：沙井厂；2：松岗厂；3：鹤山厂
     * @return 未与货架绑定的入仓签
     */
    List<UnboundBoxLabelDTO> selectUnboundBoxLabelList(@Param("date") String date, @Param("plant") Integer plant);
}
