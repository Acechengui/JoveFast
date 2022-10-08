package com.jovefast.job.domain;

/**
 * 分层分级过数锁定
 */
public class LockMessage {

    private Integer rownumber;           //序号
    private Integer rkey;               //id
    private String wo;                  //工卡号
    private String lot;                  //lot
    private String mfg;                  //型号
    private String lockreason;          //锁定原因
    private String lock_date;           //锁定时间
    private String judge;               //触发原因
    private String timeout;             //从锁定开始距离当前多少个小时

    public Integer getRownumber() {
        return rownumber;
    }

    public void setRownumber(Integer rownumber) {
        this.rownumber = rownumber;
    }

    public Integer getRkey() {
        return rkey;
    }

    public void setRkey(Integer rkey) {
        this.rkey = rkey;
    }

    public String getWo() {
        return wo;
    }

    public void setWo(String wo) {
        this.wo = wo.trim();
    }

    public String getLockreason() {
        return lockreason;
    }

    public void setLockreason(String lockreason) {
        this.lockreason = lockreason.trim();
    }

    public String getLock_date() {
        return lock_date;
    }

    public void setLock_date(String lock_date) {
        this.lock_date = lock_date.trim();
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = "0".equals(judge) ? "分层分级" : "自主预警";
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot.trim();
    }

    public String getMfg() {
        return mfg;
    }

    public void setMfg(String mfg) {
        this.mfg = mfg.trim();
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "LockMessage{" +
                "rownumber=" + rownumber +
                ", rkey=" + rkey +
                ", wo='" + wo + '\'' +
                ", lot='" + lot + '\'' +
                ", mfg='" + mfg + '\'' +
                ", lockreason='" + lockreason + '\'' +
                ", lock_date='" + lock_date + '\'' +
                ", judge='" + judge + '\'' +
                ", timeout='" + timeout + '\'' +
                '}';
    }
}
