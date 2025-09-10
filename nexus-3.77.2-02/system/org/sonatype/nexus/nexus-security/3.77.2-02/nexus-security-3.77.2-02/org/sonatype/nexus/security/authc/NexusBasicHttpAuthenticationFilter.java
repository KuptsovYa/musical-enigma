/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.servlet.ServletException
 *  javax.servlet.ServletRequest
 *  javax.servlet.ServletResponse
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.shiro.authc.AuthenticationToken
 *  org.apache.shiro.authz.AuthorizationException
 *  org.apache.shiro.subject.Subject
 *  org.apache.shiro.subject.support.DefaultSubjectContext
 *  org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter
 *  org.apache.shiro.web.util.WebUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.sonatype.nexus.security.authc;

import java.io.IOException;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@Singleton
public class NexusBasicHttpAuthenticationFilter
extends BasicHttpAuthenticationFilter {
    public static final String NAME = "nx-basic-authc";
    private static final String EMPTY_CREDENTIALS = "Og==";
    public static final String BASIC_AUTH_REALM = "Sonatype Nexus Repository Manager";
    protected final Logger log = LoggerFactory.getLogger(((Object)((Object)this)).getClass());

    public NexusBasicHttpAuthenticationFilter() {
        this.setApplicationName(BASIC_AUTH_REALM);
    }

    protected boolean isPermissive(Object mappedValue) {
        return true;
    }

    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        request.setAttribute(DefaultSubjectContext.SESSION_CREATION_ENABLED, (Object)Boolean.FALSE);
        return super.onPreHandle(request, response, mappedValue);
    }

    protected void cleanup(ServletRequest request, ServletResponse response, Exception failure) throws ServletException, IOException {
        Throwable cause = failure;
        if (cause instanceof ServletException) {
            cause = cause.getCause();
        }
        if (cause instanceof AuthorizationException) {
            boolean authenticated;
            failure = null;
            Subject subject = this.getSubject(request, response);
            boolean bl = authenticated = subject.getPrincipal() != null && subject.isAuthenticated();
            if (authenticated) {
                WebUtils.toHttp((ServletResponse)response).sendError(403);
            } else {
                try {
                    this.onAccessDenied(request, response);
                }
                catch (Exception e) {
                    failure = e;
                }
            }
        }
        super.cleanup(request, response, failure);
    }

    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        if (request instanceof HttpServletRequest) {
            Object principal = subject.getPrincipal();
            if (principal == null) {
                principal = token.getPrincipal();
            }
            String userId = principal.toString();
            request.setAttribute("nexus.user.principal", principal);
            request.setAttribute("nexus.user.id", (Object)userId);
        }
        return super.onLoginSuccess(token, subject, request, response);
    }

    protected boolean isLoginAttempt(String authzHeader) {
        return !this.isEmptyCredentials(authzHeader) && super.isLoginAttempt(authzHeader);
    }

    private boolean isEmptyCredentials(String authzHeader) {
        if (!authzHeader.toLowerCase().contains("basic ")) {
            return false;
        }
        String[] parts = authzHeader.split(" ");
        return parts.length > 1 && parts[1].equals(EMPTY_CREDENTIALS);
    }
}

