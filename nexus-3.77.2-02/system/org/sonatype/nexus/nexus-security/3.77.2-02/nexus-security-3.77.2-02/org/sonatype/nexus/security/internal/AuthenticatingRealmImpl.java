/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.apache.shiro.authc.AccountException
 *  org.apache.shiro.authc.AuthenticationInfo
 *  org.apache.shiro.authc.AuthenticationToken
 *  org.apache.shiro.authc.CredentialsException
 *  org.apache.shiro.authc.DisabledAccountException
 *  org.apache.shiro.authc.SimpleAuthenticationInfo
 *  org.apache.shiro.authc.UnknownAccountException
 *  org.apache.shiro.authc.UsernamePasswordToken
 *  org.apache.shiro.authc.credential.CredentialsMatcher
 *  org.apache.shiro.authc.credential.PasswordMatcher
 *  org.apache.shiro.authc.credential.PasswordService
 *  org.apache.shiro.realm.AuthenticatingRealm
 *  org.apache.shiro.realm.Realm
 *  org.apache.shiro.subject.PrincipalCollection
 *  org.apache.shiro.subject.SimplePrincipalCollection
 *  org.eclipse.sisu.Description
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.sonatype.nexus.security.internal;

import java.util.ConcurrentModificationException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.eclipse.sisu.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.security.NexusSimpleAuthenticationInfo;
import org.sonatype.nexus.security.RealmCaseMapping;
import org.sonatype.nexus.security.config.CUser;
import org.sonatype.nexus.security.config.SecurityConfigurationManager;
import org.sonatype.nexus.security.user.UserNotFoundException;

@Singleton
@Named(value="NexusAuthenticatingRealm")
@Description(value="Local Authenticating Realm")
public class AuthenticatingRealmImpl
extends AuthenticatingRealm
implements Realm {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticatingRealmImpl.class);
    public static final String NAME = "NexusAuthenticatingRealm";
    private static final int MAX_LEGACY_PASSWORD_LENGTH = 40;
    private final SecurityConfigurationManager configuration;
    private final PasswordService passwordService;
    private final boolean orient;

    @Inject
    public AuthenticatingRealmImpl(SecurityConfigurationManager configuration, PasswordService passwordService, @Named(value="${nexus.orient.enabled:-false}") boolean orient) {
        this.configuration = configuration;
        this.passwordService = passwordService;
        PasswordMatcher passwordMatcher = new PasswordMatcher();
        passwordMatcher.setPasswordService(this.passwordService);
        this.setCredentialsMatcher((CredentialsMatcher)passwordMatcher);
        this.setName(NAME);
        this.setAuthenticationCachingEnabled(true);
        this.orient = orient;
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        CUser user;
        UsernamePasswordToken upToken = (UsernamePasswordToken)token;
        try {
            user = this.configuration.readUser(upToken.getUsername());
        }
        catch (UserNotFoundException e) {
            throw new UnknownAccountException("User '" + upToken.getUsername() + "' cannot be retrieved.", (Throwable)e);
        }
        if (user.getPassword() == null) {
            throw new CredentialsException("User '" + upToken.getUsername() + "' has no password, cannot authenticate.");
        }
        if (user.isActive()) {
            if (this.hasLegacyPassword(user) && this.isValidCredentials(upToken, user)) {
                this.reHashPassword(user, new String(upToken.getPassword()));
            }
            return this.createAuthenticationInfo(user);
        }
        if ("disabled".equals(user.getStatus())) {
            throw new DisabledAccountException("User '" + upToken.getUsername() + "' is disabled.");
        }
        throw new AccountException("User '" + upToken.getUsername() + "' is in illegal status '" + user.getStatus() + "'.");
    }

    private void reHashPassword(CUser user, String password) {
        String hashedPassword = this.passwordService.encryptPassword((Object)password);
        try {
            boolean updated = false;
            do {
                CUser toUpdate = this.configuration.readUser(user.getId());
                toUpdate.setPassword(hashedPassword);
                try {
                    this.configuration.updateUser(toUpdate);
                    updated = true;
                }
                catch (ConcurrentModificationException e) {
                    logger.debug("Could not re-hash user '{}' password as user was concurrently being updated. Retrying...", (Object)user.getId());
                }
            } while (!updated);
            user.setPassword(hashedPassword);
        }
        catch (Exception e) {
            logger.error("Unable to update hash for user {}", (Object)user.getId(), (Object)e);
        }
    }

    private boolean isValidCredentials(UsernamePasswordToken token, CUser user) {
        boolean credentialsValid = false;
        AuthenticationInfo info = this.createAuthenticationInfo(user);
        CredentialsMatcher matcher = this.getCredentialsMatcher();
        if (matcher != null && matcher.doCredentialsMatch((AuthenticationToken)token, info)) {
            credentialsValid = true;
        }
        return credentialsValid;
    }

    private boolean hasLegacyPassword(CUser user) {
        return user.getPassword().length() <= 40;
    }

    private AuthenticationInfo createAuthenticationInfo(CUser user) {
        return this.orient ? new NexusSimpleAuthenticationInfo(user.getId(), user.getPassword().toCharArray(), new RealmCaseMapping(this.getName(), true)) : new SimpleAuthenticationInfo((Object)user.getId(), (Object)user.getPassword().toCharArray(), this.getName());
    }

    protected void clearCache(String userId) {
        this.clearCache((PrincipalCollection)new SimplePrincipalCollection((Object)userId, NAME));
    }
}

