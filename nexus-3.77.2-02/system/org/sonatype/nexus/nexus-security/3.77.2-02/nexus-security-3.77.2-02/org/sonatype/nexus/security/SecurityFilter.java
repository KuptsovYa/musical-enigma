/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  javax.servlet.FilterChain
 *  javax.servlet.ServletException
 *  javax.servlet.ServletRequest
 *  javax.servlet.ServletResponse
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.commons.lang.StringUtils
 *  org.apache.shiro.web.filter.mgt.FilterChainResolver
 *  org.apache.shiro.web.mgt.WebSecurityManager
 *  org.apache.shiro.web.servlet.AbstractShiroFilter
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.sonatype.nexus.security;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.security.Principal;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.security.UserIdMdcHelper;

@Singleton
public class SecurityFilter
extends AbstractShiroFilter {
    private static final Logger log = LoggerFactory.getLogger(SecurityFilter.class);
    public static final String ATTR_USER_PRINCIPAL = "nexus.user.principal";
    public static final String ATTR_USER_ID = "nexus.user.id";

    @Inject
    public SecurityFilter(WebSecurityManager webSecurityManager, FilterChainResolver filterChainResolver) {
        Preconditions.checkNotNull((Object)webSecurityManager);
        log.trace("Security manager: {}", (Object)webSecurityManager);
        this.setSecurityManager(webSecurityManager);
        Preconditions.checkNotNull((Object)filterChainResolver);
        log.trace("Filter chain resolver: {}", (Object)filterChainResolver);
        this.setFilterChainResolver(filterChainResolver);
    }

    protected void executeChain(ServletRequest request, ServletResponse response, FilterChain origChain) throws IOException, ServletException {
        UserIdMdcHelper.set();
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest)request;
            Principal p = httpRequest.getUserPrincipal();
            if (p != null) {
                httpRequest.setAttribute(ATTR_USER_PRINCIPAL, (Object)p);
                httpRequest.setAttribute(ATTR_USER_ID, (Object)p.getName());
            }
            String contextPath = httpRequest.getContextPath();
            String requestURI = httpRequest.getRequestURI();
            request.setAttribute("javax.servlet.include.servlet_path", (Object)StringUtils.removeStart((String)requestURI, (String)contextPath));
        }
        super.executeChain(request, response, origChain);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        UserIdMdcHelper.unknown();
        try {
            super.doFilterInternal(request, response, chain);
        }
        finally {
            UserIdMdcHelper.unset();
        }
    }
}

