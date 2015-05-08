package com.autodesk.icp.community.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.autodesk.icp.community.common.model.ServiceResponse;
import com.autodesk.icp.community.common.model.ServiceStatus;
import com.autodesk.icp.community.common.model.User;
import com.autodesk.icp.community.common.model.UserPrincipal;
import com.autodesk.icp.community.common.util.JSONReadWriteHelper;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException,
                                                                                                                      ServletException {

        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        
        ServiceResponse<User> sr = new ServiceResponse<User>(ServiceStatus.OK, ((UserPrincipal)auth.getPrincipal()).getUser());
        response.getWriter().write(JSONReadWriteHelper.serializeToJSON(sr));

        response.setStatus(HttpStatus.OK.value());
    }
}