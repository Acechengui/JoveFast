package com.jovefast.common.core.apiinvoker.messagetype;

import com.alibaba.fastjson2.JSONObject;
import com.jovefast.common.core.apiinvoker.PushMessageLog;
import com.jovefast.common.core.apiinvoker.apiinvokerbase.requesttype.RequestType;
import com.jovefast.common.core.apiinvoker.apiinvokerbase.ApiInvokerBase;

import java.util.HashMap;
import java.util.Map;

/**
 * 应用消息推送实现类
 */
public class ApplicationMessage extends ApiInvokerBase {

    //发送应用消息到群聊会话请求地址
    private static final String GCAT_URL = "https://qyapi.weixin.qq.com/cgi-bin/appchat/send";

    //发送应用信息给指定用户或者用户列表
    private static final String MS_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send";

    /**
     * 发送应用Markdown消息给指定用户
     *
     * @param accessToken 密钥
     * @param agentid     应用id
     * @param toUser      用户id，多个使用|分开
     * @param content     内容
     * @return 返回错误代码 0-正常
     */
    public static Integer markdownMessageSend(String accessToken, int agentid, String toUser, String content) {

        //构造参数
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", accessToken);

        JSONObject jo = new JSONObject();
        jo.put("touser", toUser);
        jo.put("agentid", agentid);
        jo.put("msgtype", "markdown");

        JSONObject textJson = new JSONObject();
        textJson.put("content", content);

        jo.put("markdown", textJson);

        //调用API并返回错误代码
        return myApiInvoker(MS_URL, RequestType.POST, map, jo)
                .path("errcode");
    }

    /**
     * 发送应用Markdown消息到群聊会话
     *
     * @param accessToken 密钥
     * @param chatid      群聊id
     * @param content     内容
     * @return 返回错误代码 0-正常
     */
    public static Integer groupChatMarkdownMessageSend(String accessToken, String chatid, String content) {

        //构造参数
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", accessToken);

        JSONObject jo = new JSONObject();
        jo.put("chatid", chatid);
        jo.put("msgtype", "markdown");

        JSONObject textJson = new JSONObject();
        textJson.put("content", content);

        jo.put("markdown", textJson);

        //调用API并返回错误代码
        return myApiInvoker(GCAT_URL, RequestType.POST, map, jo)
                .path("errcode");
    }


    /**
     * 发送应用文本消息到群聊会话
     *
     * @param accessToken 密钥
     * @param chatid      群聊id
     * @param content     内容
     * @param safe        是否保密 0表示否，1表示是，默认0
     * @return 返回错误代码 0-正常
     */
    public static Integer groupChatTextMessageSend(String accessToken, String chatid, String content, int safe) {

        //构造参数
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", accessToken);

        JSONObject jo = new JSONObject();
        jo.put("chatid", chatid);
        jo.put("msgtype", "text");

        JSONObject textJson = new JSONObject();
        textJson.put("content", content);

        jo.put("text", textJson);
        jo.put("safe", safe);

        //调用API并返回错误代码
        return myApiInvoker(GCAT_URL, RequestType.POST, map, jo)
                .path("errcode");
    }


    /**
     * 发送应用文本消息到群聊会话
     *
     * @param accessToken 密钥
     * @param chatid      群聊id
     * @param text        内容
     * @return 返回错误代码 0-正常
     */
    public static Integer groupChatTextMessageSend(String accessToken, String chatid, String text) {
        return groupChatTextMessageSend(accessToken, chatid, text, 0);
    }

    /**
     * 发送应用文本消息到指定用户集
     *
     * @param accessToken 密钥
     * @param agentid     应用id
     * @param toUser      用户id，多个使用|分开
     * @param mediaId     文件Key(上传上传后和获得)
     * @param safe        是否保密 0表示否，1表示是，默认0
     * @return
     */
    public static PushMessageLog fileMessageSend(String accessToken, int agentid, String toUser, String mediaId, int safe) {
        //构造参数
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", accessToken);

        JSONObject jo = new JSONObject();
        jo.put("touser",toUser);
        jo.put("msgtype","file");
        jo.put("agentid",agentid);

        JSONObject file = new JSONObject();
        file.put("media_id",mediaId);

        jo.put("file",file);
        jo.put("safe",safe);

        //调用API并返回错误代码
        return new PushMessageLog(accessToken, agentid, toUser, mediaId, myApiInvoker(MS_URL, RequestType.POST, map, jo).path("errcode"));
    }

    /**
     * 发送应用文本消息到指定用户集
     *
     * @param accessToken 密钥
     * @param agentid     应用id
     * @param toUser      用户id，多个使用|分开
     * @param mediaId     文件Key(上传上传后和获得)
     * @return
     */
    public static PushMessageLog fileMessageSend(String accessToken, int agentid, String toUser, String mediaId){
        return fileMessageSend(accessToken,agentid,toUser,mediaId,0);
    }
}
