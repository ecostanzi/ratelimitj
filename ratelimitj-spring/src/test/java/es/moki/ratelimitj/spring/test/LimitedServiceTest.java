package es.moki.ratelimitj.spring.test;

import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import es.moki.ratelimitj.spring.RateLimitExceededException;
import es.moki.ratelimitj.spring.test.config.RateLimitConfiguration;
import es.moki.ratelimitj.spring.test.service.LimitedService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * @author Enrico Costanzi
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-application.xml")
public class LimitedServiceTest {

    @Autowired
    LimitedService limitedService;

    @Autowired
    RequestRateLimiter requestRateLimiter;

    @Test(timeout = RateLimitConfiguration.MAX_DURATION_MILLISECONDS, expected = RateLimitExceededException.class)
    public void testRateLimitWithStringParam() throws Exception {
        for (int i = 1; i <= RateLimitConfiguration.RATE_LIMIT + 2; i++) {
            limitedService.limitedMethodOnStringParam("hello ratelimitj");
            //check i'm not allowed to do 11 or 12 requests
            assertThat(i).isLessThanOrEqualTo(RateLimitConfiguration.RATE_LIMIT) ;
        }
    }

    @Test
    public void testPojoParamsAreRead(){
        UserPojo userPojo = new UserPojo("Mr.Paul");
        limitedService.limitedMethodOnUserParamSimpleKey(userPojo);
        boolean keyRemoved = requestRateLimiter.resetLimit("Mr.Paul");
        assertThat(keyRemoved).isTrue();
    }

    @Test(timeout = RateLimitConfiguration.MAX_DURATION_MILLISECONDS, expected = RateLimitExceededException.class)
    public void testRateLimitWithObjectParam() throws Exception {
        UserPojo userPojo = new UserPojo("Mr.Jones");
        for (int i = 1; i <= RateLimitConfiguration.RATE_LIMIT + 2; i++) {
            limitedService.limitedMethodOnUserParam(userPojo);
            assertThat(i).isLessThanOrEqualTo(RateLimitConfiguration.RATE_LIMIT);
        }
    }
}
