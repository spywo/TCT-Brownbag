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
package com.autodesk.icp.community;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;

import com.autodesk.icp.community.stomp.WebSocketStompClient;

/**
 * @author Oliver Wu
 */
public class CommunicationBroker {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketStompClient.class);

    private volatile static WebSocketStompClient client;

    public static WebSocketStompClient getInstance() {
        if (client == null) {
            synchronized (CommunicationBroker.class) {
                if (client == null) {
                    client = getClient("ws://localhost:8080/community/portfolio");
                }
            }
        }
        return client;
    }

    public static WebSocketStompClient getClient(String endpointURI) {
        List<Transport> transports = new ArrayList<Transport>(2);
        // transports.add(new WebSocketTransport(new StandardWebSocketClient()));

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

        RestTemplateXhrTransport xhrTransport = new RestTemplateXhrTransport(restTemplate);
        xhrTransport.setXhrStreamingDisabled(false);
        transports.add(xhrTransport);
        SockJsClient sockJsClient = new SockJsClient(transports);

        WebSocketStompClient client = null;
        try {
            client = new WebSocketStompClient(new URI(endpointURI), new WebSocketHttpHeaders(), sockJsClient);
            client.setMessageConverter(new MappingJackson2MessageConverter());
        } catch (URISyntaxException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return client;
    }
}
