package com.example.springboot3.retry;

import com.example.springboot3.exception.ConnectException;
import com.example.springboot3.exception.SystemException;

import java.util.function.Predicate;

public class RetryPolicy<T> {
    private static final int DEFAULT_MAX_RETRIES = 3;
    private static final long DEFAULT_DELAY = 1000;

    private int maxRetry;
    private long delay;
    private Predicate<T> retryCondition;

    public RetryPolicy() {
        this.maxRetry = DEFAULT_MAX_RETRIES;
        this.delay = DEFAULT_DELAY;
        this.retryCondition = null;
    }

    public RetryPolicy(int maxRetry, long delay, Predicate<T> retryCondition) {
        this.maxRetry = maxRetry;
        this.delay = delay;
        this.retryCondition = retryCondition;
    }

    public static <T> RetryPolicy<T> create() {
        return new RetryPolicy<>();
    }

    public RetryPolicy<T> withMaxRetry(int maxRetry) {
        this.maxRetry = maxRetry;
        return this;
    }

    public RetryPolicy<T> withDelay(long delay) {
        this.delay = delay;
        return this;
    }

    public RetryPolicy<T> withRetryCondition(Predicate<T> retryCondition) {
        this.retryCondition = retryCondition;
        return this;
    }

    public int getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    public void execute(Runnable runnable) throws InterruptedException {
        for (int i = 1; i <= maxRetry; i++) {
            try {
                System.out.println("第" + i + "次重试");
                runnable.run();
                return;
            } catch (Exception e) {
                if (retryCondition != null && !retryCondition.test((T) e)) {
                    throw e;
                }
                Thread.sleep(delay);
                if (i == maxRetry) {
                    throw new ConnectException(500, "服务连接超时,请稍后再试或者联系管理员");
                }
            }
        }
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public Predicate<T> getRetryCondition() {
        return retryCondition;
    }

    public void setRetryCondition(Predicate<T> retryCondition) {
        this.retryCondition = retryCondition;
    }
}
