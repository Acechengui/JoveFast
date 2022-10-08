package com.jovefast.job.service;

import com.jovefast.job.domain.dto.UnboundBoxLabelDTO;

import java.util.List;

/**
 * 入仓签（装箱贴）
 *
 * @author Dijkstra
 */
public interface IBoxLabelService {

    /**
     * 根据指定日期和厂别查询未与货架绑定的入仓签
     *
     * @param date  日期 yyyy-MM-dd
     * @param plant 厂别 1：沙井厂；2：松岗厂；3：鹤山厂
     * @return 未与货架绑定的入仓签
     */
    List<UnboundBoxLabelDTO> selectUnboundBoxLabelList(String date, Integer plant);
}
