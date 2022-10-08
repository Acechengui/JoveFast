package com.jovefast.common.core.apiinvoker;

public class PushMessageLog {

    /** 密钥 */
    private String accesstoken;

    /** 应用id */
    private Integer agentid;

    /** 用户id列表 */
    private String tousers;

    /** 文件Key */
    private String mediaid;

    /** 推送结果代码 */
    private Integer errorcode;

    @Override
    public String toString() {
        return "PushMessageLog{" +
                "accesstoken='" + accesstoken + '\'' +
                ", agentid=" + agentid +
                ", tousers='" + tousers + '\'' +
                ", mediaid='" + mediaid + '\'' +
                ", errorcode=" + errorcode +
                '}';
    }

    public PushMessageLog() {
    }

    public PushMessageLog(String accesstoken, Integer agentid, String tousers, String mediaid, Integer errorcode) {
        this.accesstoken = accesstoken;
        this.agentid = agentid;
        this.tousers = tousers;
        this.mediaid = mediaid;
        this.errorcode = errorcode;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public Integer getAgentid() {
        return agentid;
    }

    public void setAgentid(Integer agentid) {
        this.agentid = agentid;
    }

    public String getTousers() {
        return tousers;
    }

    public void setTousers(String tousers) {
        this.tousers = tousers;
    }

    public String getMediaid() {
        return mediaid;
    }

    public void setMediaid(String mediaid) {
        this.mediaid = mediaid;
    }

    public Integer getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(Integer errorcode) {
        this.errorcode = errorcode;
    }
}
