package com.ngulik.resto_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final StringRedisTemplate stringRedisTemplate;

    public void blacklistToken(String token, long expirationMillis) {
        String key = "blacklist:token:" + token;
        stringRedisTemplate.opsForValue()
                .set(key, "true", Duration.ofMillis(expirationMillis));
    }

    public boolean isTokenBlacklisted(String token) {
        String key = "blacklist:token:" + token;
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }
}
