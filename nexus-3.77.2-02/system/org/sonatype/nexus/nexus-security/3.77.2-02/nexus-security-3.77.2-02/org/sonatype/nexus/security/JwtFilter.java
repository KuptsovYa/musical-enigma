/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.servlet.ServletRequest
 *  javax.servlet.ServletResponse
 *  javax.servlet.http.Cookie
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.shiro.web.servlet.AdviceFilter
 *  org.apache.shiro.web.util.WebUtils
 *  org.sonatype.nexus.common.text.Strings2
 */
package org.sonatype.nexus.security;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.apache.shiro.web.util.WebUtils;
import org.sonatype.nexus.common.text.Strings2;
import org.sonatype.nexus.security.JwtHelper;
import org.sonatype.nexus.security.JwtRefreshExemption;
import org.sonatype.nexus.security.jwt.JwtVerificationException;

@Named
@Singleton
public class JwtFilter
extends AdviceFilter {
    public static final String NAME = "nx-jwt";
    private final JwtHelper jwtHelper;
    private final List<JwtRefreshExemption> jwtExemptPaths;

    @Inject
    public JwtFilter(JwtHelper jwtHelper, List<JwtRefreshExemption> jwtExemptPaths) {
        this.jwtHelper = (JwtHelper)((Object)Preconditions.checkNotNull((Object)((Object)jwtHelper)));
        this.jwtExemptPaths = jwtExemptPaths;
    }

    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Cookie cookie2;
        String jwt;
        Optional<Cookie> jwtCookie;
        HttpServletRequest servletRequest = (HttpServletRequest)request;
        Cookie[] cookies = servletRequest.getCookies();
        if (cookies != null && !this.isExemptRequest(servletRequest) && (jwtCookie = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("NXSESSIONID")).findFirst()).isPresent() && !Strings2.isEmpty((String)(jwt = (cookie2 = jwtCookie.get()).getValue()))) {
            Cookie refreshedToken;
            try {
                refreshedToken = this.jwtHelper.verifyAndRefreshJwtCookie(jwt, request.isSecure());
            }
            catch (JwtVerificationException e) {
                cookie2.setValue("");
                cookie2.setMaxAge(0);
                WebUtils.toHttp((ServletResponse)response).addCookie(cookie2);
                return false;
            }
            WebUtils.toHttp((ServletResponse)response).addCookie(refreshedToken);
        }
        return true;
    }

    private boolean isExemptRequest(HttpServletRequest request) {
        String requestPath = request.getServletPath();
        return this.jwtExemptPaths.stream().map(JwtRefreshExemption::getPath).anyMatch(requestPath::contains);
    }
}

