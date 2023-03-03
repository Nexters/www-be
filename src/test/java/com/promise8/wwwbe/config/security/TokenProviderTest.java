package com.promise8.wwwbe.config.security;

import com.promise8.wwwbe.v1.config.security.TokenProvider;
import com.promise8.wwwbe.v1.config.security.UserPrincipal;
import com.promise8.wwwbe.v1.model.entity.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@ExtendWith({MockitoExtension.class})
class TokenProviderTest {

    @InjectMocks
    TokenProvider tokenProvider;

    @Test
    @DisplayName("token 생성 및 validate 동작 확인")
    void test() {
        UserEntity mockUser = UserEntity.builder()
                .deviceId("abc")
                .build();
        UserPrincipal userPrincipal = UserPrincipal.create(mockUser);

        String token = tokenProvider.createToken(createMockAuthentication(userPrincipal));
        boolean validated = tokenProvider.validateToken(token);

        Assertions.assertTrue(validated);
        System.out.println(token);
    }

    private Authentication createMockAuthentication(UserPrincipal userPrincipal) {
        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return userPrincipal;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return null;
            }
        };
    }

    @Test
    void getUserIdFromToken() {
    }

    @Test
    void validateToken() {
    }
}