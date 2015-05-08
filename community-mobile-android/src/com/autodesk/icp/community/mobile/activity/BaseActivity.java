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
package com.autodesk.icp.community.mobile.activity;


import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author Oliver Wu
 */
public class BaseActivity extends Activity {

    protected void createProgressBar() {
        LinearLayout mProgressLayout = new LinearLayout(this);
        mProgressLayout.setMinimumHeight(30);
        mProgressLayout.setGravity(Gravity.CENTER);
        mProgressLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                   LinearLayout.LayoutParams.WRAP_CONTENT);
        ProgressBar mProgressBar = new ProgressBar(this);
        mProgressBar.setPadding(100, 250, 5, 80);
        mProgressLayout.addView(mProgressBar, mLayoutParams);
        TextView mContent = new TextView(this);
        mContent.setText("Loading...");
        mContent.setTextSize(19);
        mContent.setTextColor(Color.BLACK);
        mContent.setPadding(0, 250, 0, 80);
        mProgressLayout.addView(mContent, mLayoutParams);
        mProgressLayout.setVisibility(View.VISIBLE);
        this.addContentView(mProgressLayout, mLayoutParams);
    }
}
