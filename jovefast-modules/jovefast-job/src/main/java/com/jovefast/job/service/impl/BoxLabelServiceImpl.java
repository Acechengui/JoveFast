package com.jovefast.job.service.impl;

import com.jovefast.job.domain.dto.UnboundBoxLabelDTO;
import com.jovefast.job.mapper.BoxLabelMapper;
import com.jovefast.job.service.IBoxLabelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 入仓签（装箱贴）
 *
 * @author Dijkstra
 */
@Service
public class BoxLabelServiceImpl implements IBoxLabelService {

    @Resource
    private BoxLabelMapper boxLabelMapper;

    /**
     * 根据指定日期和厂别查询未与货架绑定的入仓签
     *
     * @param date  日期 yyyy-MM-dd
     * @param plant 厂别 1：沙井厂；2：松岗厂；3：鹤山厂
     * @return 未与货架绑定的入仓签
     */
    @Override
    public List<UnboundBoxLabelDTO> selectUnboundBoxLabelList(String date, Integer plant) {
        return boxLabelMapper.selectUnboundBoxLabelList(date, plant);
    }
}
