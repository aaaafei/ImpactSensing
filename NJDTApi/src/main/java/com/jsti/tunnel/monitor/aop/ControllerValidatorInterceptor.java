package com.jsti.tunnel.monitor.aop;

import com.jsti.tunnel.monitor.controller.BaseController;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.jsti.system.err.BusinessException;

@Aspect
@Component
public class ControllerValidatorInterceptor {
	 @Around("execution(* com.jsti.visualinspection.controller..*.*(..)) && args(..,bindingResult)")
     public Object doAround(ProceedingJoinPoint pjp, BindingResult bindingResult) throws Throwable {
         Object retVal;
         if (bindingResult.hasErrors()) {
             throw new BusinessException(20001, BaseController.getValidatorErrors(bindingResult));
         } else {
             retVal = pjp.proceed();
         }
         return retVal;
     }
}
