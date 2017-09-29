package com.spring.retry.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.spring.retry.test.aspect.RetryAspect;
import com.spring.retry.test.service.RetryService;

@Configuration
public class RetryConfiguration {

  @Bean
  public RetryService retryService() {
    return new RetryService();
  }

  @Bean
  public RetryAspect retryAspect() {
    return new RetryAspect();
  }
}
