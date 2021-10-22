package com.example.aop;

import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/*
 AOP는 controller는 제어 못함. controller는 인터셉터로 제어할 수 있음.
 * 
 AOP(Aspect Oriented Programming : 관점 지향 프로그래밍)
 - 관점 : 주변부 로직에 대한 관심사를 의미함
 - 주변부 로직(공통 기능)을 클래스단위로 주요 로직과 완전히 분리해서 개발하는 방식.
 - 스프링은 AOP 기능을 타겟 메소드 호출 기준으로 주요 로직에 주변부 로직을 결합시켜줌.
 
 ㅁ Advice : 주변부 로직(공통기능)을 가진 클래스. (주변부 로직 + 적용될 대상 메소드)
 ㅁ Target : 주요 로직을 가지고 있는, 주변부 로직이 적용될 Target Object.
 ㅁ Join Point : 주변부 로직이 적용될(조인될) 수 있는 메소드 후보들.
 ㅁ Point Cut : 조인포인트(후보)들 중에 실제 결합시킬 메소드를 의미함.
 */

// Aspect는 별도의 모듈이라서 빈을 만들어주지는 않아 따로 애노테이션 해야함
// Root context에 컴포넌트 스캔 등록, 활성화 시켜야함
@Aspect // 주변부 로직을 가진 어드바이스 클래스
@Component
public class LogAdvice {
	
	// AOP 애노테이션 값으로 포인트컷 표현식으로 타겟 오브젝트의 포인트컷을 표현함
	// 어디에 적용할 지 애노테이션으로 지정 , (결합될 메소드)보다 먼저 시작
	// 반환 타입 적어줘야함. 상관없으면 *로 표기
	// 오버라이딩으로 인해 매개변수 타입도 적어줘야한다
	// 매개변수 상관없이 (..)로 표현
	// 메소드도 안따지면 *로 표기
	// 서비스 패키지 전체에 쓰고 싶으면 클래스도 *로 표기하면 됨
	@Before("execution( * com.example.service.MemberService.*(..) )")
	public void logBefore() {
		System.out.println("====================logBefore()======================");
		
		
	} //logBefore
	
	// Arround가 전체를 아우르기 때문에 Before 보다 먼저 호출됨.
	// Around 사용 시에는 반드시 매개변수를 ProceedingJoinPoint 타입을 선언하고
	// 리턴타입은 Object 가 되어야 함
	@Around("execution( * com.example.service.MemberService.*(..) )")
	public Object logTime(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println("====================logTime()======================");
		
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		String methodName = methodSignature.getName(); // 타깃 메소드 이름정보
		
		Object[] args = joinPoint.getArgs();// 타깃 메소드의 매개변수 정보
		List<Object> argsList = Arrays.asList(args);
		
		System.out.println("메소드 명 : " + methodName + ",\n 매개변수 : " + argsList);
		
		long beginTime = System.currentTimeMillis();
		
		
		// 메소드 호출 기준 앞으로는 @before
		Object result =  joinPoint.proceed(); // 실제 해당하는 타깃 메소드를 호출함.
		
		// 타깃 메소드가 리턴값을 가지고 있으면 Object로 반환
		System.out.println("@Around result : " + result);
		
		long endTime = System.currentTimeMillis();
		
		long diffTime = endTime - beginTime;
		
		System.out.println(methodName + " 메소드의 실행시간 : " + diffTime + "ms");
		
		// 타깃메소드에 리턴값이 있으면 리턴 해줘야함.
		return result;
	} //logTime
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
