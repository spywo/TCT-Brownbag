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

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.autodesk.icp.community.common.model.Notification;
import com.autodesk.icp.community.common.util.Utils;

/**
 * @author Oliver Wu
 */
@Controller
public class NotificationController {
    @Autowired
    private SimpMessagingTemplate template;
  
    @RequestMapping("/sendmessage")
    public void message() {
        Notification notification = new Notification();
        notification.setTitle("Change in HR Workways logo");
        notification.setDescription("Starting from today, you will see a change in the logo on the HRWorkways portal from Aon Hewitt to Excelity. Our payroll processing partner has been acquired by a leading private equity firm and they would now be operating under a new brand name Excelity. This transaction has necessitated a change in the logo on the HRWorkways portal as well. Please note that this change would not affect the services or support in any manner. You can continue to reach out to the email ids mentioned below for any queries that you might have.");
        notification.setTimestamp(Utils.formatDataInUTC(new Date()));
        template.convertAndSend("/topic/notification", notification);
    }
}
