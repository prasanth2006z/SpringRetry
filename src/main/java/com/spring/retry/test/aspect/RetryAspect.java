package com.spring.retry.test.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanExpressionContextAccessor;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import com.spring.retry.test.annot.RetryTest;
import com.spring.retry.test.config.CustomRetryListener;

@Aspect
public class RetryAspect {
  
  @Autowired
  private ApplicationContext applicationContext;
  
  @Autowired
  private CustomRetryListener customRetryListener;
  
  private ProceedingJoinPoint joinPoint=null;
  
  private String responseString;
  
  //not fully generic..need to work on it.
  private Map<String,String> executeSpringExpression(ProceedingJoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method=signature.getMethod();
    RetryTest retryTest = method.getAnnotation(RetryTest.class);
    ExpressionParser parser = new SpelExpressionParser();
    StandardEvaluationContext context = new StandardEvaluationContext();
    context.setRootObject(applicationContext.getAutowireCapableBeanFactory());
    context.setBeanResolver(new BeanFactoryResolver(applicationContext.getAutowireCapableBeanFactory()));
    context.addPropertyAccessor(new BeanExpressionContextAccessor());
    
    Map<String,String> map=new HashMap<String, String>();
    map.put("service", parser.parseExpression(retryTest.service()).getValue(context,String.class));
    map.put("delay", parser.parseExpression(retryTest.delay()).getValue(context,String.class));
    map.put("maxAttempts", parser.parseExpression(retryTest.maxAttempts()).getValue(context,String.class));
    return map;
  }

  @Around("@annotation(retryTest)")
  public Object logExecutionTime(ProceedingJoinPoint jp,RetryTest retryTest) throws Throwable {
    System.out.println("starting..");
    joinPoint=jp;
    
    RetryTemplate retryTemplate=configureRetryTemplate(executeSpringExpression(joinPoint));
    retry(retryTemplate);
    return new String("sss");
  }
  
  public String retry(RetryTemplate retryTemplate) {
    retryTemplate.execute(new RetryCallback<Void, RuntimeException>() {

      public Void doWithRetry(RetryContext context) throws RuntimeException {
        try {
          responseString=(String) joinPoint.proceed();
          if(responseString.contains("NullPointerException")){
            throw new NullPointerException();
          }
        } catch (Throwable e) {
            throw new NullPointerException();
        }
        return null;
      }
    });
    return responseString;
  }

  private RetryTemplate configureRetryTemplate(Map<String,String> map) {
    Map<Class<? extends Throwable>, Boolean> recoverExcpetionMap=new HashMap<Class<? extends Throwable>, Boolean>();
    recoverExcpetionMap.put(NullPointerException.class, true);
    SimpleRetryPolicy retryPolicy=null;
    
    if(map.get("service").equals("ANAV")) {
      retryPolicy = new SimpleRetryPolicy(Integer.parseInt(map.get("maxAttempts")),recoverExcpetionMap);  
    }else {
      retryPolicy = new SimpleRetryPolicy(Integer.parseInt(map.get("maxAttempts")),recoverExcpetionMap);
    }
    FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
    backOffPolicy.setBackOffPeriod(Long.parseLong(map.get("delay")));
    RetryTemplate template = new RetryTemplate();
    template.setRetryPolicy(retryPolicy);
    template.setBackOffPolicy(backOffPolicy);
    template.registerListener(customRetryListener);
    return template;
    
  }
}
