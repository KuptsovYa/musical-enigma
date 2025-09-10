/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.servlet.ServletRequest
 *  javax.servlet.ServletResponse
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpServletResponse
 *  org.apache.shiro.web.filter.authc.AuthenticationFilter
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.sonatype.nexus.security.authc;

import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.security.authc.AntiCsrfHelper;

@Named
@Singleton
public class AntiCsrfFilter
extends AuthenticationFilter {
    public static final String NAME = "nx-anticsrf-authc";
    private static final Logger log = LoggerFactory.getLogger(AntiCsrfFilter.class);
    private final AntiCsrfHelper csrfHelper;

    @Inject
    public AntiCsrfFilter(AntiCsrfHelper csrfHelper) {
        this.csrfHelper = csrfHelper;
    }

    public boolean isEnabled() {
        return this.csrfHelper.isEnabled();
    }

    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return this.csrfHelper.isAccessAllowed((HttpServletRequest)request);
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        log.debug("Rejecting request from {} due to invalid cross-site request forgery token", (Object)request.getRemoteAddr());
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        httpResponse.setStatus(401);
        httpResponse.setContentType("text/plain");
        httpResponse.getWriter().print("Anti cross-site request forgery token mismatch");
        return false;
    }
}

