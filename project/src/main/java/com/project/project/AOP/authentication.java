package com.project.project.AOP;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpSession;

@Component
@Aspect
public class authentication {

 
  
   @Autowired
    private HttpSession httpSession;

    @Around("execution(* com.project.project.controller.adminController.*(..)) || " +
    "execution(* com.project.project.controller.productcontrollerv1.getAll(..)) || " +
    "execution(* com.project.project.controller.productcontrollerv1.addProduct(..)) || " +
    "execution(* com.project.project.controller.productcontrollerv1.showEditForm(..))")
    public Object authenticateAdmin(ProceedingJoinPoint joinPoint) throws Throwable {
        Object userRole = httpSession.getAttribute("userrole");
        if (userRole == null || !"a".equals(userRole.toString())) {
             return new ModelAndView("/index");
        } else {
             return joinPoint.proceed();
        }
    }
}