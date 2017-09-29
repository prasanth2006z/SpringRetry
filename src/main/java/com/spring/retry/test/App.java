package com.spring.retry.test;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Spring Retry App
 *
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class App {

  private static final Logger LOGGER = LogManager.getLogger(App.class);

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
    LOGGER.info(context);
  }
}
