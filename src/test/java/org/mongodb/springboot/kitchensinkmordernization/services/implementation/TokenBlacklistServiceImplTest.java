package org.mongodb.springboot.kitchensinkmordernization.services.implementation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenBlacklistServiceImplTest {

    private TokenBlacklistServiceImpl tokenBlacklistService;

    @BeforeEach
    void setUp() {
        tokenBlacklistService = new TokenBlacklistServiceImpl();
    }

    @Nested
    class blacklistToken {
        @Test
        void shouldAddTokenToBlacklist() {
            String token = "test-jwt-token-123";

            tokenBlacklistService.blacklistToken(token);

            assertTrue(tokenBlacklistService.isBlacklisted(token));
        }

        @Test
        void shouldHandleMultipleTokens() {
            String token1 = "token-1";
            String token2 = "token-2";
            String token3 = "token-3";

            tokenBlacklistService.blacklistToken(token1);
            tokenBlacklistService.blacklistToken(token2);
            tokenBlacklistService.blacklistToken(token3);

            assertTrue(tokenBlacklistService.isBlacklisted(token1));
            assertTrue(tokenBlacklistService.isBlacklisted(token2));
            assertTrue(tokenBlacklistService.isBlacklisted(token3));
        }

        @Test
        void shouldHandleDuplicateToken() {
            String token = "duplicate-token";

            tokenBlacklistService.blacklistToken(token);
            tokenBlacklistService.blacklistToken(token);

            assertTrue(tokenBlacklistService.isBlacklisted(token));
        }
    }

    @Nested
    class isBlacklisted {
        @Test
        void shouldReturnTrueWhenTokenIsBlacklisted() {
            String token = "test-jwt-token-123";
            tokenBlacklistService.blacklistToken(token);
            Assertions.assertTrue(tokenBlacklistService.isBlacklisted(token));
        }
        @Test
        void shouldReturnFalseWhenTokenIsNotBlacklisted() {
            String token = "test-jwt-token-123";
            Assertions.assertFalse(tokenBlacklistService.isBlacklisted(token));
        }
    }
}