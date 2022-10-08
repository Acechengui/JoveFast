package com.jovefast.common.core.qiwx;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Acechengui
 */
public class TokenUtilForWechat {
    /**
     企业的ID号,固定值。一个企业只会有一个
     */
    private final static String CORPID = "ww554f12e1d85c2986";
    /**
     获取token的URL
     */
    private final static String TOKENURL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";

    private static TokenUtilForWechat tokenUtil;
    /**
     标志一个对象锁
     */
    private final static Object LOCK = new Object();
    public final static String EXPIRES_IN = "expires_in";
    public final static String ACCESS_TOKEN = "access_token";

    /**
     令牌列表，使用时需要判断是否过期。map.key = 应用key（应用Secret），map.value = 令牌（token）和 有效时间（validTime）
     */
    private static Map<String, Map<String, Object>> myTokens;

    public TokenUtilForWechat() {
    }


    /**
     获取token的实例
     */
    public static TokenUtilForWechat instance() {
        if (tokenUtil == null) {
            synchronized (LOCK) {
                if (tokenUtil == null) {
                    tokenUtil = new TokenUtilForWechat();
                }
            }
        }
        return tokenUtil;
    }


    /**
     * 创建或更新已缓存的令牌集合
     * @param appKey 应用Secret
     * @throws IOException
     */
    private void createOrUpdateToken(String appKey) throws IOException {

        //获取令牌json
        String result = sendRequestToken(CORPID, appKey);
        //拆分json得到令牌map对象
        Map<String, Object> mapToken = JSONObject.parseObject(result, new TypeReference<Map<String, Object>>() {});
        //得到令牌的有效时间（秒）
        long lExpires = Long.parseLong(mapToken.get(EXPIRES_IN).toString());

        //创建一个新的令牌对象
        Map<String, Object> newTokenMap = new HashMap<>();
        //保存令牌
        newTokenMap.put("token", mapToken.get(ACCESS_TOKEN));
        //计算出令牌过期时间并保存,防止意外提前三分钟过期
        newTokenMap.put("validTime", System.currentTimeMillis() + (lExpires - (60 * 3)) * 1000);

        //保存令牌
        myTokens.put(appKey, newTokenMap);

    }


    /**
     * 根据应用Secret生成令牌并返回，并且保存令牌方便下次使用，自动识别令牌是否过期是否需要更新
     *
     * @param appKey 应用Secret
     * @return 返回token
     * @throws IOException
     */
    public synchronized String getToken(String appKey) throws IOException {

        //创建令牌集合对象
        if(myTokens == null){
            myTokens = new HashMap<>();
        }

        //判断令牌是否存在
        if(myTokens.containsKey(appKey)){

            //获取当前时间戳
            long currentTimeMillis = System.currentTimeMillis();

            //获取令牌的过期时间
            long validTime = (long) myTokens.get(appKey).get("validTime");

            //如果令牌已过期
            if(validTime <= currentTimeMillis){

                //更新令牌
                createOrUpdateToken(appKey);

            }

        //如果令牌不存在
        }else{

            //创建令牌
            createOrUpdateToken(appKey);
        }

        //获取令牌并且返回
        return (String) myTokens.get(appKey).get("token");
    }


    /**
     * 有效期失效，获取新的token
     *
     * @param corpid     企业ID
     * @param corpsecret 应用ID
     * @return 返回token
     * @throws IOException
     */
    public synchronized String sendRequestToken(String corpid, String corpsecret) throws IOException {
        String url = TOKENURL + "?corpid=" + corpid + "&corpsecret=" + corpsecret;
        URL postUrl = new URL(url);
        StringBuilder strJson = new StringBuilder();
        OutputStream output = null;
        BufferedReader reader = null;
        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
        try {
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(20000);
            // HTTP Authorization
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // Request input content
            output = connection.getOutputStream();
            output.write("grant_type=client_credentials".getBytes());
            output.flush();
            output.close();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                strJson.insert(0, line);
            }
            reader.close();
            connection.disconnect();
            return strJson.toString();
        } catch (Exception e) {
            System.out.println("get the open api token has error:   " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (output != null) {
                output.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return strJson.toString();
    }
}
