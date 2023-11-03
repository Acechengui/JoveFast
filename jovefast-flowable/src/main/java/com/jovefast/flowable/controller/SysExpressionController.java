package com.jovefast.flowable.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.jovefast.common.core.utils.poi.ExcelUtil;
import com.jovefast.common.core.web.controller.BaseController;
import com.jovefast.common.core.web.domain.AjaxResult;
import com.jovefast.common.core.web.page.TableDataInfo;
import com.jovefast.common.log.annotation.Log;
import com.jovefast.common.log.enums.BusinessType;
import com.jovefast.common.security.annotation.RequiresPermissions;
import com.jovefast.flowable.domain.SysExpression;
import com.jovefast.flowable.service.ISysExpressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程达式Controller
 *
 * @author Acecehgnui
 */
@RestController
@RequestMapping("/flowable/expression")
public class SysExpressionController extends BaseController
{
    @Autowired
    private ISysExpressionService sysExpressionService;

    /**
     * 查询流程达式列表
     */
    @RequiresPermissions("flowable:expression:list")
    @GetMapping("/list")
    public TableDataInfo list(SysExpression sysExpression)
    {
        startPage();
        List<SysExpression> list = sysExpressionService.selectSysExpressionList(sysExpression);
        return getDataTable(list);
    }

    /**
     * 导出流程达式列表
     */
    @RequiresPermissions("flowable:expression:export")
    @Log(title = "流程表达式", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysExpression sysExpression)
    {
        List<SysExpression> list = sysExpressionService.selectSysExpressionList(sysExpression);
        ExcelUtil<SysExpression> util = new ExcelUtil<SysExpression>(SysExpression.class);
        util.exportExcel(response, list, "流程表达式数据",false,false);
    }

    /**
     * 获取流程达式详细信息
     */
    @RequiresPermissions("flowable:expression:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysExpressionService.selectSysExpressionById(id));
    }

    /**
     * 新增流程达式
     */
    @RequiresPermissions("flowable:expression:add")
    @Log(title = "流程表达式", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysExpression sysExpression)
    {
        return toAjax(sysExpressionService.insertSysExpression(sysExpression));
    }

    /**
     * 修改流程达式
     */
    @RequiresPermissions("flowable:expression:edit")
    @Log(title = "流程表达式", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysExpression sysExpression)
    {
        return toAjax(sysExpressionService.updateSysExpression(sysExpression));
    }

    /**
     * 删除流程达式
     */
    @RequiresPermissions("system:expression:remove")
    @Log(title = "流程表达式", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysExpressionService.deleteSysExpressionByIds(ids));
    }
}
