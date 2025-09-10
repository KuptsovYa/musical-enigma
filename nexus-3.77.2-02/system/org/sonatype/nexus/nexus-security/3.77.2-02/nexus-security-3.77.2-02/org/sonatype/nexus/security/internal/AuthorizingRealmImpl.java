/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.apache.shiro.authc.AuthenticationInfo
 *  org.apache.shiro.authc.AuthenticationToken
 *  org.apache.shiro.authc.credential.CredentialsMatcher
 *  org.apache.shiro.authc.credential.HashedCredentialsMatcher
 *  org.apache.shiro.authz.AuthorizationException
 *  org.apache.shiro.authz.AuthorizationInfo
 *  org.apache.shiro.authz.SimpleAuthorizationInfo
 *  org.apache.shiro.mgt.RealmSecurityManager
 *  org.apache.shiro.realm.AuthorizingRealm
 *  org.apache.shiro.realm.Realm
 *  org.apache.shiro.subject.PrincipalCollection
 *  org.eclipse.sisu.Description
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.sonatype.nexus.security.internal;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.eclipse.sisu.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.security.role.RoleIdentifier;
import org.sonatype.nexus.security.user.RoleMappingUserManager;
import org.sonatype.nexus.security.user.UserManager;
import org.sonatype.nexus.security.user.UserNotFoundException;

@Singleton
@Named(value="NexusAuthorizingRealm")
@Description(value="Local Authorizing Realm")
public class AuthorizingRealmImpl
extends AuthorizingRealm
implements Realm {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizingRealmImpl.class);
    public static final String NAME = "NexusAuthorizingRealm";
    private final RealmSecurityManager realmSecurityManager;
    private final UserManager userManager;
    private final Map<String, UserManager> userManagerMap;

    @Inject
    public AuthorizingRealmImpl(RealmSecurityManager realmSecurityManager, UserManager userManager, Map<String, UserManager> userManagerMap) {
        this.realmSecurityManager = realmSecurityManager;
        this.userManager = userManager;
        this.userManagerMap = userManagerMap;
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("SHA-1");
        this.setCredentialsMatcher((CredentialsMatcher)credentialsMatcher);
        this.setName(NAME);
        this.setAuthenticationCachingEnabled(false);
        this.setAuthorizationCachingEnabled(true);
    }

    public boolean supports(AuthenticationToken token) {
        return false;
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        return null;
    }

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            throw new AuthorizationException("Cannot authorize with no principals.");
        }
        String username = principals.getPrimaryPrincipal().toString();
        HashSet<String> roles = new HashSet<String>();
        HashSet<String> realmNames = new HashSet<String>(principals.getRealmNames());
        if (!realmNames.contains(this.getName())) {
            Iterator<RoleIdentifier> configureadRealms = this.realmSecurityManager.getRealms();
            boolean foundRealm = false;
            Iterator<Object> iterator = configureadRealms.iterator();
            while (iterator.hasNext()) {
                Realm realm = (Realm)iterator.next();
                if (!realmNames.contains(realm.getName())) continue;
                foundRealm = true;
                break;
            }
            if (!foundRealm) {
                throw new AuthorizationException("User for principals: " + principals.getPrimaryPrincipal() + " belongs to a disabled realm(s): " + principals.getRealmNames() + ".");
            }
        }
        this.cleanUpRealmList(realmNames);
        if (RoleMappingUserManager.class.isInstance(this.userManager)) {
            for (String realmName : realmNames) {
                try {
                    for (RoleIdentifier roleIdentifier : ((RoleMappingUserManager)this.userManager).getUsersRoles(username, realmName)) {
                        roles.add(roleIdentifier.getRoleId());
                    }
                }
                catch (UserNotFoundException e) {
                    logger.trace("Failed to find role mappings for user: {} realm: {}", (Object)username, (Object)realmName);
                }
            }
        } else if (realmNames.contains("default")) {
            try {
                for (RoleIdentifier roleIdentifier : this.userManager.getUser(username).getRoles()) {
                    roles.add(roleIdentifier.getRoleId());
                }
            }
            catch (UserNotFoundException e) {
                throw new AuthorizationException("User for principals: " + principals.getPrimaryPrincipal() + " could not be found.", (Throwable)e);
            }
        } else {
            throw new AuthorizationException("User for principals: " + principals.getPrimaryPrincipal() + " not manged by Nexus realm.");
        }
        return new SimpleAuthorizationInfo(roles);
    }

    private void cleanUpRealmList(Set<String> realmNames) {
        for (UserManager userManager : this.userManagerMap.values()) {
            String authRealmName = userManager.getAuthenticationRealmName();
            if (authRealmName == null || !realmNames.contains(authRealmName)) continue;
            realmNames.remove(authRealmName);
            realmNames.add(userManager.getSource());
        }
        if (realmNames.contains(this.getName())) {
            realmNames.remove(this.getName());
            realmNames.add("default");
        }
    }
}

