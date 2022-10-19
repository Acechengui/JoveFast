package com.jovefast.flowable.controller;

import com.jovefast.common.core.constant.SecurityConstants;
import com.jovefast.common.core.domain.R;
import com.jovefast.common.core.web.controller.BaseController;
import com.jovefast.common.core.web.domain.AjaxResult;
import com.jovefast.common.core.web.page.TableDataInfo;
import com.jovefast.common.log.annotation.Log;
import com.jovefast.common.log.enums.BusinessType;
import com.jovefast.common.security.annotation.RequiresPermissions;
import com.jovefast.flowable.domain.dto.FlowProcDefDto;
import com.jovefast.flowable.domain.dto.FlowSaveXmlVo;
import com.jovefast.flowable.service.IFlowDefinitionService;
import com.jovefast.system.api.RemoteUserService;
import com.jovefast.system.api.domain.SysRole;
import com.jovefast.system.api.domain.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 工作流程定义
 *
 * @author Acecehgnui
 */
@Api(tags = "流程定义")
@RestController
@RequestMapping("/flowable/definition")
public class FlowDefinitionController extends BaseController {

    @Autowired
    private IFlowDefinitionService flowDefinitionService;

    @Autowired
    private RemoteUserService remoteuserservice;


    @GetMapping(value = "/list")
    @ApiOperation(value = "流程定义列表", response = FlowProcDefDto.class)
    @RequiresPermissions("flowable:definition:list")
    public TableDataInfo list(@ApiParam(value = "当前页码", required = true) @RequestParam Integer pageNum,
                              @ApiParam(value = "每页条数", required = true) @RequestParam Integer pageSize,
                              @ApiParam(value = "流程名称", required = false) @RequestParam(required = false) String name) {
        startPage();
        return getDataTable(flowDefinitionService.list(name));
    }


    @ApiOperation(value = "导入流程文件", notes = "上传bpmn20的xml文件")
    @PostMapping("/import")
    @RequiresPermissions("flowable:definition:export")
    @Log(title = "导入流程文件", businessType = BusinessType.IMPORT)
    public AjaxResult importFile(@RequestParam(required = false) String name,
                                 @RequestParam(required = false) String category,
                                 MultipartFile file) throws IOException {
            InputStream in = file.getInputStream();
            flowDefinitionService.importFile(name, category, in);
            return AjaxResult.success("导入成功");
    }


    @ApiOperation(value = "读取xml文件")
    @GetMapping("/readXml/{deployId}")
    @RequiresPermissions("flowable:definition:edit")
    public AjaxResult readXml(@ApiParam(value = "流程定义id") @PathVariable(value = "deployId") String deployId) throws IOException {
            return AjaxResult.success("读取成功",flowDefinitionService.readXml(deployId));
    }

    @ApiOperation(value = "读取图片文件")
    @GetMapping("/readImage/{deployId}")
    @RequiresPermissions("flowable:definition:edit")
    public void readImage(@ApiParam(value = "流程定义id") @PathVariable(value = "deployId") String deployId, HttpServletResponse response) {
        OutputStream os = null;
        BufferedImage image = null;
        try {
            image = ImageIO.read(flowDefinitionService.readImage(deployId));
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


    @ApiOperation(value = "保存流程设计器内的xml文件")
    @PostMapping("/save")
    @RequiresPermissions("flowable:definition:edit")
    @Log(title = "保存流程设计器内的xml文件", businessType = BusinessType.INSERT)
    public AjaxResult save(@RequestBody FlowSaveXmlVo vo) throws IOException {
        InputStream in = null;
        try {
            in = new ByteArrayInputStream(vo.getXml().getBytes(StandardCharsets.UTF_8));
            flowDefinitionService.importFile(vo.getName(), vo.getCategory(), in);
        } catch (Exception e) {
            return AjaxResult.success(e.getMessage());
        } finally {
                if (in != null) {
                    in.close();
                }
        }
        return AjaxResult.success("保存导入成功");
    }


    @ApiOperation(value = "根据流程定义id启动流程实例")
    @PostMapping("/start/{procDefId}")
    public AjaxResult start(@ApiParam(value = "流程定义id") @PathVariable(value = "procDefId") String procDefId,
                            @ApiParam(value = "变量集合,json对象") @RequestBody Map<String, Object> variables) {
        boolean b = flowDefinitionService.startProcessInstanceById(procDefId, variables);
        if(b){
            return AjaxResult.success("流程启动成功");
        }
        return AjaxResult.error("流程启动失败，请联系管理员");

    }

    @ApiOperation(value = "激活或挂起流程定义")
    @PutMapping(value = "/updateState")
    @RequiresPermissions("flowable:definition:state")
    @Log(title = "激活或挂起流程定义", businessType = BusinessType.UPDATE)
    public AjaxResult updateState(@ApiParam(value = "1:激活,2:挂起", required = true) @RequestParam Integer state,
                                  @ApiParam(value = "流程部署ID", required = true) @RequestParam String deployId) {
        flowDefinitionService.updateState(state, deployId);
        return AjaxResult.success();
    }

    @ApiOperation(value = "删除流程")
    @DeleteMapping(value = "/delete/{deployId}")
    @RequiresPermissions("flowable:definition:del")
    @Log(title = "删除流程", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable String[] deployId) {
        flowDefinitionService.delete(deployId);
        return AjaxResult.success();
    }

    @ApiOperation(value = "指定流程办理人员列表")
    @GetMapping("/userList")
    public AjaxResult userList(SysUser user) {
        R<List<SysUser>> listR = remoteuserservice.selectUserList(user, SecurityConstants.INNER);
        return AjaxResult.success(listR.getData());
    }

    @ApiOperation(value = "指定流程办理组列表")
    @GetMapping("/roleList")
    public AjaxResult roleList(SysRole role) {
        R<List<SysRole>> sysRoleR = remoteuserservice.selectRoleList(role, SecurityConstants.INNER);
        return AjaxResult.success(sysRoleR.getData());
    }

}