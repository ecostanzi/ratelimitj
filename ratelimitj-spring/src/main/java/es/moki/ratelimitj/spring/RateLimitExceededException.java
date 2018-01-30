package es.moki.ratelimitj.spring;

/**
 * Exception to be thrown by the aspect method when a rate limiter has exceeded
 *
 * @author Enrico Costanzi
 */
public class RateLimitExceededException extends RuntimeException {

    public RateLimitExceededException(String message) {
        super(message);
    }
}
