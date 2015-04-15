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
package com.autodesk.icp.community.intecepter;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.autodesk.icp.community.common.util.Consts;

/**
 * @author Oliver Wu
 */
public class SessionHoldHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    public SessionHoldHandshakeInterceptor() {
        super();
    }

    public SessionHoldHandshakeInterceptor(Collection<String> attributeNames) {
        super(attributeNames);
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        attributes.put(Consts.SESSION_ATTR_SESSION, getSession(request));

        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    private HttpSession getSession(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverRequest = (ServletServerHttpRequest)request;
            return serverRequest.getServletRequest().getSession(true);
        }
        return null;
    }
}
