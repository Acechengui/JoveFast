package com.jovefast.modules.monitor.service;

import com.jovefast.common.core.qiwx.NotificationRefresh;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.impl.WxCpMessageServiceImpl;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.article.NewArticle;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;;


@Service
public class QywxMonitorService {

    private final Logger log = LoggerFactory.getLogger(QywxMonitorService.class);

    @Autowired
    private NotificationRefresh notificationRefresh;


    private  WxCpDefaultConfigImpl init(){
        WxCpDefaultConfigImpl config = new WxCpDefaultConfigImpl();
        // 设置微信企业号的appid
        config.setCorpId(notificationRefresh.getCorpid());
        // 设置微信企业号的app corpSecret
        config.setCorpSecret(notificationRefresh.getCorpsecret());
        // 设置微信企业号应用ID
        config.setAgentId(notificationRefresh.getAgentid());
        return config;
    }
    /**
     * 指定人发送文本消息
     * @param user
     * @param content
     * @throws WxErrorException
     */
    public void push(String user, String content) throws WxErrorException {
        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(init());
        WxCpMessageServiceImpl wxCpMessageService = new WxCpMessageServiceImpl(wxCpService);
/*        WxCpMessage wxCpMessage = WxCpMessage
                .TEXT()
                .agentId(AGENTID)
                .toUser("非必填，UserID列表（消息接收者，多个接收者用‘|’分隔）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送")
                .toParty("非必填，PartyID列表，多个接受者用‘|’分隔。当touser为@all时忽略本参数")
                .toTag("非必填，TagID列表，多个接受者用‘|’分隔。当touser为@all时忽略本参数")
                .content("sfsfdsdf")
                .build();*/
        WxCpMessage wxCpMessage = WxCpMessage
                .TEXT()
                .agentId(notificationRefresh.getAgentid())
                .toUser(user)
                .content(content)
                .build();
        wxCpMessageService.send(wxCpMessage);
        log.info("文本消息发送完毕,发送内容:{}",content);
    }

    //图片消息
    public void pictureMessage(String user, String content) throws WxErrorException {
        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(init());
        WxCpMessageServiceImpl wxCpMessageService = new WxCpMessageServiceImpl(wxCpService);
        WxCpMessage wxCpMessage =WxCpMessage.IMAGE()
                .agentId(notificationRefresh.getAgentid())
                .toUser("非必填，UserID列表（消息接收者，多个接收者用‘|’分隔）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送")
                .toParty("非必填，PartyID列表，多个接受者用‘|’分隔。当touser为@all时忽略本参数")
                .toTag("非必填，TagID列表，多个接受者用‘|’分隔。当touser为@all时忽略本参数")
                .mediaId("MEDIA_ID")
                .build();
        wxCpMessageService.send(wxCpMessage);
    }

    //语言消息
    public void voiceMessage(String user, String content) throws WxErrorException {
        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(init());
        WxCpMessageServiceImpl wxCpMessageService = new WxCpMessageServiceImpl(wxCpService);
        WxCpMessage wxCpMessage =WxCpMessage.VOICE().agentId(notificationRefresh.getAgentid())
                .toUser("非必填，UserID列表（消息接收者，多个接收者用‘|’分隔）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送")
                .toParty("非必填，PartyID列表，多个接受者用‘|’分隔。当touser为@all时忽略本参数")
                .toTag("非必填，TagID列表，多个接受者用‘|’分隔。当touser为@all时忽略本参数")
                .mediaId("MEDIA_ID")
                .build();
        wxCpMessageService.send(wxCpMessage);
    }

    //视频消息
    public void videoMessage(String user, String content) throws WxErrorException {
        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(init());
        WxCpMessageServiceImpl wxCpMessageService = new WxCpMessageServiceImpl(wxCpService);
        WxCpMessage wxCpMessage =WxCpMessage.VIDEO().agentId(notificationRefresh.getAgentid())
                .toUser("非必填，UserID列表（消息接收者，多个接收者用‘|’分隔）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送")
                .toParty("非必填，PartyID列表，多个接受者用‘|’分隔。当touser为@all时忽略本参数")
                .toTag("非必填，TagID列表，多个接受者用‘|’分隔。当touser为@all时忽略本参数")
                .title("TITLE")
                .mediaId("MEDIA_ID")
                .thumbMediaId("MEDIA_ID")
                .description("DESCRIPTION")
                .build();
        wxCpMessageService.send(wxCpMessage);
    }

    //图文消息
    public void graphicMessage(String user, String content) throws WxErrorException {
        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(init());
        WxCpMessageServiceImpl wxCpMessageService = new WxCpMessageServiceImpl(wxCpService);
        NewArticle article1 = new NewArticle();
        article1.setUrl("URL");
        article1.setPicUrl("PIC_URL");
        article1.setDescription("Is Really A Happy Day");
        article1.setTitle("Happy Day");

        NewArticle article2 = new NewArticle();
        article2.setUrl("URL");
        article2.setPicUrl("PIC_URL");
        article2.setDescription("Is Really A Happy Day");
        article2.setTitle("Happy Day");

        WxCpMessage wxCpMessage =WxCpMessage.NEWS().agentId(notificationRefresh.getAgentid())
                .toUser("非必填，UserID列表（消息接收者，多个接收者用‘|’分隔）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送")
                .toParty("非必填，PartyID列表，多个接受者用‘|’分隔。当touser为@all时忽略本参数")
                .toTag("非必填，TagID列表，多个接受者用‘|’分隔。当touser为@all时忽略本参数")
                .addArticle(article1)
                .addArticle(article2)
                .build();
        wxCpMessageService.send(wxCpMessage);
    }


}