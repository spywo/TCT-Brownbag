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

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.autodesk.icp.community.common.model.User;
import com.autodesk.icp.community.exception.UnauthenticatedException;
import com.autodesk.icp.community.util.WSUtils;

/**
 * The interceptor for handling messages.
 * 
 * @author Oliver Wu
 */
@Aspect
@Component
public class AuthenticationMessageInterceptor {
    @Before(value = "@annotation(org.springframework.messaging.handler.annotation.MessageMapping)")
    public void verifyAuthentication(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        if (!"login".equalsIgnoreCase(methodName)) {
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                if (args[0] instanceof Message<?>) {
                    Message<?> message = (Message<?>)args[0];

                    User user = WSUtils.getUser(message);
                    if (user == null || !user.isAuthed()) {
                        throw new UnauthenticatedException();
                    }
                }
            }
        }
    }
}
