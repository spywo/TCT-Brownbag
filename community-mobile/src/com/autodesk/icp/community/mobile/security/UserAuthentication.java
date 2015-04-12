package com.autodesk.icp.community.mobile.security;

import com.unboundid.ldap.sdk.BindRequest;
import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.ldap.sdk.SimpleBindRequest;
import com.unboundid.ldap.sdk.controls.SubentriesRequestControl;

public class UserAuthentication {
    private static String ldapHost = "ads.autodesk.com";
    private static int ldapPort = 3268;    
    
    private static LDAPConnection connection = null;

    public static void main(String[] args) {
        
        String baseDN = "DC=ads,DC=autodesk,DC=com";
        
        String filter = "&(objectClass=user)(sAMAccountName=xuer)";
        
        
        queryLdap(baseDN, filter);
        connection.close();
       
    }

    
    public static void openConnection() {
        if (connection == null) {
            try {
             // exception handling not shown
                connection = new LDAPConnection(ldapHost,ldapPort);
                BindRequest bindRequest = new SimpleBindRequest("ads\\wuol", "Tenghuawu7283");
                BindResult bindResult = connection.bind(bindRequest);
                if(bindResult.getResultCode().equals(ResultCode.SUCCESS)) {
                   /// successful authentication
                }                
            } catch (Exception e) {
                System.out.println("连接LDAP出现错误：\n" + e.getMessage());
            }
        }
    }

    /** 查询 */
    public static void queryLdap(String searchDN, String filter) {
        try {
            // 连接LDAP
            openConnection();

            // 查询企业所有用户
            SearchRequest searchRequest = new SearchRequest(searchDN, SearchScope.SUB, "(" + filter + ")");
            searchRequest.addControl(new SubentriesRequestControl());
            SearchResult searchResult = connection.search(searchRequest);
            System.out.println(">>>共查询到" + searchResult.getSearchEntries().size() + "条记录");
            int index = 1;
            for (SearchResultEntry entry : searchResult.getSearchEntries()) {
                System.out.println((index++) + "\t" + entry.getDN());
            }
        } catch (Exception e) {
            System.out.println("查询错误，错误信息如下：\n" + e.getMessage());
        }
    }

}