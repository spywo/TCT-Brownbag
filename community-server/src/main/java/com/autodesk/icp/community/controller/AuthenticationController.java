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
package com.autodesk.icp.community.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.autodesk.icp.community.common.model.MessageResponse;
import com.autodesk.icp.community.common.model.User;
import com.autodesk.icp.community.common.util.Consts;
import com.autodesk.icp.community.exception.UnauthenticatedException;
import com.autodesk.icp.community.service.AuthenticationService;

/**
 * @author Oliver Wu
 */
@Controller
public class AuthenticationController extends BaseController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private AuthenticationService authService;

    @MessageMapping(value = "/login")
    @SendToUser("/queue/login")
    public MessageResponse login(@Payload User user, SimpMessageHeaderAccessor headerAccessor) {
        User authedUser = authService.login(user.getLoginId(), user.getPassword());
        
        if (authedUser!= null && authedUser.isAuthed()) {
            HttpSession session = (HttpSession)(((Map)headerAccessor.getMessageHeaders().get("simpSessionAttributes")).get(Consts.SESSION_ATTR_SESSION));
            session.setAttribute(Consts.SESSION_ATTR_USER, authedUser);
            MessageResponse mr = new MessageResponse();
            mr.setStatus(Consts.MESSAGE_STATUS_OK);
            mr.setPayload(authedUser);
            return mr;
        } else {
            throw new UnauthenticatedException();
        }      
    }
}
