package org.mongodb.springboot.kitchensinkmordernization.services.implementation;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class TokenBlacklistServiceImpl {
    private final Set<String> tokenBlacklist = ConcurrentHashMap.newKeySet();

    public void blacklistToken(String token) {
        tokenBlacklist.add(token);
    }

    public boolean isBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }
}
