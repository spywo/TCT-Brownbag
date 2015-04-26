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
package com.autodesk.icp.community.util;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.autodesk.icp.community.common.model.User;
import com.autodesk.icp.community.common.util.Consts;

/**
 * @author Oliver Wu
 */
public class WSUtils {
    public static HttpSession getHttpSession(Message<?> message) {
        @SuppressWarnings("unchecked")
        Map<String, Object> attributes = (Map<String, Object>)message.getHeaders()
                                                                     .get(SimpMessageHeaderAccessor.SESSION_ATTRIBUTES);
        HttpSession session = (HttpSession)attributes.get(Consts.SESSION_ATTR_SESSION);
        return session;
    }

    public static boolean isSendMessage(Message<?> message) {
        return SimpMessageType.MESSAGE.equals(message.getHeaders().get(SimpMessageHeaderAccessor.MESSAGE_TYPE_HEADER));
    }

    public static User getUser(Message<?> message) {
        User user = null;
        HttpSession session = WSUtils.getHttpSession(message);
        if (session != null) {
            user = (User)session.getAttribute(Consts.SESSION_ATTR_USER);
        }
        return user;
    }

    public static void saveUser(Message<?> message, final User user) {
        getHttpSession(message).setAttribute(Consts.SESSION_ATTR_USER, user);
        
        SecurityContextHolder.getContext().setAuthentication(new Authentication() {
            
            @Override
            public String getName() {
                // TODO Auto-generated method stub
                return user.getDisplayName();
            }
            
            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public boolean isAuthenticated() {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public Object getPrincipal() {
                // TODO Auto-generated method stub
                return user;
            }
            
            @Override
            public Object getDetails() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Object getCredentials() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                // TODO Auto-generated method stub
                return null;
            }
        });
        

        // Create a new session and add the security context.
        
        getHttpSession(message).setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
    }

    public static boolean isLoginRequest(Message<?> message) {
        boolean flag = false;
        String od = (String)message.getHeaders().get(SimpMessageHeaderAccessor.DESTINATION_HEADER);

        if ("/aae/login".equals(od)) {
            flag = true;
        }

        return flag;
    }
}