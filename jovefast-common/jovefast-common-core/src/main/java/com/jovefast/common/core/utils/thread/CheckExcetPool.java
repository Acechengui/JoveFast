package com.jovefast.common.core.utils.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * ThreadPoolExecutor
 * <p>
 * ThreadPoolExecutor线程池，java提供开发框架，管理线程的创建、销毁、优化、监控等。
 * <p>
 * 有4种不同的任务队列:
 * <p>
 * 1.ArrayBlockingQueue：基于数组结构的任务队列。此队列按先进先出的原则对任务进行排序。
 * 2.LinkedBlockingQueue：基于链表结构的任务队列。此队列也是按先进先出的原则对任务进行排序。但性能比ArrayBlockingQueue高。
 * 3.synchronousQueue：不存储元素的任务队列。每个插入操作必须等到另一个线程调用移除操作，否则插入操作一直处于阻塞状态。
 * 4.PriorityBlockingQueue：具有优先级的任务队列。此队列中的元素必须能够比较。
 * 拒绝策略：
 * <p>
 * RejectedExecutionHandler（饱和策略 ）：当线程池中的线程数大于maximumPoolSize时，线程池就不能在处理任何任务了，这时线程池会抛出异常。原因就是这个策略默认情况下是AbortPolicy：表示无法处理新任务时抛出异常。
 * <p>
 * 1.AbortPolicy：直接抛出异常。
 * 2.CallerRunsPolicy：只用调用者所在线程来运行任务。
 * 3.DiscardOldestPolicy：丢弃队列里最近的一个任务，并执行当前任务
 * 4.DiscardPolicy：不处理，丢弃掉。
 * 自定义：
 * ThreadPoolExecutor.AbortPolicy()
 * //抛出java.util.concurrent.RejectedExecutionException异常
 * ThreadPoolExecutor.CallerRunsPolicy()
 * //重试添加当前的任务，他会自动重复调用execute()方法
 * ThreadPoolExecutor.DiscardOldestPolicy()
 * //抛弃旧的任务
 * ThreadPoolExecutor.DiscardPolicy()
 * 线程工具类
 * @author Acechengui
 */
@SuppressWarnings("unchecked")
public class CheckExcetPool
{
  private static final Logger log = LoggerFactory.getLogger(CheckExcetPool.class);
  /**
   * 线程池核心线程数
   */
  private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 5;
  /**
   * 最大线程数
   */
  private static final int MAXIMUM_POOL_SIZE = CORE_POOL_SIZE > 255 ? 255 : CORE_POOL_SIZE * 2;
  /**
   * 线程池中除了核心线程，其他线程的最大存活时间
   */
  private static final int KEEP_ALIVE_TIME = 60;
  /**
   * 时间单位
   */
  private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
  /**
   * 线程等待队列
   */
  private static final BlockingQueue QUEUE = new LinkedBlockingQueue();
  /**
   * 创建线程的工厂
   */
  private static final CheckThreadFactory CHECK_THREAD_FACTORY = new CheckThreadFactory("checkGroup");
  /*h 拒绝策略 当提交任务数超过maxmumPoolSize+workQueue之和时,
    *    即当提交第41个任务时(前面线程都没有执行完,此测试方法中用sleep(100)),
     *         任务会交给RejectedExecutionHandler来处理
  andler的拒绝策略：
  有四种：第一种AbortPolicy:不执行新任务，直接抛出异常，提示线程池已满
  第二种DisCardPolicy:不执行新任务，也不抛出异常
  第三种DisCardOldSetPolicy:将消息队列中的第一个任务替换为当前新进来的任务执行
  第四种CallerRunsPolicy:直接调用execute来执行当前任务*/

  private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
      CORE_POOL_SIZE,
      MAXIMUM_POOL_SIZE,
      KEEP_ALIVE_TIME,
      TIME_UNIT,
      QUEUE,
      CHECK_THREAD_FACTORY
  );

  public static void submit( Runnable runnable)
  {
    log.info(CORE_POOL_SIZE+"::"+QUEUE.size());
    THREAD_POOL_EXECUTOR.submit(runnable);
  }
  public static <T> Future submit(Callable<T> callable)
  {
    return THREAD_POOL_EXECUTOR.submit(callable);
  }

  public static <T> void excutor( Runnable run, T result )
  {
    THREAD_POOL_EXECUTOR.submit( run,result );
  }
  public static void excutor( Runnable run)
  {
    THREAD_POOL_EXECUTOR.execute( run);
  }

  private static class RecjectThreadHandler implements RejectedExecutionHandler
  {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
    }

    // 异常记录
    private void doLog(Runnable r, ThreadPoolExecutor executor)
    {
      log.error(r.toString()+"excutor failed."+executor.getCompletedTaskCount());
    }
  }
}