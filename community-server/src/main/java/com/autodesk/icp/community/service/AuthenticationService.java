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
package com.autodesk.icp.community.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.autodesk.icp.community.common.model.User;
import com.unboundid.ldap.sdk.BindRequest;
import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.ldap.sdk.SimpleBindRequest;
import com.unboundid.ldap.sdk.controls.SubentriesRequestControl;

/**
 * The LDAP authentication service.
 * 
 * @author Oliver Wu
 */
@Service
public class AuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    private static String ldapHost = "ads.autodesk.com";
    private static int ldapPort = 3268;

    public User login(String username, String password) {
        LDAPConnection connection = null;
        try {
            connection = openConnection(username, password);
            User user = queryLdap(connection, username);
            return user;
        } catch (LDAPException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private LDAPConnection openConnection(String username, String password) throws LDAPException {

        LDAPConnection connection = new LDAPConnection(ldapHost, ldapPort);

        BindRequest bindRequest = new SimpleBindRequest(username, password);
        BindResult bindResult = connection.bind(bindRequest);
        if (bindResult.getResultCode().equals(ResultCode.SUCCESS)) {
            return connection;
        } else {
            throw new LDAPException(bindResult.getResultCode(), "Failed to connect to the LDAP.");
        }
    }

    private User queryLdap(LDAPConnection connection, String username) throws LDAPException {
        User user = null;

        String accountName = username.toLowerCase().replace("ads\\", "");
        String baseDN = "DC=ads,DC=autodesk,DC=com";
        String filter = "&(objectClass=user)(sAMAccountName=" + accountName + ")";

        SearchRequest searchRequest = new SearchRequest(baseDN, SearchScope.SUB, "(" + filter + ")");
        searchRequest.addControl(new SubentriesRequestControl());
        SearchResult searchResult = connection.search(searchRequest);

        int size = searchResult.getSearchEntries().size();
        if (size == 0) {
            return null;
        } else {
            // always retrieves the first one
            SearchResultEntry entry = searchResult.getSearchEntries().get(0);

            user = new User();
            user.setName(entry.getAttribute("name").getValue());
            user.setEmail(entry.getAttribute("mail").getValue());
            user.setDisplayName(entry.getAttribute("displayName").getValue());
            user.setLoginId(username);
            user.setAuthed(true);
        }

        return user;
    }

}