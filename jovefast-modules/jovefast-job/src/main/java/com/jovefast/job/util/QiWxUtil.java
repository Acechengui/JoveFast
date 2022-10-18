package com.jovefast.job.util;

import com.jovefast.common.core.qiwx.WeixinApiConfig;
import com.jovefast.job.enums.RobotsTarget;

import java.io.File;
import java.util.Objects;

/**
 * @description:  企业微信发送文件工具类
 * @author Acechengui
 * @date Created in 2022/8/2
 */
public class QiWxUtil {
    /**
     发送全部文件
     */
    public static void forFiles(String key, String path) {
        File file = new File(path);
        // 如果这个路径是文件夹
        if (file.isDirectory()) {
            // 获取路径下的所有文件
            File[] files = file.listFiles();
            for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
                String mediaId = WeixinApiConfig.temporaryFile(new File(files[i].getPath()),key, 0);
                String text = "{\n" +
                        "    \"msgtype\": \"file\",\n" +
                        "    \"file\": {\n" +
                        "         \"media_id\": \"" + mediaId + "\"\n" +
                        "    }\n" +
                        "}";
                WeixinApiConfig.rotSendInfo(String.valueOf(key), text, 0);
            }
            // @全体人员
            WeixinApiConfig.rotTextAll(key);
        }
    }

    /**
     发送单个文件
     */
    public static void forFile(String key, String path) {
        File file = new File(path);
        String mediaId = WeixinApiConfig.temporaryFile(new File(file.getPath()), key, 0);
        String text = "{\n" +
                "    \"msgtype\": \"file\",\n" +
                "    \"file\": {\n" +
                "         \"media_id\": \"" + mediaId + "\"\n" +
                "    }\n" +
                "}";
        WeixinApiConfig.rotSendInfo(key, text, 0);
        // @全体人员
        WeixinApiConfig.rotTextAll(key);
    }
}
