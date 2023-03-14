package com.jovefast.flowable.service;

import com.jovefast.flowable.domain.dto.FlowEnclosureDTO;

import java.util.List;

/**
 * @Description 附件业务操作层
 * @Author Acechengui
 * @Date Created in 2023/3/13
 */
public interface IFlowAttachmentService {

    /**
     * 根据id获取当前用户所上传的附件列表
     */
   List<FlowEnclosureDTO> getActHiAttachmentByuserId(FlowEnclosureDTO params);

    /**
     * 根据唯一标识删除附件信息
     * @param ids 唯一标识
     */
   Boolean delActHiAttachmentByids(String[] ids);
}
