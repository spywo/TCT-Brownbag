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
package com.autodesk.icp.community.common.exception;

/**
 * It is for internal errors (ie system problems not the user's fault).
 * 
 * @author Oliver
 */
public class SystemException extends BaseException {
    private static final long serialVersionUID = -4385778692022807712L;

    public static final String DEFAULT_ERROR_CODE = "error.system";

    public static final String DEFAULT_ERROR_MESSAGE = "System is not available, please try again later.";

    public SystemException() {
        super(DEFAULT_ERROR_CODE, DEFAULT_ERROR_MESSAGE);
    }

    public SystemException(String pErrorCode) {
        super(pErrorCode, DEFAULT_ERROR_MESSAGE);
    }

    public SystemException(Throwable pNext) {
        super(DEFAULT_ERROR_CODE, pNext, DEFAULT_ERROR_MESSAGE);
    }

    public SystemException(String pErrorCode, String pErrorMessage) {
        super(pErrorCode, pErrorMessage);
    }

    public SystemException(String pErrorCode, Throwable pNext, String pErrorMessage) {
        super(pErrorCode, pNext, pErrorMessage);
    }
}
