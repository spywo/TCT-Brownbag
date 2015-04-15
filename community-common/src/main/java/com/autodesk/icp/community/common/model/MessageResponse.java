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
package com.autodesk.icp.community.common.model;


/**
 * @author Oliver Wu
 */
public class MessageResponse {
    private String status;
    private String exception;
    private Object payload;

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the payload
     */
    public Object getPayload() {
        return payload;
    }

    /**
     * @param payload
     *            the payload to set
     */
    public void setPayload(Object payload) {
        this.payload = payload;
    }

    /**
     * @return the exception
     */
    public String getException() {
        return exception;
    }

    /**
     * @param exception
     *            the exception to set
     */
    public void setException(String exception) {
        this.exception = exception;
    }

}
