/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.servlet.http.Cookie
 *  javax.servlet.http.HttpServletRequest
 *  javax.ws.rs.core.MediaType
 *  org.apache.shiro.SecurityUtils
 *  org.apache.shiro.authz.UnauthorizedException
 *  org.apache.shiro.subject.Subject
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.text.Strings2
 */
package org.sonatype.nexus.security.authc;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.text.Strings2;
import org.sonatype.nexus.security.authc.CsrfExemption;

@Named
@Singleton
public class AntiCsrfHelper
extends ComponentSupport {
    public static final String ENABLED = "nexus.security.anticsrftoken.enabled";
    public static final String ERROR_MESSAGE_TOKEN_MISMATCH = "Anti cross-site request forgery token mismatch";
    public static final String ANTI_CSRF_TOKEN_NAME = "NX-ANTI-CSRF-TOKEN";
    private final boolean enabled;
    private final List<CsrfExemption> csrfExemptPaths;

    @Inject
    public AntiCsrfHelper(@Named(value="${nexus.security.anticsrftoken.enabled:-true}") boolean enabled, List<CsrfExemption> csrfExemptPaths) {
        this.enabled = enabled;
        this.csrfExemptPaths = csrfExemptPaths;
    }

    public boolean isAccessAllowed(HttpServletRequest httpRequest) {
        if (!this.enabled) {
            return true;
        }
        return this.isSafeHttpMethod(httpRequest) || this.isMultiPartFormDataPost(httpRequest) || !this.isSessionAuthentication() || this.isExemptRequest(httpRequest) || this.isAntiCsrfTokenValid(httpRequest, Optional.ofNullable(httpRequest.getHeader(ANTI_CSRF_TOKEN_NAME)));
    }

    public void requireValidToken(HttpServletRequest httpRequest, @Nullable String token) {
        Optional<String> optToken;
        Optional<String> optional = optToken = token == null ? Optional.ofNullable(httpRequest.getHeader(ANTI_CSRF_TOKEN_NAME)) : Optional.of(token);
        if (!this.enabled || !this.isSessionAuthentication() || this.isAntiCsrfTokenValid(httpRequest, optToken)) {
            return;
        }
        throw new UnauthorizedException(ERROR_MESSAGE_TOKEN_MISMATCH);
    }

    private boolean isSafeHttpMethod(HttpServletRequest request) {
        String method = request.getMethod();
        return "GET".equals(method) || "HEAD".equals(method);
    }

    private boolean isMultiPartFormDataPost(HttpServletRequest request) {
        return "POST".equals(request.getMethod()) && !Strings2.isBlank((String)request.getContentType()) && MediaType.MULTIPART_FORM_DATA_TYPE.isCompatible(MediaType.valueOf((String)request.getContentType()));
    }

    private boolean isSessionAuthentication() {
        Subject subject = SecurityUtils.getSubject();
        return subject != null && subject.getSession(false) != null;
    }

    private Optional<String> getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (!cookieName.equals(cookie.getName())) continue;
                return Optional.ofNullable(cookie.getValue());
            }
        }
        return Optional.empty();
    }

    private Optional<String> getAntiCsrfTokenCookie(HttpServletRequest request) {
        return this.getCookie(request, ANTI_CSRF_TOKEN_NAME);
    }

    private boolean isAntiCsrfTokenValid(HttpServletRequest request, Optional<String> token) {
        Optional<String> cookie = this.getAntiCsrfTokenCookie(request);
        return token.isPresent() && token.equals(cookie);
    }

    private boolean isExemptRequest(HttpServletRequest request) {
        String requestPath = request.getServletPath();
        return this.csrfExemptPaths.stream().map(CsrfExemption::getPath).anyMatch(requestPath::contains);
    }

    public boolean isEnabled() {
        return this.enabled;
    }
}

