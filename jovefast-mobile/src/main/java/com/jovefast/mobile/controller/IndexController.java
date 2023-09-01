package com.jovefast.mobile.controller;

import com.jovefast.common.core.web.controller.BaseController;
import com.jovefast.common.core.web.domain.AjaxResult;
import com.jovefast.common.security.annotation.RequiresPermissions;
import com.jovefast.mobile.domain.dto.CategoriesSeriesDTO;
import com.jovefast.mobile.service.IIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 首页
 * @Author Acechengui
 * @Date Created in 2023/5/19
 */
@RestController
@RequestMapping("/index")
public class IndexController extends BaseController {

    @Autowired
    private IIndexService indexService;

    @RequiresPermissions("mobile:index:statistics")
    @GetMapping("/statistics")
    public AjaxResult statistics(){
        CategoriesSeriesDTO rs = indexService.getVisitorStatistics();
        return AjaxResult.success(rs);
    }
}
