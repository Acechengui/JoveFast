package com.jovefast.file.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.jovefast.file.config.MinioConfig;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Component
public class MinioFileInterface {
    @Autowired
    private MinioConfig minioConfig;

    private MinioClient init() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient build = MinioClient.builder().endpoint(minioConfig.getUrl()).credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey()).build();
        //判断bucket是否存在，没有就创建
        boolean found =build.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucketName()).build());
        if (!found) {
            build.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucketName()).build());
        }
        return build;
    }

    //上传文件并 返回文件路径
    public String uploadImage(String prefix,MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String suffixName = "";
            if (fileName.contains(".")) {
                suffixName = fileName.substring(fileName.lastIndexOf("."));
            }
            //生成新的文件名，避免重复
            String newFileName = RandomUtil.randomString(5) + System.currentTimeMillis() + suffixName;
            Date date = DateUtil.date();
            //创建minio客户端，用于接下来的存储操作，如果你的minio-java依赖版本低，没有builder()方法
            MinioClient minioClient = init();
            int year = DateUtil.year(date);
            int month = DateUtil.month(date) + 1;
            //例如:ERP/SpotCheck_TEST/年/月/dasdasd.png
            String url =  year + "/" + month + "/" + newFileName;
            //上传文件
            minioClient.putObject(PutObjectArgs
                    .builder()
                    .bucket(minioConfig.getBucketName())
                    .object(prefix + url)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            //返回url
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除文件
     * @param objectName 文件名（路径）
     */
    public void deleteObject(String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = init();
        //删除文件时，如果对应文件夹下的文件已删除完，文件夹会自动删除
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(objectName)
                //.versionId("my-versionid") //还可以删除指定版本号的对象
                .build());
    }

    /**
     * 下载文件
     * @param objectName 文件名（路径）
     */
    public byte[] download(String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(minioConfig.getBucketName())
                .object(objectName).build();
        MinioClient minioClient = init();
        GetObjectResponse response = minioClient.getObject(objectArgs);
        byte[] buf = new byte[1024];
        int len;
        try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
            while ((len = response.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
            os.flush();
            byte[] bytes = os.toByteArray();
            os.close();
            return bytes;
        }
    }

    /**
     * 生成一个GET请求的分享链接。
     * 失效时间默认是7天。
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param expires    失效时间（以秒为单位），默认是7天，不得大于七天
     * @return url
     */
    public String presignedGetObject(String bucketName, String objectName, Integer expires) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String url = "";
        if (expires == null){
            expires = 604800;
        }
        GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(objectName)
                // .expiry(expires)
                .build();
        MinioClient minioClient = init();
        url = minioClient.getPresignedObjectUrl(getPresignedObjectUrlArgs);
        return url;
    }

}
