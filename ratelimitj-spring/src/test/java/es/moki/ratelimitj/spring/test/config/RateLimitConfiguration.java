package es.moki.ratelimitj.spring.test.config;

import es.moki.ratelimitj.core.limiter.request.RequestLimitRule;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;

import es.moki.ratelimitj.inmemory.request.InMemorySlidingWindowRequestRateLimiter;
import es.moki.ratelimitj.spring.test.service.LimitedService;
import es.moki.ratelimitj.spring.RateLimitjAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author Enrico Costanzi
 */
@Configuration
public class RateLimitConfiguration {

    public static final int MAX_DURATION_MILLISECONDS = 1000;
    public static final int RATE_LIMIT = 10;

    @Bean
    public RequestLimitRule rule() {
        return RequestLimitRule.of(MAX_DURATION_MILLISECONDS, TimeUnit.MILLISECONDS, RATE_LIMIT);
    }

    @Bean(name = "defaultLimiter")
    public RequestRateLimiter requestRateLimiter(@Autowired RequestLimitRule requestLimitRule){
        return new InMemorySlidingWindowRequestRateLimiter(Collections.singleton(requestLimitRule));
    }

    @Bean
    public LimitedService limitedService() {
        return new LimitedService();
    }

    @Bean
    public RateLimitjAspect ratelimitjAspect(ApplicationContext applicationContext){
        RateLimitjAspect ratelimitjAspect = new RateLimitjAspect();
        ratelimitjAspect.setApplicationContext(applicationContext);
        return ratelimitjAspect;
    }

}
