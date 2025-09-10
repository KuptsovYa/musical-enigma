/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.servlet.ServletRequest
 *  javax.servlet.ServletResponse
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.shiro.authc.AuthenticationToken
 *  org.apache.shiro.web.util.WebUtils
 */
package org.sonatype.nexus.security.authc;

import java.util.List;
import javax.annotation.Nullable;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.util.WebUtils;
import org.sonatype.nexus.security.authc.AuthenticationTokenFactory;
import org.sonatype.nexus.security.authc.HttpHeaderAuthenticationToken;

public abstract class HttpHeaderAuthenticationTokenFactorySupport
implements AuthenticationTokenFactory {
    @Override
    @Nullable
    public AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        List<String> headerNames = this.getHttpHeaderNames();
        if (headerNames != null) {
            HttpServletRequest httpRequest = WebUtils.toHttp((ServletRequest)request);
            for (String headerName : headerNames) {
                String headerValue = httpRequest.getHeader(headerName);
                if (headerValue == null) continue;
                return this.createToken(headerName, headerValue, request.getRemoteHost());
            }
        }
        return null;
    }

    protected HttpHeaderAuthenticationToken createToken(String headerName, String headerValue, String host) {
        return new HttpHeaderAuthenticationToken(headerName, headerValue, host);
    }

    protected abstract List<String> getHttpHeaderNames();

    public String toString() {
        return this.getClass().getSimpleName() + "(creates authentication tokens if any of HTTP headers is present: " + this.getHttpHeaderNames() + ")";
    }
}

