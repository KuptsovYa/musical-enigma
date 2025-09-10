/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Throwables
 *  com.google.inject.binder.AnnotatedBindingBuilder
 *  javax.inject.Singleton
 *  javax.servlet.ServletContext
 *  org.apache.shiro.authc.AuthenticationInfo
 *  org.apache.shiro.authc.AuthenticationToken
 *  org.apache.shiro.authc.Authenticator
 *  org.apache.shiro.authz.Authorizer
 *  org.apache.shiro.config.ConfigurationException
 *  org.apache.shiro.guice.web.ShiroWebModule
 *  org.apache.shiro.mgt.RealmSecurityManager
 *  org.apache.shiro.mgt.SessionStorageEvaluator
 *  org.apache.shiro.mgt.SubjectDAO
 *  org.apache.shiro.realm.Realm
 *  org.apache.shiro.session.mgt.SessionFactory
 *  org.apache.shiro.session.mgt.SessionManager
 *  org.apache.shiro.session.mgt.eis.SessionDAO
 *  org.apache.shiro.web.config.ShiroFilterConfiguration
 *  org.apache.shiro.web.filter.mgt.FilterChainManager
 *  org.apache.shiro.web.filter.mgt.FilterChainResolver
 *  org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver
 *  org.apache.shiro.web.mgt.WebSecurityManager
 */
package org.sonatype.nexus.security;

import com.google.common.base.Throwables;
import com.google.inject.binder.AnnotatedBindingBuilder;
import java.lang.reflect.Constructor;
import javax.inject.Singleton;
import javax.servlet.ServletContext;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.guice.web.ShiroWebModule;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.mgt.SubjectDAO;
import org.apache.shiro.nexus.NexusSessionDAO;
import org.apache.shiro.nexus.NexusSessionFactory;
import org.apache.shiro.nexus.NexusSessionStorageEvaluator;
import org.apache.shiro.nexus.NexusSubjectDAO;
import org.apache.shiro.nexus.NexusWebSecurityManager;
import org.apache.shiro.nexus.NexusWebSessionManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.web.config.ShiroFilterConfiguration;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.sonatype.nexus.security.DynamicFilterChainManager;
import org.sonatype.nexus.security.authc.FirstSuccessfulModularRealmAuthenticator;
import org.sonatype.nexus.security.authz.ExceptionCatchingModularRealmAuthorizer;

public class WebSecurityModule
extends ShiroWebModule {
    public WebSecurityModule(ServletContext servletContext) {
        super(servletContext);
    }

    protected void configureShiroWeb() {
        this.bindRealm().to(EmptyRealm.class);
        this.bindSingleton(SessionFactory.class, NexusSessionFactory.class);
        this.bindSingleton(SessionStorageEvaluator.class, NexusSessionStorageEvaluator.class);
        this.bindSingleton(SubjectDAO.class, NexusSubjectDAO.class);
        this.bindSingleton(SessionDAO.class, NexusSessionDAO.class);
        this.bindSingleton(Authenticator.class, FirstSuccessfulModularRealmAuthenticator.class);
        this.bindSingleton(Authorizer.class, ExceptionCatchingModularRealmAuthorizer.class);
        this.bindSingleton(FilterChainManager.class, DynamicFilterChainManager.class);
        this.bind(ShiroFilterConfiguration.class).asEagerSingleton();
        this.bind(FilterChainResolver.class).toConstructor(WebSecurityModule.ctor(PathMatchingFilterChainResolver.class, new Class[0])).asEagerSingleton();
        this.expose(FilterChainResolver.class);
        this.expose(FilterChainManager.class);
    }

    private <T> void bindSingleton(Class<T> api, Class<? extends T> impl) {
        this.bind(impl).in(Singleton.class);
        this.bind(api).to(impl);
    }

    protected void bindWebSecurityManager(AnnotatedBindingBuilder<? super WebSecurityManager> bind) {
        this.bind(NexusWebSecurityManager.class).asEagerSingleton();
        this.bind(RealmSecurityManager.class).to(NexusWebSecurityManager.class);
        bind.to(NexusWebSecurityManager.class);
        this.expose(RealmSecurityManager.class);
        this.expose(WebSecurityManager.class);
    }

    protected void bindSessionManager(AnnotatedBindingBuilder<SessionManager> bind) {
        bind.to(NexusWebSessionManager.class).asEagerSingleton();
        this.bind(NexusWebSessionManager.class);
    }

    private static <T> Constructor<T> ctor(Class<T> clazz, Class<?> ... parameterTypes) {
        try {
            return clazz.getConstructor(parameterTypes);
        }
        catch (Exception e) {
            Throwables.throwIfUnchecked((Throwable)e);
            throw new ConfigurationException((Throwable)e);
        }
    }

    @Singleton
    private static final class EmptyRealm
    implements Realm {
        private EmptyRealm() {
        }

        public String getName() {
            return this.getClass().getName();
        }

        public boolean supports(AuthenticationToken token) {
            return false;
        }

        public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) {
            return null;
        }
    }
}

