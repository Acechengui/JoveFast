package com.jovefast.job.service;

/**
 * 分层分级过数锁定
 */
public interface ILockMessageService {

    /**
     * 过数锁定超时报警
     */
    void theLockTimeoutPush();

    /**
     * 过数锁定消息推送
     */
    void hierarchicalClassificationLockPush();
}
