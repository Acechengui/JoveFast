package com.jovefast.common.core.utils.file;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;

/**
 * 文件压缩/解压
 *
 * @author MuTouYSK
 * @date 2022-02-10 15:02
 */
public class CompressedFileUtils {
    /*
    文件解压路径
    */
    private String descDir = System.getProperty("user.dir") + "/upload/";

    /**
     * 解压ZIP文件夹
     * @param zipFile   zip路径
     * @return
     * @throws IOException
     */
    public String unZip(File zipFile) throws IOException {
        String descDir = this.descDir + zipFile.getName().substring(0, zipFile.getName().length() - 4);
        try (ZipArchiveInputStream inputStream = getZipFile(zipFile)) {
            File pathFile = new File(descDir);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            ZipArchiveEntry entry = null;
            while ((entry = inputStream.getNextZipEntry()) != null) {
                File directory = new File(descDir, entry.getName());

                //判断该文件是否为目录，是则创建不是则写入。
                if (entry.isDirectory()) {
                    directory.mkdirs();
                } else {
                    //判断该目录的父级目录是否存在，不存在则创建父级目录
                    if(!directory.exists()) {
                        directory.getParentFile().mkdirs();
                        directory.createNewFile();
                    }
                    //写入文件
                    OutputStream os = null;
                    try {
                        os = new BufferedOutputStream(new FileOutputStream(new File(descDir, entry.getName())));
                        IOUtils.copy(inputStream, os);
                    } finally {
                        IOUtils.closeQuietly(os);
                    }
                }
            }
        } catch (Exception e) {
            throw new IOException("解压zip文件出错：" + e);
        }
        return descDir;
    }

    private static ZipArchiveInputStream getZipFile(File zipFile) throws Exception {
        return new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
    }

}
