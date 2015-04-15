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
package com.autodesk.icp.community.exception;

/**
 * It is for user-related errors (ie errors triggered by invalid data submitted by the user and thus correctable by the
 * user).
 * 
 * @author Oliver
 */
public class BusinessException extends BaseException {
    private static final long serialVersionUID = -4385778692022807712L;

    public static final String DEFAULT_ERROR_CODE = "error.business";

    public static final String DEFAULT_ERROR_MESSAGE = "Invalid operation.";

    public BusinessException() {
        super(DEFAULT_ERROR_CODE, DEFAULT_ERROR_MESSAGE);
    }

    public BusinessException(String pErrorCode) {
        super(pErrorCode, DEFAULT_ERROR_MESSAGE);
    }

    public BusinessException(Throwable pNext) {
        super(DEFAULT_ERROR_CODE, pNext, DEFAULT_ERROR_MESSAGE);
    }

    public BusinessException(String pErrorCode, String pErrorMessage) {
        super(pErrorCode, pErrorMessage);
    }

    public BusinessException(String pErrorCode, Throwable pNext, String pErrorMessage) {
        super(pErrorCode, pNext, pErrorMessage);
    }
}
