package com.example.Nap.Buyzen.OAuth2;


import com.example.Nap.Buyzen.enums.AuthProviderType;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;


@Component
public class GoogleOAuth2Provider implements OAuth2Provider {

    @Override
    public AuthProviderType getProviderType() {
        return AuthProviderType.GOOGLE;
    }

    @Override
    public String getProviderId(OAuth2User user) {
        return getRequiredAttribute(user, "sub");
    }

    @Override
    public String getEmail(OAuth2User user) {
        return getRequiredAttribute(user, "email");
    }

    @Override
    public String getName(OAuth2User user) {
        return getRequiredAttribute(user, "name");
    }

    private String getRequiredAttribute(OAuth2User user, String attribute) {
        String value = user.getAttribute(attribute);

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Missing required Google OAuth2 attribute: " + attribute
            );
        }

        return value;
    }
}
