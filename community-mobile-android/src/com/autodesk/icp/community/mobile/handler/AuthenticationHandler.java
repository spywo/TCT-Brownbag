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
package com.autodesk.icp.community.mobile.handler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import com.autodesk.icp.community.common.model.User;
import com.autodesk.icp.community.common.util.JSONReadWriteHelper;
import com.autodesk.icp.community.stomp.StompSession;
import com.autodesk.icp.community.ws.handler.AbstractStompMessageHandler;

/**
 * @author Oliver Wu
 */
public class AuthenticationHandler extends AbstractStompMessageHandler {
    private String subscrible;

    private String destination;

    private User payload;

    public AuthenticationHandler(String subscrible, String destination, User payload, CountDownLatch latch ) {
        this.subscrible = subscrible;
        this.destination = destination;
        this.payload = payload;
        this.latch = latch;
    }

    public void afterConnected(StompSession session, StompHeaderAccessor headers) {
        session.subscribe(subscrible, null);

        session.send(destination, payload);
    }

    public void handleMessage(Message<byte[]> message) {
//        StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);
       
        try (InputStream is = new ByteArrayInputStream(message.getPayload())) {
            User user = JSONReadWriteHelper.deSerializeJSON(is, User.class);
            
            this.payload = user;
        } catch (IOException e) {
        } finally {
            this.latch.countDown();
        }            
    }
    
    public User getPayload() {
        return payload;
    }
}
