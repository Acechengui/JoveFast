package com.jovefast.flowable.service.impl;

import com.jovefast.common.core.constant.HttpStatus;
import com.jovefast.common.core.domain.R;
import com.jovefast.common.core.exception.CheckedException;
import com.jovefast.common.security.utils.SecurityUtils;
import com.jovefast.flowable.domain.dto.FlowEnclosureDTO;
import com.jovefast.flowable.mapper.FlowAttachmentMapper;
import com.jovefast.flowable.service.IFlowAttachmentService;
import com.jovefast.system.api.RemoteFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description 附件类业务层
 * @Author Acechengui
 * @Date Created in 2023/3/13
 */
@Service
public class FlowAttachmentServiceImpl implements IFlowAttachmentService {

    @Autowired
    private FlowAttachmentMapper flowAttachmentMapper;

    @Autowired
    private RemoteFileService remoteFileService;

    /**
     * 根据id获取当前用户所上传的附件列表
     *
     */
    @Override
    public List<FlowEnclosureDTO> getActHiAttachmentByuserId(FlowEnclosureDTO params) {
        params.setHuserId(SecurityUtils.getUserId());
        return flowAttachmentMapper.selectActHiAttachmentByuserId(params);
    }

    /**
     * 根据唯一标识删除附件信息
     *
     * @param ids 唯一标识
     */
    @Override
    @Transactional
    public Boolean delActHiAttachmentByids(String[] ids) {
        for (String s : ids) {
            FlowEnclosureDTO hurl = flowAttachmentMapper.selectActHiAttachmentByhid(s);
            R<Boolean> booleanR = remoteFileService.delByPath(hurl.getHurl());
            if (booleanR.getCode() == HttpStatus.SUCCESS){
                flowAttachmentMapper.deleteActHiAttachmentById(s);
            }else {
                throw new CheckedException("删除附件出现异常");
            }
        }
        return true;
    }
}
