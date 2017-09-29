package com.spring.retry.test.service;

import org.springframework.stereotype.Component;

import com.spring.retry.test.annot.RetryTest;

@Component
public class RetryService {

  private String service;
  private String delay;
  private String maxAttempts;

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }
  
  public String getDelay() {
    return delay;
  }

  public void setDelay(String delay) {
    this.delay = delay;
  }

  public String getMaxAttempts() {
    return maxAttempts;
  }

  public void setMaxAttempts(String maxAttempts) {
    this.maxAttempts = maxAttempts;
  }

  @RetryTest(service = "@retryService.getService()", maxAttempts="@retryService.getMaxAttempts()",delay="@retryService.getDelay()" )
  public String testRetry() {
    if(getService().equals("ANAV")) {
      return "test";
    }else {
      return "NullPointerException";  
    }
    
  }

}
