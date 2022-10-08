package com.jovefast.system.api.domain;

import java.io.Serializable;

/**
 * 测试案例 -分布式测试
 *
 */
public class SysFileInfo implements Serializable {

    private static final long serialVersionUID = 8672468331105890551L;
    /**
     * 文件编号
     */
    private Long fileId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    public Long getFileId()
    {
        return fileId;
    }

    public void setFileId(Long fileId)
    {
        this.fileId = fileId;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }
}
