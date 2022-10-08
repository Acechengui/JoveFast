package com.jovefast.job.mapper;


import com.jovefast.job.domain.Data9995;

import java.util.List;

/**
 * 点检数据操作
 */
public interface SpotCheckMapper {
    List<Data9995> findExpireData9995();

    int delExpireData9995();
}