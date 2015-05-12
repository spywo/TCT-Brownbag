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
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.autodesk.icp.community.mobile.util.ConfigurationManager;
import com.autodesk.icp.community.mobile.util.SessionManager;

/**
 * @author Oliver Wu
 */
public class BaseActivity extends Activity {

    protected DialogFragment showLoadingDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment loadingFragment = new LoadingDialog();
        Bundle args = new Bundle();
        loadingFragment.setArguments(args);

        loadingFragment.show(ft, "loading dialog");
        
        return loadingFragment;
    }

    protected void dismissWaitingDialog(DialogFragment dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
    
    protected ConfigurationManager getConfigurationManager() {
        return new ConfigurationManager(this);
    }
    
    protected SessionManager getSessionManager() {
        return new SessionManager(this);
    }
    
    private class LoadingDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = new Dialog(BaseActivity.this, R.style.loading_circle_progress);
            dialog.setContentView(R.layout.loading);
            dialog.setCancelable(false);          
            
            return dialog;
        }
    }
}
