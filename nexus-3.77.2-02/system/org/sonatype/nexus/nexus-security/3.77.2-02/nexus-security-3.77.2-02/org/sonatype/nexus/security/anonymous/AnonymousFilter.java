/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.cache.CacheBuilder
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Provider
 *  javax.inject.Singleton
 *  javax.servlet.ServletRequest
 *  javax.servlet.ServletResponse
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.shiro.SecurityUtils
 *  org.apache.shiro.subject.Subject
 *  org.apache.shiro.util.ThreadContext
 *  org.apache.shiro.web.servlet.AdviceFilter
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.sonatype.nexus.common.event.EventManager
 */
package org.sonatype.nexus.security.anonymous;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.common.event.EventManager;
import org.sonatype.nexus.security.ClientInfo;
import org.sonatype.nexus.security.anonymous.AnonymousAccessEvent;
import org.sonatype.nexus.security.anonymous.AnonymousManager;

@Named
@Singleton
public class AnonymousFilter
extends AdviceFilter {
    public static final String NAME = "nx-anonymous";
    private static final int CACHE_SIZE = 100;
    private static final String ORIGINAL_SUBJECT = AnonymousFilter.class.getName() + ".originalSubject";
    private static final Logger log = LoggerFactory.getLogger(AnonymousFilter.class);
    private final Provider<AnonymousManager> anonymousManager;
    private final Provider<EventManager> eventManager;
    private final Set<ClientInfo> cache = Collections.newSetFromMap(CacheBuilder.newBuilder().maximumSize(100L).build().asMap());

    @Inject
    public AnonymousFilter(Provider<AnonymousManager> anonymousManager, Provider<EventManager> eventManager) {
        this.anonymousManager = (Provider)Preconditions.checkNotNull(anonymousManager);
        this.eventManager = (Provider)Preconditions.checkNotNull(eventManager);
    }

    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        AnonymousManager manager = (AnonymousManager)this.anonymousManager.get();
        if ((subject.getPrincipal() == null || this.isAnonymousUser(manager, subject)) && manager.isEnabled()) {
            request.setAttribute(ORIGINAL_SUBJECT, (Object)subject);
            subject = manager.buildSubject();
            ThreadContext.bind((Subject)subject);
            log.trace("Bound anonymous subject: {}", (Object)subject);
            if (request instanceof HttpServletRequest) {
                String userId = manager.getConfiguration().getUserId();
                ClientInfo clientInfo = ClientInfo.builder().userId(userId).remoteIP(request.getRemoteAddr()).userAgent(((HttpServletRequest)request).getHeader("User-Agent")).path(((HttpServletRequest)request).getServletPath()).build();
                if (this.cache.add(clientInfo)) {
                    log.trace("Tracking new anonymous access from: {}", (Object)clientInfo);
                    ((EventManager)this.eventManager.get()).post((Object)new AnonymousAccessEvent(clientInfo, new Date()));
                }
            }
        }
        return true;
    }

    public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) throws Exception {
        Subject subject = (Subject)request.getAttribute(ORIGINAL_SUBJECT);
        if (subject != null) {
            log.trace("Binding original subject: {}", (Object)subject);
            ThreadContext.bind((Subject)subject);
        }
    }

    private boolean isAnonymousUser(AnonymousManager manager, Subject subject) {
        if (manager == null || manager.getConfiguration() == null || manager.getConfiguration().getUserId() == null) {
            return false;
        }
        return manager.getConfiguration().getUserId().equals(subject.getPrincipals().getPrimaryPrincipal());
    }
}

