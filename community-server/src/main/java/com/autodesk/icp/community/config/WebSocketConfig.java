//
// Copyright (C) 2015 by Autodesk, Inc. All Rights Reserved.
//
// The information contained herein is confidential, proprietary
// to Autodesk, Inc., and considered a trade secret as defined
// in section 499C of the penal code of the State of California.
// Use of this information by anyone other than authorized
// employees of Autodesk, Inc. is granted only under a written
// non-disclosure agreement, expressly prescribing the scope
// and manner of such use.
//
// AUTODESK MAKES NO WARRANTIES, EXPRESS OR IMPLIED, AS TO THE
// CORRECTNESS OF THIS CODE OR ANY DERIVATIVE WORKS WHICH INCORPORATE
// IT. AUTODESK PROVIDES THE CODE ON AN "AS-IS" BASIS AND EXPLICITLY
// DISCLAIMS ANY LIABILITY, INCLUDING CONSEQUENTIAL AND INCIDENTAL
// DAMAGES FOR ERRORS, OMISSIONS, AND OTHER PROBLEMS IN THE CODE.
//
// Use, duplication, or disclosure by the U.S. Government is subject
// to restrictions set forth in FAR 52.227-19 (Commercial Computer
// Software Restricted Rights) and DFAR 252.227-7013(c)(1)(ii)
// (Rights in Technical Data and Computer Software), as applicable.
//
package com.autodesk.icp.community.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.autodesk.icp.community.web.CommonTextHandler;
import com.autodesk.icp.community.web.HandshakeInterceptor;

/**
 * The configurer of the Web Scoket endpoints.
 * 
 * @author Oliver Wu
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketConfig.class);

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        LOGGER.info("Registering CommonTextHandler...");
        registry.addHandler(createCommonTextHandler(), "/text")
                .addInterceptors(new HandshakeInterceptor())
                .setAllowedOrigins("*");
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        container.setMaxSessionIdleTimeout(30 * 60 * 1000); // 30 minutes
        container.setAsyncSendTimeout(10 * 60 * 1000);
        return container;
    }

    @Bean
    public WebSocketHandler createCommonTextHandler() {
        return new CommonTextHandler();
    }
}