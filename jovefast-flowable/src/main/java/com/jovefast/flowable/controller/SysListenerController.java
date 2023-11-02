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
import com.jovefast.flowable.domain.SysListener;
import com.jovefast.flowable.service.ISysListenerService;
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
 * 流程监听Controller
 * 
 * @author Acecehgnui
 */
@RestController
@RequestMapping("/flowable/listener")
public class SysListenerController extends BaseController
{
    @Autowired
    private ISysListenerService sysListenerService;

    /**
     * 查询流程监听列表
     */
    @RequiresPermissions("system:listener:list")
    @GetMapping("/list")
    public TableDataInfo list(SysListener sysListener)
    {
        startPage();
        List<SysListener> list = sysListenerService.selectSysListenerList(sysListener);
        return getDataTable(list);
    }

    /**
     * 导出流程监听列表
     */
    @RequiresPermissions("system:listener:export")
    @Log(title = "流程监听", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysListener sysListener)
    {
        List<SysListener> list = sysListenerService.selectSysListenerList(sysListener);
        ExcelUtil<SysListener> util = new ExcelUtil<SysListener>(SysListener.class);
        util.exportExcel(response, list, "流程监听数据",false,false);
    }

    /**
     * 获取流程监听详细信息
     */
    @RequiresPermissions("system:listener:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysListenerService.selectSysListenerById(id));
    }

    /**
     * 新增流程监听
     */
    @RequiresPermissions("system:listener:add")
    @Log(title = "流程监听", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysListener sysListener)
    {
        return toAjax(sysListenerService.insertSysListener(sysListener));
    }

    /**
     * 修改流程监听
     */
    @RequiresPermissions("system:listener:edit")
    @Log(title = "流程监听", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysListener sysListener)
    {
        return toAjax(sysListenerService.updateSysListener(sysListener));
    }

    /**
     * 删除流程监听
     */
    @RequiresPermissions("system:listener:remove")
    @Log(title = "流程监听", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysListenerService.deleteSysListenerByIds(ids));
    }
}
