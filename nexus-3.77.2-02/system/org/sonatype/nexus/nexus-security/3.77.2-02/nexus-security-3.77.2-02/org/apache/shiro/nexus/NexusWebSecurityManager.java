/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Provider
 *  org.apache.shiro.authc.AuthenticationException
 *  org.apache.shiro.authc.AuthenticationToken
 *  org.apache.shiro.session.mgt.eis.CachingSessionDAO
 *  org.apache.shiro.session.mgt.eis.SessionDAO
 *  org.apache.shiro.subject.Subject
 *  org.apache.shiro.web.mgt.DefaultWebSecurityManager
 *  org.sonatype.goodies.common.Time
 *  org.sonatype.nexus.cache.CacheHelper
 *  org.sonatype.nexus.common.app.ManagedLifecycleManager
 *  org.sonatype.nexus.common.event.EventManager
 */
package org.apache.shiro.nexus;

import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.nexus.NexusWebSessionManager;
import org.apache.shiro.nexus.ShiroJCacheManagerAdapter;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.sonatype.goodies.common.Time;
import org.sonatype.nexus.cache.CacheHelper;
import org.sonatype.nexus.common.app.ManagedLifecycleManager;
import org.sonatype.nexus.common.event.EventManager;
import org.sonatype.nexus.security.UserIdMdcHelper;
import org.sonatype.nexus.security.authc.AuthenticationEvent;
import org.sonatype.nexus.security.authc.AuthenticationFailureReason;
import org.sonatype.nexus.security.authc.LoginEvent;
import org.sonatype.nexus.security.authc.NexusAuthenticationException;

public class NexusWebSecurityManager
extends DefaultWebSecurityManager {
    private final Provider<EventManager> eventManager;

    @Inject
    public NexusWebSecurityManager(Provider<EventManager> eventManager, Provider<CacheHelper> cacheHelper, @Named(value="${nexus.shiro.cache.defaultTimeToLive:-2m}") Provider<Time> defaultTimeToLive) {
        this.eventManager = (Provider)Preconditions.checkNotNull(eventManager);
        this.setCacheManager(new ShiroJCacheManagerAdapter(cacheHelper, defaultTimeToLive));
        this.setRememberMeManager(null);
    }

    private void post(AuthenticationToken token, boolean successful, Set<AuthenticationFailureReason> authenticationFailureReasons) {
        ((EventManager)this.eventManager.get()).post((Object)new AuthenticationEvent(token.getPrincipal().toString(), successful, authenticationFailureReasons));
    }

    public Subject login(Subject subject, AuthenticationToken token) {
        if ("anonymous".equals(token.getPrincipal())) {
            throw new AuthenticationException("Cannot login with anonymous user");
        }
        try {
            subject = super.login(subject, token);
            UserIdMdcHelper.set(subject);
            this.post(token, true, Collections.emptySet());
            Optional<String> realmName = subject.getPrincipals().getRealmNames().stream().filter(realm -> realm.equals("SamlRealm")).findFirst();
            String principal = subject.getPrincipal().toString();
            realmName.ifPresent(realm -> ((EventManager)this.eventManager.get()).post((Object)new LoginEvent(principal, (String)realm)));
            return subject;
        }
        catch (NexusAuthenticationException e) {
            this.post(token, false, e.getAuthenticationFailureReasons());
            throw e;
        }
        catch (AuthenticationException e) {
            this.post(token, false, Collections.emptySet());
            throw e;
        }
    }

    public void logout(Subject subject) {
        super.logout(subject);
        UserIdMdcHelper.unset();
    }

    public void destroy() {
        if (ManagedLifecycleManager.isShuttingDown()) {
            super.destroy();
        } else {
            SessionDAO sessionDAO = ((NexusWebSessionManager)this.getSessionManager()).getSessionDAO();
            if (sessionDAO instanceof CachingSessionDAO) {
                ((CachingSessionDAO)sessionDAO).setActiveSessionsCache(null);
            }
        }
    }
}

