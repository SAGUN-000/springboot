package com.example.Nap.Buyzen.security;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final AuthUtil authUtil;

    private static final String AUTHENTICATION =
            "WEBSOCKET_AUTHENTICATION";

    @Override
    public Message<?> preSend(
            Message<?> message,
            MessageChannel channel
    ) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(
                        message,
                        StompHeaderAccessor.class
                );

        if (accessor == null) {
            return message;
        }

        StompCommand command = accessor.getCommand();

        System.out.println("STOMP COMMAND = " + command);
        System.out.println("STOMP USER BEFORE = " + accessor.getUser());

        // Authenticate during STOMP CONNECT
        if (StompCommand.CONNECT.equals(command)) {

            String authorization =
                    accessor.getFirstNativeHeader("Authorization");

            System.out.println(
                    "WS AUTH HEADER = " + authorization
            );

            if (authorization == null ||
                    !authorization.startsWith("Bearer ")) {

                System.out.println(
                        "NO WS AUTHORIZATION HEADER"
                );

                return message;
            }

            String token =
                    authorization.substring(7);

            String email =
                    authUtil.getUsernameFromToken(token);

            int userId =
                    authUtil.getUserIdFromToken(token);

            String role =
                    authUtil.getRoleFromToken(token);

            List<GrantedAuthority> authorities =
                    List.of(
                            new SimpleGrantedAuthority(
                                    "ROLE_" + role
                            )
                    );

            SecurityPrinciple principal =
                    new SecurityPrinciple(
                            userId,
                            email,
                            authorities
                    );

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            principal.getAuthorities()
                    );

            // Set the authenticated user on the STOMP session
            accessor.setUser(authentication);

            // Explicitly store it in session attributes too
            accessor.getSessionAttributes()
                    .put(AUTHENTICATION, authentication);

            System.out.println(
                    "WS AUTHENTICATED USER = " + userId
            );
        }

        // Restore authentication for subsequent messages
        else {

            Object storedAuthentication =
                    accessor.getSessionAttributes()
                            .get(AUTHENTICATION);

            if (storedAuthentication instanceof Authentication authentication) {

                accessor.setUser(authentication);

                System.out.println(
                        "WS RESTORED USER = " +
                                ((SecurityPrinciple)
                                        authentication.getPrincipal())
                                        .getUserId()
                );
            }
        }

        System.out.println(
                "STOMP USER AFTER = " + accessor.getUser()
        );

        return message;
    }
}