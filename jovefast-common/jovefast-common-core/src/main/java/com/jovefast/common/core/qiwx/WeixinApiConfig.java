package com.jovefast.common.core.qiwx;

import com.alibaba.fastjson2.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class WeixinApiConfig {
    //机器人发送信息 API
    private static final String ROBOT_SEND_FILE = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=";
    //企业微信应用发送信息 API
    private static final String WEIXIN_SEND_FILE = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=";
    //机器人上传文件素材库 API
    private static final String ROBOT_MEDIA_ID = "https://qyapi.weixin.qq.com/cgi-bin/webhook/upload_media?type=file&key=";
    //企业微信上传文件素材库 API
    private static final String WEIXIN_MEDIA_ID = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token=";
    //token凭证
    private static String access_token;

    /**
     * 上传临时素材文件库
     *
     * @param file 文件
     * @param key  机器人唯一标识||企业微信应用唯一标识符
     * @param type 0：上传到机器人素材库；1：上传到企业微信素材库
     * @return 返回media_id
     */
    public static String temporaryFile(File file, String key, int type) {
        StringBuilder buffer = new StringBuilder();
        try {
            if (type == 1) {
                access_token = new TokenUtilForWechat().getToken(key);
            }
            URL url = type == 0 ? new URL(ROBOT_MEDIA_ID + key) : new URL(WEIXIN_MEDIA_ID + access_token + "&type=file");
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
            //1.1输入输出设置
            httpUrlConn.setDoInput(true);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setUseCaches(false); // post方式不能使用缓存

            //1.2设置请求头信息
            httpUrlConn.setRequestProperty("Connection", "Keep-Alive");
            httpUrlConn.setRequestProperty("Charset", "UTF-8");
            //1.3设置边界
            String BOUNDARY = "----------" + System.currentTimeMillis();
            httpUrlConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            // 请求正文信息
            // 第一部分：
            // 2.将文件头输出到微信服务器
            StringBuilder sb = new StringBuilder();
            sb.append("--"); // 必须多两道线
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"media\";filelength=\"").append(file.length()).append("\";filename=\"").append(file.getName()).append("\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");
            byte[] head = sb.toString().getBytes(StandardCharsets.UTF_8);
            // 获得输出流
            OutputStream outputStream = new DataOutputStream(httpUrlConn.getOutputStream());
            // 将表头写入输出流中：输出表头
            outputStream.write(head);

            //3.将文件正文部分输出到微信服务器
            // 把文件以流文件的方式 写入到微信服务器中
            DataInputStream in = new DataInputStream(Files.newInputStream(file.toPath()));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                outputStream.write(bufferOut, 0, bytes);
            }
            in.close();
            //4.将结尾部分输出到微信服务器
            byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes(StandardCharsets.UTF_8);// 定义最后数据分隔线
            outputStream.write(foot);
            outputStream.flush();
            outputStream.close();


            //5.将微信服务器返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            httpUrlConn.disconnect();
        } catch (IOException e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
        JSONObject json = JSONObject.parseObject(buffer.toString());
        return json.getString("media_id");
    }


    /**
     * 发送信息
     *
     * @param key  机器人唯一标识||企业微信应用唯一标识符
     * @param text 文本内容
     * @param type 0：上传到机器人素材库；1：上传到企业微信素材库
     * @return true: 发送成功 false: 发送失败
     */
    public static Boolean rotSendInfo(String key, String text, int type) {
        boolean isSend = false;
        try {
            if (type == 1) {
                access_token = new TokenUtilForWechat().getToken(key);
            }
            URL url = type == 0 ? new URL(ROBOT_SEND_FILE + key) : new URL(WEIXIN_SEND_FILE + access_token);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //System.out.println("access_token======"+access_token);
            // 设定请求的方法为"POST"，默认是GET
            conn.setRequestProperty("content-type", "application/json;charset=utf-8");
            conn.setRequestProperty("contentType", "utf-8");

            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true, 默认情况下是false;
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            out.write(text.getBytes("UTF-8"));
            out.flush();
            out.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            JSONObject resultList = JSONObject.parseObject(reader.readLine());
            isSend = "0".equals(resultList.get("errcode").toString());
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSend;
    }

    /**
     * 机器人@全部人（公用的信息模板）
     *
     * @param key 机器人唯一标识
     */
    public static void rotTextAll(String key) {
        String text = "{\n" +
                "    \"msgtype\": \"text\",\n" +
                "    \"text\": {\n" +
                "        \"content\": \"请注意查收\",\n" +
                "        \"mentioned_list\":[\"wangqing\",\"@all\"],\n" +
                "        \"mentioned_mobile_list\":[\"13800001111\",\"@all\"]\n" +
                "    }\n" +
                "}";
        rotSendInfo(key, text, 0);
    }

    /**
     * 机器人上传文件模板
     *
     * @param media_id 临时素材 media_id
     */
    public static String robotFileModel(String media_id) {
        String text = "{\n" +
                "    \"msgtype\": \"file\",\n" +
                "    \"file\": {\n" +
                "         \"media_id\": \"" + media_id + "\"\n" +
                "    }\n" +
                "}";
        return text;
    }
}
