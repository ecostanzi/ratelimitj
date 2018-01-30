package es.moki.ratelimitj.spring;

import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import es.moki.ratelimitj.spring.annotations.RateLimit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * Class responsible
 * @author Enrico Costanzi
 */
@Aspect
public class RateLimitjAspect implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Pointcut("@annotation(es.moki.ratelimitj.spring.annotations.RateLimit)")
    public void rateLimitPointcut(){}

    @Before("rateLimitPointcut()")
    public void beforeRequestRate(JoinPoint joinPoint) throws RuntimeException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        RateLimit[] rateLimitAnnotation  = methodSignature.getMethod().getAnnotationsByType(RateLimit.class);

        for (RateLimit rateLimit : rateLimitAnnotation) {
            evaluateRateLimit(methodSignature, joinPoint, rateLimit);
        }

    }

    private void evaluateRateLimit(MethodSignature methodSignature, JoinPoint joinPoint, RateLimit rateLimit){
        String rateLimiterName = rateLimit.value();
        RequestRateLimiter rateLimiter = applicationContext.getBean(rateLimiterName, RequestRateLimiter.class);

        String[] parameterNames = methodSignature.getParameterNames();
        EvaluationContext context = new StandardEvaluationContext();
        Object[] parameters = joinPoint.getArgs();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], parameters[i]);
        }

        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(rateLimit.key());
        String evaluatedSpelExpression = (String) exp.getValue(context);

        String rateLimitKey;
        if(rateLimit.methodSignaturePrefix()){
            rateLimitKey = getMethodKey(methodSignature) + "_" + evaluatedSpelExpression;
        } else {
            rateLimitKey = evaluatedSpelExpression;
        }
        boolean rateLimitExceeded = rateLimiter.overLimitWhenIncremented(rateLimitKey);

        if(rateLimitExceeded){
            throw new RateLimitExceededException("Rate limit exceeded");
        }

    }

    private String getMethodKey(MethodSignature methodSignature){

        Method method = methodSignature.getMethod();
        String methodName = method.getName();
        String className = method.getDeclaringClass().getSimpleName();
        return MessageFormat.format("{0}.{1}", className, methodName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
