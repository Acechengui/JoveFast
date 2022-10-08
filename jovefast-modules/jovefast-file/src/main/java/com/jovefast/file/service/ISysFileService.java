package com.jovefast.file.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 * 
 * @author Acechengui
 */
public interface ISysFileService
{
    /**
     * 文件上传接口
     * 
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    String uploadFile(MultipartFile file) throws Exception;

    /**
     * 删除文件接口
     *
     * @param path 文件路径
     * @return 访问地址
     * @throws Exception
     */
    boolean delFile(String path) throws Exception;


    /**
     * 下载模板文件
     * @param objectName 文件名（路径）
     * @return Exception
     */
    byte[] downloadTemplate(String objectName) throws Exception;

}
