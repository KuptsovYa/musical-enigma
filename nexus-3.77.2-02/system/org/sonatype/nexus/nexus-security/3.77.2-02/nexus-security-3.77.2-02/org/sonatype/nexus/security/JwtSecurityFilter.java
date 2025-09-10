/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.auth0.jwt.interfaces.Claim
 *  com.auth0.jwt.interfaces.DecodedJWT
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  javax.servlet.ServletRequest
 *  javax.servlet.ServletResponse
 *  javax.servlet.http.Cookie
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.shiro.mgt.SecurityManager
 *  org.apache.shiro.session.Session
 *  org.apache.shiro.session.mgt.SimpleSession
 *  org.apache.shiro.subject.PrincipalCollection
 *  org.apache.shiro.subject.SimplePrincipalCollection
 *  org.apache.shiro.web.filter.mgt.FilterChainResolver
 *  org.apache.shiro.web.mgt.WebSecurityManager
 *  org.apache.shiro.web.subject.WebSubject
 *  org.apache.shiro.web.subject.support.WebDelegatingSubject
 *  org.apache.shiro.web.util.WebUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.sonatype.nexus.common.text.Strings2
 */
package org.sonatype.nexus.security;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.subject.WebSubject;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.common.text.Strings2;
import org.sonatype.nexus.security.JwtHelper;
import org.sonatype.nexus.security.SecurityFilter;
import org.sonatype.nexus.security.jwt.JwtVerificationException;

@Singleton
public class JwtSecurityFilter
extends SecurityFilter {
    private final JwtHelper jwtHelper;
    private static final Logger log = LoggerFactory.getLogger(JwtSecurityFilter.class);

    @Inject
    public JwtSecurityFilter(WebSecurityManager webSecurityManager, FilterChainResolver filterChainResolver, JwtHelper jwtHelper) {
        super(webSecurityManager, filterChainResolver);
        this.jwtHelper = (JwtHelper)((Object)Preconditions.checkNotNull((Object)((Object)jwtHelper)));
    }

    protected WebSubject createSubject(ServletRequest request, ServletResponse response) {
        Optional<Cookie> jwtCookie;
        Cookie[] cookies = ((HttpServletRequest)request).getCookies();
        if (cookies != null && (jwtCookie = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("NXSESSIONID")).findFirst()).isPresent()) {
            Cookie cookie2 = jwtCookie.get();
            SimpleSession session = new SimpleSession(request.getRemoteHost());
            String jwt = cookie2.getValue();
            if (!Strings2.isEmpty((String)jwt)) {
                DecodedJWT decodedJwt;
                try {
                    decodedJwt = this.jwtHelper.verifyJwt(jwt);
                }
                catch (JwtVerificationException e) {
                    log.debug("Expire and reset the JWT cookie due to the error: {}", (Object)e.getMessage());
                    cookie2.setValue("");
                    cookie2.setMaxAge(0);
                    WebUtils.toHttp((ServletResponse)response).addCookie(cookie2);
                    return super.createSubject(request, response);
                }
                Claim user = decodedJwt.getClaim("user");
                Claim realm = decodedJwt.getClaim("realm");
                SimplePrincipalCollection principals = new SimplePrincipalCollection((Object)user.asString(), realm.asString());
                session.setTimeout(TimeUnit.SECONDS.toMillis(this.jwtHelper.getExpirySeconds()));
                session.setAttribute((Object)"NXSESSIONID", (Object)jwt);
                return new WebDelegatingSubject((PrincipalCollection)principals, true, request.getRemoteHost(), (Session)session, true, request, response, (SecurityManager)this.getSecurityManager());
            }
        }
        return super.createSubject(request, response);
    }
}

