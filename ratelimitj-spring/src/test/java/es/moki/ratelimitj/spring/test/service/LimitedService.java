package es.moki.ratelimitj.spring.test.service;

import es.moki.ratelimitj.spring.annotations.RateLimit;
import es.moki.ratelimitj.spring.test.UserPojo;

/**
 * @author Enrico Costanzi
 */
public class LimitedService {

    @RateLimit(value = "defaultLimiter", key = "#param")
    public void limitedMethodOnStringParam(String param){
    }

    @RateLimit(value = "defaultLimiter", key = "#user.name")
    public void limitedMethodOnUserParam(UserPojo user){

    }

    @RateLimit(value = "defaultLimiter", key = "#user.name", methodSignaturePrefix = false)
    public void limitedMethodOnUserParamSimpleKey(UserPojo user){

    }
}
