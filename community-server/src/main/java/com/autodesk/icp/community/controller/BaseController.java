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

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RequestMapping;

import com.autodesk.icp.community.common.exception.BaseException;
import com.autodesk.icp.community.common.exception.SystemException;
import com.autodesk.icp.community.common.model.MessageResponse;
import com.autodesk.icp.community.common.util.Consts;

/**
 * @author Oliver Wu
 */
public class BaseController {

    @RequestMapping("/home")
    public String index() {
        return "index";
    }

    @MessageExceptionHandler
    @SendToUser(value = "/queue/error", broadcast = false)
    public MessageResponse handleException(Exception exception) {

        MessageResponse mr = new MessageResponse();
        mr.setStatus(Consts.MESSAGE_STATUS_FAILURE);
        if (exception instanceof BaseException) {
            mr.setException(exception.getMessage());
        } else {
            SystemException se = new SystemException(SystemException.DEFAULT_ERROR_CODE,
                                                     exception,
                                                     exception.getMessage());
            mr.setException(se.getMessage());
        }

        return mr;
    }
}
