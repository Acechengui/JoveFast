package com.jovefast.flowable.controller;

import com.jovefast.common.core.web.controller.BaseController;
import com.jovefast.common.core.web.domain.AjaxResult;
import com.jovefast.common.core.web.page.TableDataInfo;
import com.jovefast.common.log.annotation.Log;
import com.jovefast.common.log.enums.BusinessType;
import com.jovefast.common.security.annotation.RequiresPermissions;
import com.jovefast.flowable.domain.dto.FlowEnclosureDTO;
import com.jovefast.flowable.service.IFlowAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 附件接口层
 * @Author Acechengui
 * @Date Created in 2023/3/13
 */
@RestController
@RequestMapping("/flowable/enclosure")
public class FlowAttachmentContoller extends BaseController {

    @Autowired
    private IFlowAttachmentService iflowAttachmentService;

    @GetMapping("/list")
    @RequiresPermissions("flowable:enclosure:list")
    public TableDataInfo list(FlowEnclosureDTO params) {
        startPage();
        return getDataTable(iflowAttachmentService.getActHiAttachmentByuserId(params));
    }

    @DeleteMapping(value = "/delete/{ids}")
    @RequiresPermissions("flowable:enclosure:del")
    @Log(title = "删除附件", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable String[] ids) {
        Boolean aBoolean = iflowAttachmentService.delActHiAttachmentByids(ids);
        if(aBoolean){
           return AjaxResult.success("删除附件成功");
        }
        return AjaxResult.error("删除附件失败");
    }
}
