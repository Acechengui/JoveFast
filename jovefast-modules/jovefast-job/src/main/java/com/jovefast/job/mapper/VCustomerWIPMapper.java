package com.jovefast.job.mapper;

import com.jovefast.job.domain.dto.DetailCustomerWipDTO;
import com.jovefast.job.domain.dto.VCustomerWipDTO;

import java.util.List;
import java.util.Map;

/**
 * @author Acechengui
 */
public interface VCustomerWIPMapper {
    /**
     导出：外层
     **/
    List<VCustomerWipDTO> listOuterCustomerWIPExport(Map<String, Object> param);

    /**
     导出：内层
     */
    List<VCustomerWipDTO> listInnerCustomerWIPExport(Map<String, Object> param);

    /**
     * 欠料清单
     **/
    List<DetailCustomerWipDTO> listBackLogCustomerWIP();

}