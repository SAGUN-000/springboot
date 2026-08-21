package com.example.Nap.Buyzen.OAuth2;

import com.example.Nap.Buyzen.enums.AuthProviderType;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2Provider {
    AuthProviderType getProviderType();
    String getProviderId(OAuth2User user);
    String getEmail(OAuth2User user);
    String getName(OAuth2User user);
}