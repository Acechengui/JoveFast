package com.jovefast.job.service;

import com.jovefast.job.domain.dto.VCustomerWipDTO;

import java.util.List;
import java.util.Map;

/**
 * @Author Acechengui
 * @Date Created in 2022/9/16
 */
public interface IErpCustomerWIPService {

    /**
     * 预警
     */
    List<VCustomerWipDTO> earlyWarn(Map<String, Object> params);
}
