package com.jovefast.flowable.controller;


import com.jovefast.common.core.utils.StringUtils;
import com.jovefast.common.core.utils.poi.ExcelUtil;
import com.jovefast.common.core.web.controller.BaseController;
import com.jovefast.common.core.web.domain.AjaxResult;
import com.jovefast.common.core.web.page.TableDataInfo;
import com.jovefast.common.log.annotation.Log;
import com.jovefast.common.log.enums.BusinessType;
import com.jovefast.common.security.annotation.RequiresPermissions;
import com.jovefast.flowable.domain.SysDeployForm;
import com.jovefast.flowable.domain.SysForm;
import com.jovefast.flowable.service.ISysDeployFormService;
import com.jovefast.flowable.service.ISysFormService;
import com.jovefast.system.api.RemoteFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 流程表单Controller
 *
 * @author Acecehgnui
 */
@RestController
@RequestMapping("/flowable/form")
public class FlowFormController extends BaseController {
    @Autowired
    private ISysFormService sysFormService;

    @Autowired
    private ISysDeployFormService sysDeployFormService;


    /**
     * 附件上传
     * @param request 请求
     */
    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult upload(HttpServletRequest request){
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 这里是接收文件
        MultipartFile file = multipartRequest.getFile("files");
        if(file==null){
            return AjaxResult.error("上传异常！files is null ！！");
        }
        String uploadfile = sysFormService.uploadfiles(file);
        if(StringUtils.isNotNull(uploadfile)){
            return AjaxResult.success("上传成功",uploadfile);
        }
        return AjaxResult.error("上传失败");
    }

    /**
     * 查询流程表单列表
     */
    @RequiresPermissions("flowable:form:list")
    @GetMapping("/list")
    public TableDataInfo list(SysForm sysForm) {
        startPage();
        List<SysForm> list = sysFormService.selectSysFormList(sysForm);
        return getDataTable(list);
    }

    /**
     * 导出流程表单列表
     */
    @RequiresPermissions("flowable:form:export")
    @Log(title = "流程表单", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public void export(HttpServletResponse response, SysForm sysForm) {
        List<SysForm> list = sysFormService.selectSysFormList(sysForm);
        ExcelUtil<SysForm> util = new ExcelUtil<SysForm>(SysForm.class);
        util.exportExcel(response, list, "流程表单列表",false,true);
    }

    /**
     * 获取流程表单详细信息
     */
    @RequiresPermissions("flowable:form:list")
    @GetMapping(value = "/{formId}")
    public AjaxResult getInfo(@PathVariable("formId") Long formId) {
        return AjaxResult.success(sysFormService.selectSysFormById(formId));
    }

    /**
     * 新增流程表单
     */
    @RequiresPermissions("flowable:form:add")
    @Log(title = "流程表单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysForm sysForm) {
        return toAjax(sysFormService.insertSysForm(sysForm));
    }

    /**
     * 修改流程表单
     */
    @RequiresPermissions("flowable:form:edit")
    @Log(title = "流程表单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysForm sysForm) {
        return toAjax(sysFormService.updateSysForm(sysForm));
    }

    /**
     * 删除流程表单
     */
    @RequiresPermissions("flowable:form:del")
    @Log(title = "流程表单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{formIds}")
    public AjaxResult remove(@PathVariable Long[] formIds) {
        return toAjax(sysFormService.deleteSysFormByIds(formIds));
    }


    /**
     * 挂载流程表单
     */
    @Log(title = "流程表单", businessType = BusinessType.INSERT)
    @PostMapping("/addDeployForm")
    public AjaxResult addDeployForm(@RequestBody SysDeployForm sysDeployForm) {
        return toAjax(sysDeployFormService.insertSysDeployForm(sysDeployForm));
    }
}
