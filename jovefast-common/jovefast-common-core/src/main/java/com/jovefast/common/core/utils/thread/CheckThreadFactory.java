package com.jovefast.common.core.utils.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * 创建线程工厂
 * @author Acechengui
 */
public class CheckThreadFactory implements ThreadFactory
{
  private final String threadGroupName;

  private final AtomicInteger count = new AtomicInteger(0);

  public CheckThreadFactory(String threadGroupName) {
    this.threadGroupName = threadGroupName;
  }

  @Override
  public Thread newThread(Runnable r)
  {
    Thread thread = new Thread(r);
    thread.setName(threadGroupName+"--"+count.addAndGet(1));
    thread.setPriority(5);
    // 设置为守护线程， 默认为主线程
    thread.setDaemon(true);
    return thread;
  }
}