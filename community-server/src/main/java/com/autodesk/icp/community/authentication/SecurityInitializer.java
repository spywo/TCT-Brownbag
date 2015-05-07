package com.autodesk.icp.community.authentication;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

@Order(value = 2)
public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {
}