package com.jovefast.common.core.qiwx;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Description SpringCloud+Nacos项目中动态刷新配置
 * @Author Acechengui
 */
@Component
@RefreshScope
public class NotificationRefresh {

    @Value("${qywx.corpid}")
    private String corpid;
    @Value("${qywx.agentid}")
    private Integer agentid;
    @Value("${qywx.corpsecret}")
    private String corpsecret;
    @Value("${qywx.target}")
    private String target;

    public String getTarget() {
        return target;
    }

    public void setTargets(String target) {
        this.target = target;
    }

    public String getCorpid() {
        return corpid;
    }

    public void setCorpid(String corpid) {
        this.corpid = corpid;
    }

    public Integer getAgentid() {
        return agentid;
    }

    public void setAgentid(Integer agentid) {
        this.agentid = agentid;
    }

    public String getCorpsecret() {
        return corpsecret;
    }

    public void setCorpsecret(String corpsecret) {
        this.corpsecret = corpsecret;
    }
}
