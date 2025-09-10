/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.servlet.ServletRequest
 *  javax.servlet.ServletResponse
 *  javax.servlet.http.HttpServletResponse
 *  org.apache.shiro.web.servlet.AdviceFilter
 */
package org.sonatype.nexus.security;

import java.util.Collection;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.web.servlet.AdviceFilter;

@Named
@Singleton
public class CookieFilter
extends AdviceFilter {
    private static final String SECURE_FLAG = "; Secure";

    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        this.filterCookies(request, response);
        return true;
    }

    protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
        this.filterCookies(request, response);
    }

    protected void filterCookies(ServletRequest request, ServletResponse response) {
        if (request.isSecure() && response instanceof HttpServletResponse) {
            this.secureCookies((HttpServletResponse)response);
        }
    }

    private void secureCookies(HttpServletResponse response) {
        Collection cookies = response.getHeaders("Set-Cookie");
        boolean mustAdd = false;
        for (String cookie : cookies) {
            Object cookieVal;
            Object object = cookieVal = cookie.lastIndexOf(SECURE_FLAG) == -1 ? cookie + SECURE_FLAG : cookie;
            if (mustAdd) {
                response.addHeader("Set-Cookie", (String)cookieVal);
            } else {
                response.setHeader("Set-Cookie", (String)cookieVal);
            }
            mustAdd = true;
        }
    }
}

