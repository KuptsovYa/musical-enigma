/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.apache.shiro.SecurityUtils
 *  org.apache.shiro.authc.AuthenticationException
 *  org.apache.shiro.authc.AuthenticationToken
 *  org.apache.shiro.authc.UsernamePasswordToken
 *  org.apache.shiro.authz.AuthorizationException
 *  org.apache.shiro.mgt.RealmSecurityManager
 *  org.apache.shiro.mgt.SecurityManager
 *  org.apache.shiro.realm.Realm
 *  org.apache.shiro.subject.PrincipalCollection
 *  org.apache.shiro.subject.Subject
 *  org.apache.shiro.util.Destroyable
 *  org.apache.shiro.util.LifecycleUtils
 *  org.sonatype.nexus.common.app.ManagedLifecycle
 *  org.sonatype.nexus.common.app.ManagedLifecycle$Phase
 *  org.sonatype.nexus.common.event.EventManager
 *  org.sonatype.nexus.common.stateguard.StateGuardLifecycleSupport
 *  org.sonatype.nexus.common.text.Strings2
 */
package org.sonatype.nexus.security.internal;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.crypto.Cipher;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Destroyable;
import org.apache.shiro.util.LifecycleUtils;
import org.sonatype.nexus.common.app.ManagedLifecycle;
import org.sonatype.nexus.common.event.EventManager;
import org.sonatype.nexus.common.stateguard.StateGuardLifecycleSupport;
import org.sonatype.nexus.common.text.Strings2;
import org.sonatype.nexus.security.SecurityHelper;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.UserIdHelper;
import org.sonatype.nexus.security.UserPrincipalsExpired;
import org.sonatype.nexus.security.anonymous.AnonymousConfiguration;
import org.sonatype.nexus.security.anonymous.AnonymousHelper;
import org.sonatype.nexus.security.anonymous.AnonymousManager;
import org.sonatype.nexus.security.authc.UserPasswordChanged;
import org.sonatype.nexus.security.authz.AuthorizationConfigurationChanged;
import org.sonatype.nexus.security.authz.AuthorizationManager;
import org.sonatype.nexus.security.authz.NoSuchAuthorizationManagerException;
import org.sonatype.nexus.security.privilege.Privilege;
import org.sonatype.nexus.security.realm.RealmManager;
import org.sonatype.nexus.security.realm.SecurityRealm;
import org.sonatype.nexus.security.role.Role;
import org.sonatype.nexus.security.role.RoleIdentifier;
import org.sonatype.nexus.security.user.InvalidCredentialsException;
import org.sonatype.nexus.security.user.NoSuchUserManagerException;
import org.sonatype.nexus.security.user.RoleMappingUserManager;
import org.sonatype.nexus.security.user.User;
import org.sonatype.nexus.security.user.UserManager;
import org.sonatype.nexus.security.user.UserNotFoundException;
import org.sonatype.nexus.security.user.UserSearchCriteria;

@Named(value="default")
@ManagedLifecycle(phase=ManagedLifecycle.Phase.SECURITY)
@Singleton
public class DefaultSecuritySystem
extends StateGuardLifecycleSupport
implements SecuritySystem {
    private static final String ALL_ROLES_KEY = "all";
    public static final String NEXUS_AUTHORIZING_REALM = "NexusAuthorizingRealm";
    public static final String NEXUS_AUTHENTICATING_REALM = "NexusAuthenticatingRealm";
    private final EventManager eventManager;
    private final RealmSecurityManager realmSecurityManager;
    private final RealmManager realmManager;
    private final AnonymousManager anonymousManager;
    private final Map<String, AuthorizationManager> authorizationManagers;
    private final Map<String, UserManager> userManagers;
    private final SecurityHelper securityHelper;

    @Inject
    public DefaultSecuritySystem(EventManager eventManager, RealmSecurityManager realmSecurityManager, RealmManager realmManager, AnonymousManager anonymousManager, Map<String, AuthorizationManager> authorizationManagers, Map<String, UserManager> userManagers, SecurityHelper securityHelper) {
        this.eventManager = (EventManager)Preconditions.checkNotNull((Object)eventManager);
        this.realmSecurityManager = (RealmSecurityManager)Preconditions.checkNotNull((Object)realmSecurityManager);
        this.realmManager = (RealmManager)Preconditions.checkNotNull((Object)realmManager);
        this.anonymousManager = (AnonymousManager)Preconditions.checkNotNull((Object)anonymousManager);
        this.authorizationManagers = (Map)Preconditions.checkNotNull(authorizationManagers);
        this.userManagers = (Map)Preconditions.checkNotNull(userManagers);
        this.securityHelper = (SecurityHelper)((Object)Preconditions.checkNotNull((Object)((Object)securityHelper)));
    }

    protected void doStart() throws Exception {
        if (Cipher.getMaxAllowedKeyLength("AES") == Integer.MAX_VALUE) {
            this.log.info("Unlimited strength JCE policy detected");
        }
        SecurityUtils.setSecurityManager((SecurityManager)this.realmSecurityManager);
        this.realmManager.start();
    }

    protected void doStop() throws Exception {
        this.realmManager.stop();
        LifecycleUtils.destroy((Destroyable)this.realmSecurityManager);
    }

    @Override
    public Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    @Override
    public boolean isPermitted(PrincipalCollection principal, String permission) {
        return this.realmSecurityManager.isPermitted(principal, permission);
    }

    @Override
    public boolean[] isPermitted(PrincipalCollection principal, List<String> permissions) {
        return this.realmSecurityManager.isPermitted(principal, permissions.toArray(new String[permissions.size()]));
    }

    @Override
    public void checkPermission(PrincipalCollection principal, String permission) {
        this.realmSecurityManager.checkPermission(principal, permission);
    }

    @Override
    public Set<Role> listRoles() {
        HashSet<Role> result = new HashSet<Role>();
        for (AuthorizationManager authzManager : this.authorizationManagers.values()) {
            Set<Role> roles = authzManager.listRoles();
            if (roles == null) continue;
            result.addAll(roles);
        }
        return result;
    }

    @Override
    public Set<Role> listRoles(String sourceId) throws NoSuchAuthorizationManagerException {
        if (ALL_ROLES_KEY.equalsIgnoreCase(sourceId)) {
            return this.listRoles();
        }
        AuthorizationManager authzManager = this.getAuthorizationManager(sourceId);
        return authzManager.listRoles();
    }

    @Override
    public Set<Role> searchRoles(String sourceId, String query) throws NoSuchAuthorizationManagerException {
        AuthorizationManager authzManager = this.getAuthorizationManager(sourceId);
        return authzManager.searchRoles(query);
    }

    @Override
    public Set<Privilege> listPrivileges() {
        HashSet<Privilege> result = new HashSet<Privilege>();
        for (AuthorizationManager authzManager : this.authorizationManagers.values()) {
            Set<Privilege> privileges = authzManager.listPrivileges();
            if (privileges == null) continue;
            result.addAll(privileges);
        }
        return result;
    }

    @Override
    public User addUser(User user, String password) throws NoSuchUserManagerException {
        UserManager userManager = this.getUserManager(user.getSource());
        if (!userManager.supportsWrite()) {
            throw new RuntimeException("UserManager: " + userManager.getSource() + " does not support writing.");
        }
        userManager.addUser(user, password);
        for (UserManager tmpUserManager : this.getUserManagers()) {
            if (tmpUserManager.getSource().equals(user.getSource()) || !RoleMappingUserManager.class.isInstance(tmpUserManager)) continue;
            try {
                RoleMappingUserManager roleMappingUserManager = (RoleMappingUserManager)tmpUserManager;
                roleMappingUserManager.setUsersRoles(user.getUserId(), user.getSource(), RoleIdentifier.getRoleIdentifiersForSource(user.getSource(), user.getRoles()));
            }
            catch (UserNotFoundException e) {
                this.log.debug("User '{}' is not managed by the user-manager: {}", (Object)user.getUserId(), (Object)tmpUserManager.getSource());
            }
        }
        return user;
    }

    @Override
    public User updateUser(User user) throws UserNotFoundException, NoSuchUserManagerException {
        UserManager userManager = this.getUserManager(user.getSource());
        if (!userManager.supportsWrite()) {
            throw new RuntimeException("UserManager: " + userManager.getSource() + " does not support writing.");
        }
        User oldUser = userManager.getUser(user.getUserId());
        userManager.updateUser(user);
        if (oldUser.getStatus().isActive() && user.getStatus() != oldUser.getStatus()) {
            this.eventManager.post((Object)new UserPrincipalsExpired(user.getUserId(), user.getSource()));
        }
        for (UserManager tmpUserManager : this.getUserManagers()) {
            if (tmpUserManager.getSource().equals(user.getSource()) || !RoleMappingUserManager.class.isInstance(tmpUserManager)) continue;
            try {
                RoleMappingUserManager roleMappingUserManager = (RoleMappingUserManager)tmpUserManager;
                roleMappingUserManager.setUsersRoles(user.getUserId(), user.getSource(), RoleIdentifier.getRoleIdentifiersForSource(user.getSource(), user.getRoles()));
            }
            catch (UserNotFoundException e) {
                this.log.debug("User '{}' is not managed by the user-manager: {}", (Object)user.getUserId(), (Object)tmpUserManager.getSource());
            }
        }
        this.eventManager.post((Object)new AuthorizationConfigurationChanged());
        return user;
    }

    @Override
    public void deleteUser(String userId) throws UserNotFoundException {
        User user = this.getUser(userId);
        try {
            this.deleteUser(userId, user.getSource());
        }
        catch (NoSuchUserManagerException e) {
            this.log.error("User manager returned user, but could not be found: {}", (Object)e.getMessage(), (Object)e);
            throw new IllegalStateException("User manager returned user, but could not be found: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteUser(String userId, String source) throws UserNotFoundException, NoSuchUserManagerException {
        Preconditions.checkNotNull((Object)userId, (Object)"User ID may not be null");
        Subject subject = this.getSubject();
        if (subject.getPrincipal() != null && userId.equals(subject.getPrincipal().toString())) {
            throw new IllegalArgumentException("Can not delete currently signed in user");
        }
        AnonymousConfiguration anonymousConfiguration = this.anonymousManager.getConfiguration();
        if (anonymousConfiguration.isEnabled() && userId.equals(anonymousConfiguration.getUserId())) {
            throw new IllegalArgumentException("Can not delete anonymous user");
        }
        UserManager userManager = this.getUserManager(source);
        userManager.deleteUser(userId);
        this.eventManager.post((Object)new UserPrincipalsExpired(userId, source));
    }

    @Override
    public void setUsersRoles(String userId, String source, Set<RoleIdentifier> roleIdentifiers) throws UserNotFoundException {
        boolean foundUser = false;
        for (UserManager userManager : this.getUserManagers()) {
            if (!RoleMappingUserManager.class.isInstance(userManager)) continue;
            RoleMappingUserManager roleMappingUserManager = (RoleMappingUserManager)userManager;
            try {
                foundUser = true;
                roleMappingUserManager.setUsersRoles(userId, source, RoleIdentifier.getRoleIdentifiersForSource(userManager.getSource(), roleIdentifiers));
            }
            catch (UserNotFoundException e) {
                this.log.debug("User '{}' is not managed by the user-manager: {}", (Object)userId, (Object)userManager.getSource());
            }
        }
        if (!foundUser) {
            throw new UserNotFoundException(userId);
        }
        this.eventManager.post((Object)new AuthorizationConfigurationChanged());
    }

    private User findUser(String userId, UserManager userManager, Set<String> roleIds) throws UserNotFoundException {
        this.log.trace("Finding user: {} in user-manager: {}", (Object)userId, (Object)userManager);
        User user = userManager.getUser(userId, roleIds);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        this.log.trace("Found user: {}", (Object)user);
        this.addOtherRolesToUser(user);
        return user;
    }

    @Override
    @Nullable
    public User currentUser() throws UserNotFoundException {
        Subject subject = this.getSubject();
        if (subject.getPrincipal() == null) {
            return null;
        }
        String userId = subject.getPrincipal().toString();
        Optional realm = subject.getPrincipals().getRealmNames().stream().findFirst();
        try {
            if (realm.isPresent()) {
                return this.findUser(userId, this.getUserManagerByRealm((String)realm.get()), null);
            }
        }
        catch (NoSuchUserManagerException e) {
            this.log.trace("User: '{}' of source: '{}' could not be found.", (Object)userId, realm.get());
        }
        return this.getUser(subject.getPrincipal().toString());
    }

    @Override
    public User getUser(String userId) throws UserNotFoundException {
        this.log.trace("Finding user: {}", (Object)userId);
        for (UserManager userManager : this.orderUserManagers()) {
            try {
                return this.findUser(userId, userManager, null);
            }
            catch (UserNotFoundException e) {
                this.log.trace("User: '{}' was not found in: '{}'", new Object[]{userId, userManager, e});
            }
        }
        this.log.trace("User not found: {}", (Object)userId);
        throw new UserNotFoundException(userId);
    }

    @Override
    public User getUser(String userId, String source) throws UserNotFoundException, NoSuchUserManagerException {
        return this.getUser(userId, source, null);
    }

    @Override
    public User getUser(String userId, String source, Set<String> roleIds) throws UserNotFoundException, NoSuchUserManagerException {
        this.log.trace("Finding user: {} in source: {}", (Object)userId, (Object)source);
        UserManager userManager = this.getUserManager(source);
        return this.findUser(userId, userManager, roleIds);
    }

    @Override
    public Set<User> listUsers() {
        HashSet<User> result = new HashSet<User>();
        for (UserManager userManager : this.getUserManagers()) {
            result.addAll(userManager.listUsers());
        }
        for (User user : result) {
            this.addOtherRolesToUser(user);
        }
        return result;
    }

    @Override
    public Set<User> searchUsers(UserSearchCriteria criteria) {
        HashSet<User> result = new HashSet<User>();
        if (Strings2.isBlank((String)criteria.getSource())) {
            for (UserManager userManager : this.getUserManagers()) {
                Set<User> users = userManager.searchUsers(criteria);
                if (users == null) continue;
                result.addAll(users);
            }
        } else {
            try {
                result.addAll(this.getUserManager(criteria.getSource()).searchUsers(criteria));
            }
            catch (NoSuchUserManagerException e) {
                this.log.warn("UserManager: {} was not found.", (Object)criteria.getSource(), (Object)e);
            }
        }
        for (User user : result) {
            this.addOtherRolesToUser(user);
        }
        return result;
    }

    private List<UserManager> orderUserManagers() {
        ArrayList<UserManager> orderedLocators = new ArrayList<UserManager>();
        ArrayList<UserManager> unOrderdLocators = new ArrayList<UserManager>(this.getUserManagers());
        HashMap<String, UserManager> realmToUserManagerMap = new HashMap<String, UserManager>();
        for (UserManager userManager : this.getUserManagers()) {
            if (userManager.getAuthenticationRealmName() == null) continue;
            realmToUserManagerMap.put(userManager.getAuthenticationRealmName(), userManager);
        }
        Collection realms = this.realmSecurityManager.getRealms();
        for (Realm realm : realms) {
            if (!realmToUserManagerMap.containsKey(realm.getName())) continue;
            UserManager userManager = (UserManager)realmToUserManagerMap.get(realm.getName());
            unOrderdLocators.remove(userManager);
            orderedLocators.add(userManager);
        }
        orderedLocators.addAll(unOrderdLocators);
        return orderedLocators;
    }

    private void addOtherRolesToUser(User user) {
        for (UserManager userManager : this.getUserManagers()) {
            if (userManager.getSource().equals(user.getSource()) || !RoleMappingUserManager.class.isInstance(userManager)) continue;
            try {
                RoleMappingUserManager roleMappingUserManager = (RoleMappingUserManager)userManager;
                Set<RoleIdentifier> roleIdentifiers = roleMappingUserManager.getUsersRoles(user.getUserId(), user.getSource());
                if (roleIdentifiers == null) continue;
                user.addAllRoles(roleIdentifiers);
            }
            catch (UserNotFoundException e) {
                this.log.debug("User '{}' is not managed by the user-manager: {}", (Object)user.getUserId(), (Object)userManager.getSource());
            }
        }
    }

    @Override
    public AuthorizationManager getAuthorizationManager(String source) throws NoSuchAuthorizationManagerException {
        if (!this.authorizationManagers.containsKey(source)) {
            throw new NoSuchAuthorizationManagerException(source);
        }
        return this.authorizationManagers.get(source);
    }

    @Override
    public void changePassword(String userId, String oldPassword, String newPassword) throws UserNotFoundException, InvalidCredentialsException {
        try {
            UsernamePasswordToken authenticationToken = new UsernamePasswordToken(userId, oldPassword);
            if (this.realmSecurityManager.authenticate((AuthenticationToken)authenticationToken) == null) {
                throw new InvalidCredentialsException();
            }
        }
        catch (AuthenticationException e) {
            this.log.debug("User failed to change password reason: " + e.getMessage(), (Throwable)e);
            throw new InvalidCredentialsException();
        }
        this.changePassword(userId, newPassword);
    }

    @Override
    public void changePassword(String userId, String newPassword) throws UserNotFoundException {
        this.changePassword(userId, newPassword, true);
    }

    @Override
    public void changePassword(String userId, String newPassword, boolean clearCache) throws UserNotFoundException {
        this.requirePermissionToChangeUserPassword(userId);
        User user = this.getUser(userId);
        try {
            UserManager userManager = this.getUserManager(user.getSource());
            userManager.changePassword(userId, newPassword);
        }
        catch (NoSuchUserManagerException e) {
            this.log.warn("User '{}' with source: '{}' but could not find the user-manager for that source.", (Object)userId, (Object)user.getSource());
        }
        this.eventManager.post((Object)new UserPasswordChanged(userId, clearCache));
    }

    public void requirePermissionToChangeUserPassword(String userId) {
        if (!this.isPermittedToChangeUserPassword(userId)) {
            throw new AuthorizationException(String.format("%s is not permitted to change the password for %s", UserIdHelper.get(), userId));
        }
    }

    public boolean isPermittedToChangeUserPassword(String userId) {
        return UserIdHelper.get().equals(userId) || this.securityHelper.isAllPermitted();
    }

    private Collection<UserManager> getUserManagers() {
        return this.userManagers.values();
    }

    private UserManager getUserManagerByRealm(String realmName) throws NoSuchUserManagerException {
        return this.userManagers.values().stream().filter(userManager -> realmName.equalsIgnoreCase(userManager.getAuthenticationRealmName())).findFirst().orElseThrow(() -> new NoSuchUserManagerException(realmName));
    }

    @Override
    public UserManager getUserManager(String source) throws NoSuchUserManagerException {
        if (!this.userManagers.containsKey(source)) {
            throw new NoSuchUserManagerException(source);
        }
        return this.userManagers.get(source);
    }

    @Override
    public List<String> listSources() {
        return this.authorizationManagers.keySet().stream().sorted().collect(Collectors.toList());
    }

    @Override
    public boolean isValidRealm(String realm) {
        return !realm.isEmpty() && this.getAllRealmIds().stream().anyMatch(singleRealm -> singleRealm.equals(realm));
    }

    private List<String> getAllRealmIds() {
        List<String> authenticationRealms = AnonymousHelper.getAuthenticationRealms(new ArrayList<UserManager>(this.userManagers.values()));
        return this.realmManager.getAvailableRealms(true).stream().map(SecurityRealm::getId).filter(authenticationRealms::contains).map(id -> id.equals(NEXUS_AUTHORIZING_REALM) ? NEXUS_AUTHENTICATING_REALM : id).collect(Collectors.toList());
    }
}

