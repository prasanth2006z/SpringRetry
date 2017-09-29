package com.spring.retry.test.config;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class CustomRetryListener extends RetryListenerSupport {

  @Override
  public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback,
    Throwable t) {
    if (t != null && t instanceof NullPointerException) {
      System.out.println("====>" + context.getRetryCount());
      System.out.println("Retrying..");
    }
  }
}
