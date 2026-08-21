package com.example.Nap.Buyzen.service;

import com.example.Nap.Buyzen.OAuth2.OAuth2Provider;
import com.example.Nap.Buyzen.dto.LoginResponseDto;
import com.example.Nap.Buyzen.entities.User;
import com.example.Nap.Buyzen.enums.AuthProviderType;
import com.example.Nap.Buyzen.enums.Role;
import com.example.Nap.Buyzen.repository.UserRepo;
import com.example.Nap.Buyzen.security.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final UserRepo userRepo;
    private final AuthUtil authUtil;
    private final List<OAuth2Provider> providers;
    private final MailService mailService;

    public LoginResponseDto handleOauth2LoginRequest(
            OAuth2User oAuth2User,
            String registrationId
    ) {



        // 1. Convert Spring's registration ID ("google")
        //    into our application's AuthProviderType enum (GOOGLE).
        AuthProviderType providerType =
                authUtil.getProviderTypeFromRegistrationId(registrationId);

        OAuth2Provider provider = providers.stream()
                .filter(p -> p.getProviderType()==(providerType))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Unsupported OAuth2 provider: " + registrationId
                        ));



        // 2. Extract the provider-specific user ID.
        //    For Google, this is the "sub" attribute.
        String providerId =
                provider.getProviderId(oAuth2User);

        // 3. Check whether this OAuth identity is already linked
        //    to a user in our database.
        Optional<User> existingProviderUser =
                userRepo.findByProviderTypeAndProviderId(
                        providerType,
                        providerId
                );

        User user;

        if (existingProviderUser.isPresent()) {

            // 4a. OAuth identity already exists.
            //     This is an existing OAuth user, so use that account.
            user = existingProviderUser.get();

        } else {

            // 4b. This OAuth identity has never been seen before.
            //     Extract the user's email from the OAuth provider.
            String email =
                    provider.getEmail(oAuth2User);

            // 5. Check whether an account with this email already exists.
            User existingEmailUser =
                    userRepo.findByEmail(email).orElse(null);

            if (existingEmailUser != null) {

                // 6. An account with this email already exists.
                //    Link this OAuth provider identity to that account.
                existingEmailUser.setProviderType(providerType);
                existingEmailUser.setProviderId(providerId);

                user = userRepo.save(existingEmailUser);

            } else {

                // 7. No account exists with this email.
                //    Create a new local user from the OAuth information.
                String name =
                        provider.getName(oAuth2User);

                user = new User();
                user.setEmail(email);
                user.setName(name);
                user.setProviderType(providerType);
                user.setProviderId(providerId);

                // OAuth users should also have the normal USER role
                user.setRole(Role.USER);

                user = userRepo.save(user);
            }
        }

        // 8. The user now exists in our database.
        //    Generate our application's JWT and return the login response.
        String token = authUtil.generateAccessToken(user);
        mailService.sendMail(user.getEmail());

        return new LoginResponseDto(token);
    }
}
