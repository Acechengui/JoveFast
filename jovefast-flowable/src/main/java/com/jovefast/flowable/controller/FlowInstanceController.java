package com.jovefast.flowable.controller;


import com.jovefast.common.core.web.controller.BaseController;
import com.jovefast.common.core.web.domain.AjaxResult;
import com.jovefast.common.log.annotation.Log;
import com.jovefast.common.log.enums.BusinessType;
import com.jovefast.common.security.annotation.RequiresPermissions;
import com.jovefast.flowable.service.IFlowInstanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>工作流流程实例管理<p>
 *
 * @author Acecehgnui
 */
@Api(tags = "工作流流程实例管理")
@RestController
@RequestMapping("/flowable/instance")
public class FlowInstanceController extends BaseController {

    @Autowired
    private IFlowInstanceService flowInstanceService;

    @ApiOperation(value = "根据流程定义id启动流程实例")
    @PostMapping("/startBy/{procDefId}")
    public AjaxResult startById(@ApiParam(value = "流程定义id") @PathVariable(value = "procDefId") String procDefId,
                                @ApiParam(value = "变量集合,json对象") @RequestBody Map<String, Object> variables) {
        boolean b = flowInstanceService.startProcessInstanceById(procDefId, variables);
        if(b){
            return AjaxResult.success("流程启动成功");
        }
        return AjaxResult.error("流程启动失败");

    }


    @ApiOperation(value = "激活或挂起流程实例")
    @PostMapping(value = "/updateState")
    @Log(title = "激活或挂起流程实例", businessType = BusinessType.UPDATE)
    public AjaxResult updateState(@ApiParam(value = "1:激活,2:挂起", required = true) @RequestParam Integer state,
                                  @ApiParam(value = "流程实例ID", required = true) @RequestParam String instanceId) {
        flowInstanceService.updateState(state,instanceId);
        return AjaxResult.success();
    }


    @ApiOperation(value = "删除流程实例")
    @DeleteMapping(value = "/delete")
    @Log(title = "删除流程实例", businessType = BusinessType.DELETE)
    @RequiresPermissions("flowable:instance:del")
    public AjaxResult delete(@ApiParam(value = "流程实例ID", required = true) @RequestParam String instanceId,
                             @ApiParam(value = "删除原因") @RequestParam(required = false) String deleteReason) {
        flowInstanceService.delete(instanceId,deleteReason);
        return AjaxResult.success();
    }
}