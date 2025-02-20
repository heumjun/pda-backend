package com.denso.pdabackend.aop;

import java.util.Collections;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import lombok.RequiredArgsConstructor;

//@Configuration 어노테이션의경우 @Component와 큰차이는 없다. 
// 하지만 클래스 내부에 1개이상의 Bean을 생성하는 경우는 Configuration 어노테이션을 쓴다.
// 주로 외부라이브러리 또는 내장클래스를 Bean으로 등록할때(개발자가 제어가 불가능한 클래스)
@Configuration

//final 필드의 생성자를 만들어줌
//@Autuwired 사용하지 않고 생성자 주입방법으로 의존성을 주입한다.
@RequiredArgsConstructor
public class TransactionAspect {

    private static final String AOP_TRANSACTION_METHOD_NAME="*";    //모든메소드에 트랙잭션 설정
    private static final String AOP_TRANSACTION_EXPRESSION="execution(* com.denso..service.*Service.*(..))";

    private final TransactionManager transactionManager;

    //직접 TransactionInterceptor와 Advisor를 Bean으로 등록하여 AOP를 구현하기 때문에 @Aspect 어노테이션이 필요하지 않음.
    @Bean
    public TransactionInterceptor transactionAdvice(){
        
        MatchAlwaysTransactionAttributeSource source = new MatchAlwaysTransactionAttributeSource();
        RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();
        //트랜잭션 이름부여
        transactionAttribute.setName(AOP_TRANSACTION_METHOD_NAME);
        //트랜잭션 롤백 설정룰(Exception.class는 예외 최상위 클래스로 모든 예외발생시 롤백)
        transactionAttribute.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        source.setTransactionAttribute(transactionAttribute);

        return new TransactionInterceptor(transactionManager,source);
    }

    @Bean
    public Advisor transactionAdviceAdvisor(){
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_TRANSACTION_EXPRESSION);
        
        return new DefaultPointcutAdvisor(pointcut,transactionAdvice());
    }

}

/**
 * 참조: 위와 같이 AOP를 통한 트랜잭션을 걸고 트랜잭션이 필요없는 코드를 원할경우
 * Service 를 토함하는 클래스명을 쓰지 않거나 
 * @Transactional(noRollbackFor = Exception.class) 어노테이션을 클래스 또는 메소드에 추가한다.(롤백되지 않음)
 */

