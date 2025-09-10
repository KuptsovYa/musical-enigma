/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  org.apache.shiro.session.Session
 *  org.apache.shiro.session.mgt.SessionContext
 *  org.apache.shiro.session.mgt.SessionValidationScheduler
 *  org.apache.shiro.web.servlet.Cookie
 *  org.apache.shiro.web.session.mgt.DefaultWebSessionManager
 *  org.apache.shiro.web.util.WebUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.apache.shiro.nexus;

import javax.inject.Inject;
import javax.inject.Named;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NexusWebSessionManager
extends DefaultWebSessionManager {
    private static final Logger log = LoggerFactory.getLogger(NexusWebSessionManager.class);
    private static final String DEFAULT_NEXUS_SESSION_COOKIE_NAME = "NXSESSIONID";
    private static final ThreadLocal<Boolean> requestIsHttps = ThreadLocal.withInitial(() -> Boolean.TRUE);

    @Inject
    public void configureProperties(@Named(value="${shiro.globalSessionTimeout:-1800000}") long globalSessionTimeout, @Named(value="${nexus.sessionCookieName:-NXSESSIONID}") String sessionCookieName, @Named(value="${nexus.session.enabled:-true}") boolean sessionEnabled, @Named(value="${nexus.session.secureCookie:-true}") boolean cookieSecure) {
        this.setGlobalSessionTimeout(globalSessionTimeout);
        log.info("Global session timeout: {} ms", (Object)this.getGlobalSessionTimeout());
        this.setSessionIdCookieEnabled(sessionEnabled);
        Cookie cookie = this.getSessionIdCookie();
        cookie.setName(sessionCookieName);
        cookie.setSecure(cookieSecure);
        log.info("Session-cookie prototype: name={}, secure={}", (Object)cookie.getName(), (Object)cookie.isSecure());
    }

    protected void onStart(Session session, SessionContext context) {
        if (WebUtils.isHttp((Object)context)) {
            requestIsHttps.set(WebUtils.getHttpRequest((Object)context).isSecure());
        }
        try {
            super.onStart(session, context);
        }
        finally {
            if (WebUtils.isHttp((Object)context)) {
                requestIsHttps.remove();
            }
        }
    }

    public Cookie getSessionIdCookie() {
        Cookie cookie = super.getSessionIdCookie();
        boolean templateValue = cookie.isSecure();
        boolean requestIsSecure = requestIsHttps.get();
        log.trace("setting Secure flag on session cookie: systemValue={}, requestIsSecure={}", (Object)templateValue, (Object)requestIsSecure);
        cookie.setSecure(templateValue && requestIsSecure);
        return cookie;
    }

    protected synchronized void enableSessionValidation() {
        SessionValidationScheduler scheduler = this.getSessionValidationScheduler();
        if (scheduler == null) {
            super.enableSessionValidation();
        }
    }
}

