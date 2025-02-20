package com.denso.pdabackend.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggerAspect {
    private Logger log = LoggerFactory.getLogger(this.getClass());
	
	//pointcut 표현식 
	// * : 모든 리턴타입, com.denso..controller: com.denso패키지안에 모든 controller패키지
	// *Controller: 이름이 Controller로 끝나는 모든 클래스, *(..) : 모든 메소드
	// com.denso..controller.*Controller.*(..) =>denso내부 모든 controller패키지의 Controller로 끝나는 모든class의 모든 메소드
    @Around("execution(* com.denso..controller.*Controller.*(..)) or execution(* com.denso..service.*Service.*(..)) or execution(* com.denso..mapper.*Mapper.*(..)) ")
	public Object logPrint(ProceedingJoinPoint joinPoint) throws Throwable{
		String type = "";
		String name = joinPoint.getSignature().getDeclaringTypeName();	//class 명
		if(name.indexOf("Controller")>-1) {
			type="Controller \t:	";
		}
		else if(name.indexOf("Service")> -1) {
			type = "Service	\t:	";
		}
		else if(name.indexOf("Mapper") > -1) {
			type = "Mapper	\t:	";
		}
		log.debug(type + name + "."+ joinPoint.getSignature().getName()+"()");  //controller:클래스명.메소드명
		
		return joinPoint.proceed();
		
	}
    
}
