package es.moki.ratelimitj.spring.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Enrico Costanzi
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimit {

    /**
     * The name of the {@link es.moki.ratelimitj.core.limiter.request.RequestRateLimiter} bean
     */
    String value();

    /**
     * The suffix to be used as key in the rate limiter map. Can be a SpEL expression.
     */
    String key();

    /**
     * True if the method signature has to the prefix for the rate limiter map key. False otherwise.
     */
    boolean methodSignaturePrefix() default true;
}
