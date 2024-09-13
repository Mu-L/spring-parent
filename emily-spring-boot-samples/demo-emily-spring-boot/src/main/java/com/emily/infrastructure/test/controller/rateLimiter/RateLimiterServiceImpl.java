package com.emily.infrastructure.test.controller.rateLimiter;

import com.emily.infrastructure.rateLimiter.annotation.RateLimiter;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author :  Emily
 * @since :  2024/8/30 上午11:12
 */
@Service
public class RateLimiterServiceImpl implements RateLimiterService {
    @Override
    @RateLimiter(key = "SDK:limiter:%s:%s", timeout = 1, timeunit = TimeUnit.MINUTES)
    public void rateLimiter(String key1, String key2) {
        System.out.println("---------------");
    }
}
