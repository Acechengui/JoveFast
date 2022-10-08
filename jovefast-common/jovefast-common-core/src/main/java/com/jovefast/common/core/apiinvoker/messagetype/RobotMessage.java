package com.jovefast.common.core.apiinvoker.messagetype;

import com.alibaba.fastjson2.JSONObject;
import com.jovefast.common.core.apiinvoker.apiinvokerbase.ApiInvokerBase;
import com.jovefast.common.core.apiinvoker.apiinvokerbase.requesttype.RequestType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 机器人消息推送实现类
 */
public class RobotMessage extends ApiInvokerBase {

    //发送机器人消息到群聊会话请求地址
    private static final String GCRT_URL = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send";


    /**
     * 发送机器人Markdown消息
     *
     * @param robotKey 机器人KEY
     * @param content  内容
     * @return 返回错误代码 0-正常
     */
    public static Integer groupChatMarkdownMessageSend(String robotKey, String content) {

        //构造参数
        Map<String, Object> map = new HashMap<>();
        map.put("key", robotKey);

        JSONObject jo = new JSONObject();
        jo.put("msgtype", "markdown");

        JSONObject textJson = new JSONObject();
        textJson.put("content", content);

        jo.put("markdown", textJson);

        //调用API并返回错误代码
        return myApiInvoker(GCRT_URL, RequestType.POST, map, jo)
                .path("errcode");
    }


    /**
     * 发送机器人文本消息
     *
     * @param robotKey  机器人KEY
     * @param content   内容
     * @param toUserIds 成员userid
     * @return 返回错误代码 0-正常
     */
    public static Integer groupChatTextMessageSend(String robotKey, String content, String toUserIds) {

        //构造参数
        Map<String, Object> map = new HashMap<>();
        map.put("key", robotKey);

        JSONObject jo = new JSONObject();
        jo.put("msgtype", "text");

        JSONObject textJson = new JSONObject();
        textJson.put("content", content);
        textJson.put("mentioned_list", toUserIds);

        jo.put("text", textJson);

        //调用API并返回错误代码
        return myApiInvoker(GCRT_URL, RequestType.POST, map, jo)
                .path("errcode");
    }

    /**
     * 发送机器人文本消息
     *
     * @param robotKey  机器人KEY
     * @param content   内容
     * @param toUserIds 成员userid
     * @return 返回错误代码 0-正常
     */
    public static Integer groupChatTextMessageSend(String robotKey, String content, List<String> toUserIds) {
        return groupChatTextMessageSend(robotKey, content, String.join("|", toUserIds));
    }
}
