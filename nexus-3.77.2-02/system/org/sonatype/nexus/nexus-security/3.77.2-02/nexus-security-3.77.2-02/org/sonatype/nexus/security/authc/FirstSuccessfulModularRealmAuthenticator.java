/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.shiro.SecurityUtils
 *  org.apache.shiro.authc.AuthenticationException
 *  org.apache.shiro.authc.AuthenticationInfo
 *  org.apache.shiro.authc.AuthenticationToken
 *  org.apache.shiro.authc.CredentialsException
 *  org.apache.shiro.authc.DisabledAccountException
 *  org.apache.shiro.authc.ExpiredCredentialsException
 *  org.apache.shiro.authc.IncorrectCredentialsException
 *  org.apache.shiro.authc.UnknownAccountException
 *  org.apache.shiro.authc.pam.ModularRealmAuthenticator
 *  org.apache.shiro.realm.Realm
 *  org.apache.shiro.subject.Subject
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.sonatype.nexus.security.authc;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.security.authc.AuthenticationFailureReason;
import org.sonatype.nexus.security.authc.NexusAuthenticationException;

public class FirstSuccessfulModularRealmAuthenticator
extends ModularRealmAuthenticator {
    private static final Logger log = LoggerFactory.getLogger(FirstSuccessfulModularRealmAuthenticator.class);

    protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) {
        log.trace("Iterating through [{}] realms for PAM authentication", (Object)realms.size());
        EnumSet<AuthenticationFailureReason> authenticationFailureReasons = EnumSet.noneOf(AuthenticationFailureReason.class);
        Subject subject = SecurityUtils.getSubject();
        for (Realm realm : realms) {
            if (realm.supports(token)) {
                log.trace("Attempting to authenticate token [{}] using realm of type [{}]", (Object)token, (Object)realm);
                try {
                    AuthenticationInfo info = realm.getAuthenticationInfo(token);
                    if (info != null) {
                        Set realmNames = info.getPrincipals().getRealmNames();
                        if (subject.isAuthenticated() && !subject.getPrincipals().getRealmNames().containsAll(realmNames)) continue;
                        return info;
                    }
                    log.trace("Realm [{}] returned null when authenticating token [{}]", (Object)realm, (Object)token);
                }
                catch (DisabledAccountException e) {
                    this.logExceptionForRealm(e, realm);
                    authenticationFailureReasons.add(AuthenticationFailureReason.DISABLED_ACCOUNT);
                }
                catch (ExpiredCredentialsException e) {
                    this.logExceptionForRealm(e, realm);
                    authenticationFailureReasons.add(AuthenticationFailureReason.EXPIRED_CREDENTIALS);
                }
                catch (IncorrectCredentialsException e) {
                    this.logExceptionForRealm(e, realm);
                    authenticationFailureReasons.add(AuthenticationFailureReason.INCORRECT_CREDENTIALS);
                }
                catch (UnknownAccountException e) {
                    this.logExceptionForRealm(e, realm);
                    authenticationFailureReasons.add(AuthenticationFailureReason.USER_NOT_FOUND);
                }
                catch (CredentialsException e) {
                    this.logExceptionForRealm(e, realm);
                    authenticationFailureReasons.add(AuthenticationFailureReason.PASSWORD_EMPTY);
                }
                catch (AuthenticationException e) {
                    this.logExceptionForRealm(e, realm);
                    authenticationFailureReasons.add(AuthenticationFailureReason.UNKNOWN);
                }
                catch (Throwable t) {
                    this.logExceptionForRealm(t, realm);
                }
                continue;
            }
            log.trace("Realm of type [{}] does not support token [{}]; skipping realm", (Object)realm, (Object)token);
        }
        throw new NexusAuthenticationException("Authentication token of type [" + token.getClass() + "] could not be authenticated by any configured realms.  Please ensure that at least one realm can authenticate these tokens.", authenticationFailureReasons);
    }

    private void logExceptionForRealm(Throwable t, Realm realm) {
        log.trace("Realm [{}] threw an exception during a multi-realm authentication attempt", (Object)realm, (Object)t);
    }
}

