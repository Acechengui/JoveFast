package com.jovefast.flowable.controller;

import com.jovefast.common.core.web.controller.BaseController;
import com.jovefast.common.core.web.domain.AjaxResult;
import com.jovefast.common.core.web.page.TableDataInfo;
import com.jovefast.common.log.annotation.Log;
import com.jovefast.common.log.enums.BusinessType;
import com.jovefast.common.security.annotation.RequiresPermissions;
import com.jovefast.flowable.domain.dto.FlowTaskDto;
import com.jovefast.flowable.domain.dto.FlowViewerDto;
import com.jovefast.flowable.domain.vo.FlowTaskVo;
import com.jovefast.flowable.service.IFlowTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.flowable.bpmn.model.UserTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * 工作流任务管理
 *
 * @author Acecehgnui
 */
@Api(tags = "工作流流程任务管理")
@RestController
@RequestMapping("/flowable/task")
public class FlowTaskController extends BaseController {

    @Autowired
    private IFlowTaskService flowTaskService;

    @ApiOperation(value = "我发起的流程", response = FlowTaskDto.class)
    @GetMapping(value = "/myProcess")
    @RequiresPermissions("flowable:task:myProcess")
    public Map<String, Object> myProcess(@ApiParam(value = "当前页码", required = true) @RequestParam Integer pageNum,
                                         @ApiParam(value = "每页条数", required = true) @RequestParam Integer pageSize,
                                         FlowTaskDto params) {
        return flowTaskService.myProcess(pageNum, pageSize,params);
    }

    @ApiOperation(value = "取消申请", response = FlowTaskDto.class)
    @PostMapping(value = "/stopProcess")
    @Log(title = "取消申请", businessType = BusinessType.UPDATE)
    @RequiresPermissions("flowable:task:stopProcess")
    public AjaxResult stopProcess(@RequestBody FlowTaskVo flowTaskVo) {
        boolean b = flowTaskService.stopProcess(flowTaskVo);
        if(b){
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    @ApiOperation(value = "撤回任务", response = FlowTaskDto.class)
    @PostMapping(value = "/revokeProcess")
    @RequiresPermissions("flowable:finished:revokeProcess")
    public AjaxResult revokeProcess(@RequestBody FlowTaskVo flowTaskVo) {
        return AjaxResult.success(flowTaskService.revokeProcess(flowTaskVo));
    }

    @ApiOperation(value = "获取待办列表", response = FlowTaskDto.class)
    @GetMapping(value = "/todoList")
    @RequiresPermissions("flowable:task:todoList")
    public Map<String, Object> todoList(@ApiParam(value = "当前页码", required = true) @RequestParam Integer pageNum,
                                  @ApiParam(value = "每页条数", required = true) @RequestParam Integer pageSize, FlowTaskDto params) {
        return flowTaskService.todoList(pageNum, pageSize,params);
    }

    @ApiOperation(value = "获取已办任务", response = FlowTaskDto.class)
    @GetMapping(value = "/finishedList")
    @RequiresPermissions("flowable:task:finishedList")
    public Map<String, Object> finishedList(@ApiParam(value = "当前页码", required = true) @RequestParam Integer pageNum,
                                            @ApiParam(value = "每页条数", required = true) @RequestParam Integer pageSize,
                                            FlowTaskDto params) {
        return flowTaskService.finishedList(pageNum, pageSize, params);
    }

    @ApiOperation(value = "获取抄送列表", response = FlowTaskDto.class)
    @GetMapping(value = "/ccList")
    @RequiresPermissions("flowable:task:ccList")
    public Map<String, Object> ccList(@ApiParam(value = "当前页码", required = true) @RequestParam Integer pageNum,
                                      @ApiParam(value = "每页条数", required = true) @RequestParam Integer pageSize,
                                      FlowTaskDto params) {
        return flowTaskService.ccList(pageNum, pageSize, params);
    }


    @ApiOperation(value = "流程历史流转记录", response = FlowTaskDto.class)
    @GetMapping(value = "/flowRecord")
    public AjaxResult flowRecord(String procInsId, String deployId) {
        Map<String, Object> flowRecord=flowTaskService.flowRecord(procInsId, deployId);
        return AjaxResult.success(flowRecord);
    }

    @ApiOperation(value = "获取流程变量", response = FlowTaskDto.class)
    @GetMapping(value = "/processVariables/{taskId}")
    public AjaxResult processVariables(@ApiParam(value = "流程任务Id") @PathVariable(value = "taskId") String taskId) {
        return AjaxResult.success(flowTaskService.processVariables(taskId));
    }

    @ApiOperation(value = "审批任务")
    @PostMapping(value = "/complete")
    @RequiresPermissions("flowable:task:complete")
    @Log(title = "审批任务", businessType = BusinessType.UPDATE)
    public AjaxResult complete(@RequestBody FlowTaskVo params) {
        if(flowTaskService.complete(params)){
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    @ApiOperation(value = "批量审批任务")
    @RequestMapping(value = "/complete/batch/{ids}")
    @RequiresPermissions("flowable:task:batchhandle")
    @Log(title = "批量审批任务", businessType = BusinessType.BATCH)
    public AjaxResult batchComplete(@PathVariable String[] ids) {
        if(ids.length >10){
            return AjaxResult.error("批量审批最多不能超过10条");
        }
        if(flowTaskService.batchComplete(ids)){
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    @ApiOperation(value = "抄送任务")
    @PostMapping(value = "/courtesycopy")
    @RequiresPermissions("flowable:task:handle")
    @Log(title = "抄送任务", businessType = BusinessType.UPDATE)
    public AjaxResult courtesyCopy(@RequestBody FlowTaskVo params) {
        if(flowTaskService.courtesyCopy(params)){
            return AjaxResult.success("抄送发送成功");
        }
        return AjaxResult.error();
    }

    @ApiOperation(value = "驳回任务")
    @PostMapping(value = "/reject")
    @Log(title = "驳回任务", businessType = BusinessType.UPDATE)
    @RequiresPermissions("flowable:task:reject")
    public AjaxResult taskReject(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.taskReject(flowTaskVo);
        return AjaxResult.success();
    }

    @ApiOperation(value = "退回任务")
    @PostMapping(value = "/return")
    @RequiresPermissions("flowable:task:return")
    @Log(title = "退回任务", businessType = BusinessType.UPDATE)
    public AjaxResult taskReturn(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.taskReturn(flowTaskVo);
        return AjaxResult.success();
    }

    @ApiOperation(value = "获取所有可回退的节点")
    @PostMapping(value = "/returnList")
    public AjaxResult findReturnTaskList(@RequestBody FlowTaskVo flowTaskVo) {
        return AjaxResult.success(flowTaskService.findReturnTaskList(flowTaskVo));
    }

    @ApiOperation(value = "删除任务")
    @DeleteMapping(value = "/delete")
    @Log(title = "删除任务", businessType = BusinessType.DELETE)
    @RequiresPermissions("flowable:task:delete")
    public AjaxResult delete(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.deleteTask(flowTaskVo);
        return AjaxResult.success();
    }

    @ApiOperation(value = "认领/签收任务")
    @PostMapping(value = "/claim")
    @RequiresPermissions("flowable:task:claim")
    @Log(title = "认领/签收任务", businessType = BusinessType.UPDATE)
    public AjaxResult claim(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.claim(flowTaskVo);
        return AjaxResult.success();
    }

    @ApiOperation(value = "取消认领/签收任务")
    @PostMapping(value = "/unClaim")
    @RequiresPermissions("flowable:task:unClaim")
    @Log(title = "取消认领/签收任务", businessType = BusinessType.UPDATE)
    public AjaxResult unClaim(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.unClaim(flowTaskVo);
        return AjaxResult.success();
    }

    @ApiOperation(value = "委派任务")
    @PostMapping(value = "/delegate")
    @Log(title = "委派任务", businessType = BusinessType.UPDATE)
    @RequiresPermissions("flowable:task:delegate")
    public AjaxResult delegate(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.delegateTask(flowTaskVo);
        return AjaxResult.success();
    }

    @ApiOperation(value = "转办任务")
    @PostMapping(value = "/assign")
    @Log(title = "转办任务", businessType = BusinessType.UPDATE)
    @RequiresPermissions("flowable:task:assign")
    public AjaxResult assign(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.assignTask(flowTaskVo);
        return AjaxResult.success();
    }

    @ApiOperation(value = "获取下一节点")
    @PostMapping(value = "/nextFlowNode")
    public AjaxResult getNextFlowNode(@RequestBody FlowTaskVo flowTaskVo) {
        return AjaxResult.success(flowTaskService.getNextFlowNode(flowTaskVo));
    }

    @ApiOperation(value = "流程发起时获取下一节点")
    @PostMapping(value = "/nextFlowNodeByStart")
    public AjaxResult getNextFlowNodeByStart(@RequestBody FlowTaskVo flowTaskVo) {
        return AjaxResult.success(flowTaskService.getNextFlowNodeByStart(flowTaskVo));
    }

    /**
     * 生成流程图
     *
     * @param processId 任务ID
     */
    @RequestMapping("/diagram/{processId}")
    public void genProcessDiagram(HttpServletResponse response,
                                  @PathVariable("processId") String processId) {
        InputStream inputStream = flowTaskService.diagram(processId);
        OutputStream os = null;
        BufferedImage image = null;
        try {
            image = ImageIO.read(inputStream);
            response.setContentType("image/png");
            os = response.getOutputStream();
            if (image != null) {
                ImageIO.write(image, "png", os);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取流程执行节点
     *
     * @param procInsId 流程实例编号
     * @param executionId 任务执行编号
     */
    @RequestMapping("/flowViewer/{procInsId}/{executionId}")
    public AjaxResult getFlowViewer(@PathVariable("procInsId") String procInsId,
                                    @PathVariable("executionId") String executionId) {
        List<FlowViewerDto> viewer = flowTaskService.getFlowViewer(procInsId, executionId);
        return AjaxResult.success(viewer);
    }

    /**
     * 流程节点信息
     * @param procInsId     流程实例id
     */
    @GetMapping("/flowXmlAndNode")
    public AjaxResult flowXmlAndNode(@RequestParam(value = "procInsId",required = false) String procInsId,
                                     @RequestParam(value = "deployId",required = false) String deployId){
        try {
            return AjaxResult.success(flowTaskService.flowXmlAndNode(procInsId,deployId));
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.error("高亮历史任务失败");
        }
    }
}
