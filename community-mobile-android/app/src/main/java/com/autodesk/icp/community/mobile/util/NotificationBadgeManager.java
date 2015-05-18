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
package com.autodesk.icp.community.mobile.util;

import android.view.View;

import com.autodesk.icp.community.mobile.ui.BadgeView;

/**
 * @author Oliver Wu
 */
public class NotificationBadgeManager {

    private static final NotificationBadgeManager instance = new NotificationBadgeManager();

    private static BadgeView notificationBadge;

    private static View target;

    public static NotificationBadgeManager getInstance() {
        return instance;
    }

    public void increse(View target) {
        if (notificationBadge == null || this.target != target) {
            BadgeView bv = new BadgeView(target.getContext(), target);

            if (notificationBadge != null) {
                bv.setText(notificationBadge.getText());
            }

            this.target = target;
            this.notificationBadge = bv;
        }

        notificationBadge.increment(1);
        notificationBadge.show();
    }

    public void decrease() {
        notificationBadge.decrement(1);

        if ("0".equals(notificationBadge.getText())) {
            notificationBadge.hide();
        }
    }
}
