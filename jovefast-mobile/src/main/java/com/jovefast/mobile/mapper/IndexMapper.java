package com.jovefast.mobile.mapper;

import com.jovefast.mobile.domain.AccessingDataInThePastSevenDays;

import java.util.List;

/**
 * @Description Description
 * @Author Acechengui
 * @Date Created in 2023/5/19
 */
public interface IndexMapper {

    List<AccessingDataInThePastSevenDays> selectAccessingDataInThePastSevenDays();
}
