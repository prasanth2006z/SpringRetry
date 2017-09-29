package com.spring.retry.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.retry.test.service.RetryService;

@RestController
public class RetryController {

  @Autowired
  private RetryService retryService;
  
  @RequestMapping(value="/")
  public void retry() {
    retryService.setService("ANAV");
    System.out.println("===>"+retryService.testRetry());
    retryService.setService("RULES");
    System.out.println("===>"+retryService.testRetry());
    
  }
}
