package com.jovefast.file.service;

import com.jovefast.common.core.utils.file.FileTypeUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import java.io.InputStream;

/**
 * FastDFS 文件存储
 *
 * @author Acechengui
 */
@Service
public class FastDfsSysFileServiceImpl implements ISysFileService
{
    /**
     * 域名或本机访问地址
     */
    @Value("${fdfs.domain}")
    public String domain;

    @Autowired
    private FastFileStorageClient storageClient;

    /**
     * FastDfs文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public String uploadFile(MultipartFile file) throws Exception
    {
        InputStream inputStream = file.getInputStream();
        StorePath storePath = storageClient.uploadFile(inputStream, file.getSize(),
                FileTypeUtils.getExtension(file), null);
        inputStream.close();
        return domain + "/" + storePath.getFullPath();
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
