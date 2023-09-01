package com.jovefast.mobile.service.impl;

import com.jovefast.mobile.domain.AccessingDataInThePastSevenDays;
import com.jovefast.mobile.domain.dto.CategoriesDTO;
import com.jovefast.mobile.domain.dto.CategoriesSeriesDTO;
import com.jovefast.mobile.mapper.IndexMapper;
import com.jovefast.mobile.service.IIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 首页业务处理
 * @Author Acechengui
 * @Date Created in 2023/5/19
 */
@Service
public class IndexServiceimpl implements IIndexService {

    @Autowired
    private IndexMapper indexMapper;

    @Override
    public CategoriesSeriesDTO getVisitorStatistics() {
        List<AccessingDataInThePastSevenDays> sevenDays = indexMapper.selectAccessingDataInThePastSevenDays();
        List<String> categories = new ArrayList<>();
        List<Integer> data=new ArrayList<>();
        List<CategoriesDTO> silist = new ArrayList<>();
        for (AccessingDataInThePastSevenDays day : sevenDays) {
            categories.add(day.getFromDay());
            data.add(day.getVisits());
        }
        CategoriesDTO si=new CategoriesDTO("访问情况",data,"line","curve","#4ECDB6");
        silist.add(si);
        return new CategoriesSeriesDTO(categories,silist);
    }
}
