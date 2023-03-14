package com.jovefast.flowable.mapper;

import com.jovefast.flowable.domain.dto.FlowEnclosureDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 附件数据层
 * @Author Acechengui
 * @Date Created in 2023/3/13
 */
public interface FlowAttachmentMapper {

    List<FlowEnclosureDTO> selectActHiAttachmentByuserId(FlowEnclosureDTO params);

    int deleteActHiAttachmentById(String hId);

    FlowEnclosureDTO selectActHiAttachmentByhid(@Param("hid") String hid);

}
