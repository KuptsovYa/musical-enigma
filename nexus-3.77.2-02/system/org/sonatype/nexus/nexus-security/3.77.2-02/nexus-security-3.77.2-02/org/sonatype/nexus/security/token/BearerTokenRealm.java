/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.common.base.Preconditions
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Provider
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.shiro.authc.AuthenticationException
 *  org.apache.shiro.authc.AuthenticationInfo
 *  org.apache.shiro.authc.AuthenticationToken
 *  org.apache.shiro.authc.SimpleAuthenticationInfo
 *  org.apache.shiro.realm.AuthenticatingRealm
 *  org.apache.shiro.subject.PrincipalCollection
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.sonatype.nexus.security.token;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.util.Optional;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.security.UserPrincipalsHelper;
import org.sonatype.nexus.security.authc.NexusApiKeyAuthenticationToken;
import org.sonatype.nexus.security.authc.apikey.ApiKey;
import org.sonatype.nexus.security.authc.apikey.ApiKeyService;
import org.sonatype.nexus.security.user.UserNotFoundException;

public abstract class BearerTokenRealm
extends AuthenticatingRealm {
    public static final String IS_TOKEN_AUTH_KEY = BearerTokenRealm.class.getName() + ".IS_TOKEN";
    @VisibleForTesting
    static final String ANONYMOUS_USER = "anonymous";
    private final Logger log = LoggerFactory.getLogger(((Object)((Object)this)).getClass());
    private final ApiKeyService keyStore;
    private final UserPrincipalsHelper principalsHelper;
    private final String format;
    private Provider<HttpServletRequest> requestProvider;

    protected BearerTokenRealm(ApiKeyService keyStore, UserPrincipalsHelper principalsHelper, String format) {
        this.keyStore = (ApiKeyService)Preconditions.checkNotNull((Object)keyStore);
        this.principalsHelper = (UserPrincipalsHelper)((Object)Preconditions.checkNotNull((Object)((Object)principalsHelper)));
        this.format = (String)Preconditions.checkNotNull((Object)format);
        this.setName(format);
        this.setAuthenticationCachingEnabled(true);
    }

    @Inject
    protected void setRequestProvider(Provider<HttpServletRequest> requestProvider) {
        this.requestProvider = (Provider)Preconditions.checkNotNull(requestProvider);
    }

    public boolean supports(AuthenticationToken token) {
        return token instanceof NexusApiKeyAuthenticationToken && this.format.equals(token.getPrincipal());
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        Preconditions.checkNotNull((Object)token);
        return this.getPrincipals(token).map(principals -> {
            try {
                if (this.anonymousAndSupported((PrincipalCollection)principals) || this.principalsHelper.getUserStatus((PrincipalCollection)principals).isActive()) {
                    return new SimpleAuthenticationInfo(principals, token.getCredentials());
                }
            }
            catch (UserNotFoundException e) {
                this.log.debug("Realm did not find user", (Throwable)e);
                this.keyStore.deleteApiKeys((PrincipalCollection)principals);
            }
            return null;
        }).orElse(null);
    }

    @Nullable
    protected Object getAuthenticationCacheKey(@Nullable AuthenticationToken token) {
        if (token != null) {
            return this.getPrincipals(token).map(PrincipalCollection::getPrimaryPrincipal).orElse(null);
        }
        return null;
    }

    protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws AuthenticationException {
        super.assertCredentialsMatch(token, info);
        ((HttpServletRequest)this.requestProvider.get()).setAttribute(IS_TOKEN_AUTH_KEY, (Object)Boolean.TRUE);
        this.getPrincipals(token).map(PrincipalCollection::getPrimaryPrincipal).ifPresent(principal -> ((NexusApiKeyAuthenticationToken)token).setPrincipal(principal));
    }

    protected boolean isAnonymousSupported() {
        return false;
    }

    private Optional<PrincipalCollection> getPrincipals(AuthenticationToken token) {
        return this.keyStore.getApiKeyByToken(this.format, (char[])token.getCredentials()).map(ApiKey::getPrincipals);
    }

    private boolean anonymousAndSupported(PrincipalCollection principals) {
        return ANONYMOUS_USER.equals(principals.getPrimaryPrincipal()) && this.isAnonymousSupported();
    }
}

