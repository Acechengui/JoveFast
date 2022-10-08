package com.jovefast.file.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.jovefast.file.utils.FileUploadUtils;

/**
 * 本地文件存储
 * 
 * @author Acechengui
 */
@Service
public class LocalSysFileServiceImpl implements ISysFileService
{
    /**
     * 资源映射路径 前缀
     */
    @Value("${file.prefix}")
    public String localFilePrefix;

    /**
     * 域名或本机访问地址
     */
    @Value("${file.domain}")
    public String domain;
    
    /**
     * 上传文件存储在本地的根路径
     */
    @Value("${file.path}")
    private String localFilePath;

    /**
     * 本地文件上传接口
     * 
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public String uploadFile(MultipartFile file) throws Exception
    {
        String name = FileUploadUtils.upload(localFilePath, file);
        String url = domain + localFilePrefix + name;
        return url;
    }

    /**
     * 删除文件接口
     *
     * @param path 文件枯竭
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public boolean delFile(String path) throws Exception {
        return false;
    }

    @Override
    public byte[] downloadTemplate(String objectName) throws Exception {
        return new byte[0];
    }

}
