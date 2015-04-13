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
package com.autodesk.icp.community.ws.handler;

import java.util.concurrent.CountDownLatch;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import com.autodesk.icp.community.stomp.StompMessageHandler;
import com.autodesk.icp.community.stomp.StompSession;

/**
 * @author Oliver Wu
 */
public class AbstractStompMessageHandler implements StompMessageHandler {

    protected CountDownLatch latch;

    /*
     * (non-Javadoc)
     * @see
     * com.autodesk.icp.community.stomp.StompMessageHandler#afterConnected(com.autodesk.icp.community.stomp.StompSession
     * , org.springframework.messaging.simp.stomp.StompHeaderAccessor)
     */
    @Override
    public void afterConnected(StompSession session, StompHeaderAccessor headers) {

    }

    /*
     * (non-Javadoc)
     * @see com.autodesk.icp.community.stomp.StompMessageHandler#handleMessage(org.springframework.messaging.Message)
     */
    @Override
    public void handleMessage(Message<byte[]> message) {
        if (latch != null) {
            latch.countDown();
        }
    }

    /*
     * (non-Javadoc)
     * @see com.autodesk.icp.community.stomp.StompMessageHandler#handleReceipt(java.lang.String)
     */
    @Override
    public void handleReceipt(String receiptId) {
        if (latch != null) {
            latch.countDown();
        }
    }

    /*
     * (non-Javadoc)
     * @see com.autodesk.icp.community.stomp.StompMessageHandler#handleError(org.springframework.messaging.Message)
     */
    @Override
    public void handleError(Message<byte[]> message) {
        if (latch != null) {
            latch.countDown();
        }
    }

    /*
     * (non-Javadoc)
     * @see com.autodesk.icp.community.stomp.StompMessageHandler#afterDisconnected()
     */
    @Override
    public void afterDisconnected() {
        if (latch != null) {
            latch.countDown();
        }
    }

}
