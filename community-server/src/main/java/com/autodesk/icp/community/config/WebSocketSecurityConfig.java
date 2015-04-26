package com.autodesk.icp.community.config;

import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

//@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages.simpDestMatchers("/app/**")
                .hasRole("USER_ROLE")
                .simpSubscribeDestMatchers("/user/**", "/topic/**", "/queue/**")
                .hasRole("USER_ROLE")                
                .anyMessage()
                .authenticated();

    }
}