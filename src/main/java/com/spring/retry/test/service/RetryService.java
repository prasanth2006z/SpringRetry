package com.spring.retry.test.service;

import org.springframework.stereotype.Component;

import com.spring.retry.test.annot.RetryTest;

@Component
public class RetryService {

  private String service;

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  @RetryTest(value = "@retryService.getService()")
  public String testRetry() {
    if(getService().equals("ANAV")) {
      return "test";
    }else {
      return "NullPointerException";  
    }
    
  }

}
