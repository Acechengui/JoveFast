package com.jovefast.system.api;

import com.jovefast.system.api.domain.SysFileInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.jovefast.common.core.constant.ServiceNameConstants;
import com.jovefast.common.core.domain.R;
import com.jovefast.system.api.domain.SysFile;
import com.jovefast.system.api.factory.RemoteFileFallbackFactory;

/**
 * 文件服务
 * 
 * @author Acechengui
 */
@FeignClient(contextId = "remoteFileService", value = ServiceNameConstants.FILE_SERVICE, fallbackFactory = RemoteFileFallbackFactory.class)
public interface RemoteFileService
{
    /**
     * 上传文件
     *
     * @param file 文件信息
     * @return 结果
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<SysFile> upload(@RequestPart(value = "file") MultipartFile file);

    /**
     * 根据路径删除文件
     *
     * @param filePath 文件信息
     * @return 结果
     */
    @RequestMapping(value = "/del")
    R<Boolean> delByPath(@RequestParam("filePath") String filePath);

    /**
     * 保存系统文件 测试Feign服务调用
     *
     * @param sysFileInfo 系统文件
     * @return 结果
     */
    @PostMapping("/insertFile")
    R<Boolean> saveFile(@RequestBody SysFileInfo sysFileInfo);

    /**
     * 下载模板文件
     * @param objectName 文件名（路径）
     * @return
     */
    @GetMapping("/downloadFile")
    byte[] downloadTemplate(@RequestParam("objectName") String objectName) throws Exception;

}
