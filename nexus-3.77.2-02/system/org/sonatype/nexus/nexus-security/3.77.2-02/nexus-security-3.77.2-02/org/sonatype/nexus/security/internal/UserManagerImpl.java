/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.Sets
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.apache.shiro.authc.credential.PasswordService
 *  org.eclipse.sisu.Description
 *  org.sonatype.nexus.common.event.EventManager
 */
package org.sonatype.nexus.security.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.shiro.authc.credential.PasswordService;
import org.eclipse.sisu.Description;
import org.sonatype.nexus.common.event.EventManager;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.config.CRole;
import org.sonatype.nexus.security.config.CUser;
import org.sonatype.nexus.security.config.CUserRoleMapping;
import org.sonatype.nexus.security.config.SecurityConfigurationManager;
import org.sonatype.nexus.security.internal.PasswordValidator;
import org.sonatype.nexus.security.role.NoSuchRoleException;
import org.sonatype.nexus.security.role.RoleIdentifier;
import org.sonatype.nexus.security.user.AbstractUserManager;
import org.sonatype.nexus.security.user.NoSuchRoleMappingException;
import org.sonatype.nexus.security.user.NoSuchUserManagerException;
import org.sonatype.nexus.security.user.RoleMappingUserManager;
import org.sonatype.nexus.security.user.User;
import org.sonatype.nexus.security.user.UserCreatedEvent;
import org.sonatype.nexus.security.user.UserDeletedEvent;
import org.sonatype.nexus.security.user.UserNotFoundException;
import org.sonatype.nexus.security.user.UserRoleMappingCreatedEvent;
import org.sonatype.nexus.security.user.UserRoleMappingDeletedEvent;
import org.sonatype.nexus.security.user.UserRoleMappingUpdatedEvent;
import org.sonatype.nexus.security.user.UserSearchCriteria;
import org.sonatype.nexus.security.user.UserStatus;
import org.sonatype.nexus.security.user.UserUpdatedEvent;

@Named(value="default")
@Singleton
@Description(value="Local")
public class UserManagerImpl
extends AbstractUserManager
implements RoleMappingUserManager {
    private final EventManager eventManager;
    private final SecurityConfigurationManager configuration;
    private final SecuritySystem securitySystem;
    private final PasswordService passwordService;
    private final PasswordValidator passwordValidator;

    @Inject
    public UserManagerImpl(EventManager eventManager, SecurityConfigurationManager configuration, SecuritySystem securitySystem, PasswordService passwordService, PasswordValidator passwordValidator) {
        this.eventManager = (EventManager)Preconditions.checkNotNull((Object)eventManager);
        this.configuration = configuration;
        this.securitySystem = securitySystem;
        this.passwordService = passwordService;
        this.passwordValidator = passwordValidator;
    }

    protected CUser toUser(User user) {
        if (user == null) {
            return null;
        }
        CUser secUser = this.configuration.newUser();
        secUser.setId(user.getUserId());
        secUser.setVersion(user.getVersion());
        secUser.setFirstName(user.getFirstName());
        secUser.setLastName(user.getLastName());
        secUser.setEmail(user.getEmailAddress());
        secUser.setStatus(user.getStatus().name());
        return secUser;
    }

    protected User toUser(CUser cUser, Set<String> roleIds) {
        if (cUser == null) {
            return null;
        }
        User user = new User();
        user.setUserId(cUser.getId());
        user.setVersion(cUser.getVersion());
        user.setFirstName(cUser.getFirstName());
        user.setLastName(cUser.getLastName());
        user.setEmailAddress(cUser.getEmail());
        user.setSource("default");
        user.setStatus(UserStatus.valueOf(cUser.getStatus()));
        try {
            if (roleIds != null) {
                user.setRoles(this.getUsersRoles(roleIds));
            } else {
                user.setRoles(this.getUsersRoles(cUser.getId(), "default"));
            }
        }
        catch (UserNotFoundException e) {
            this.log.warn("Could not find user: '{}' of source: '{}' while looking up the users roles.", new Object[]{cUser.getId(), "default", e});
        }
        return user;
    }

    protected RoleIdentifier toRole(String roleId, String source) {
        if (roleId == null) {
            return null;
        }
        try {
            CRole role = this.configuration.readRole(roleId);
            return new RoleIdentifier(source, role.getId());
        }
        catch (NoSuchRoleException e) {
            return null;
        }
    }

    @Override
    public Set<User> listUsers() {
        HashSet<User> users = new HashSet<User>();
        for (CUser user : this.configuration.listUsers()) {
            users.add(this.toUser(user, null));
        }
        return users;
    }

    @Override
    public Set<String> listUserIds() {
        HashSet<String> userIds = new HashSet<String>();
        for (CUser user : this.configuration.listUsers()) {
            userIds.add(user.getId());
        }
        return userIds;
    }

    @Override
    public User getUser(String userId) throws UserNotFoundException {
        return this.getUser(userId, null);
    }

    @Override
    public User getUser(String userId, Set<String> roleIds) throws UserNotFoundException {
        return this.toUser(this.configuration.readUser(userId), roleIds);
    }

    @Override
    public String getSource() {
        return "default";
    }

    @Override
    public boolean supportsWrite() {
        return true;
    }

    @Override
    public User addUser(User user, String password) {
        CUser secUser = this.toUser(user);
        secUser.setPassword(this.hashPassword(password));
        this.configuration.createUser(secUser, this.getRoleIdsFromUser(user));
        this.eventManager.post((Object)new UserCreatedEvent(user));
        return user;
    }

    @Override
    public void changePassword(String userId, String newPassword) throws UserNotFoundException {
        CUser secUser = this.configuration.readUser(userId);
        if ("changepassword".equals(secUser.getStatus())) {
            secUser.setStatus("active");
        }
        secUser.setPassword(this.hashPassword(newPassword));
        this.configuration.updateUser(secUser);
        User user = this.getUser(userId);
        this.eventManager.post((Object)new UserUpdatedEvent(user));
    }

    @Override
    public User updateUser(User user) throws UserNotFoundException {
        CUser oldSecUser = this.configuration.readUser(user.getUserId());
        CUser newSecUser = this.toUser(user);
        newSecUser.setPassword(oldSecUser.getPassword());
        this.configuration.updateUser(newSecUser, this.getRoleIdsFromUser(user));
        this.eventManager.post((Object)new UserUpdatedEvent(user));
        return user;
    }

    @Override
    public void deleteUser(String userId) throws UserNotFoundException {
        User user = this.getUser(userId);
        this.configuration.deleteUser(userId);
        this.eventManager.post((Object)new UserDeletedEvent(user));
    }

    @Override
    public Set<RoleIdentifier> getUsersRoles(String userId, String source) throws UserNotFoundException {
        Set<RoleIdentifier> roles = new HashSet<RoleIdentifier>();
        try {
            CUserRoleMapping roleMapping = this.configuration.readUserRoleMapping(userId, source);
            if (roleMapping != null) {
                roles = this.getUsersRoles(roleMapping.getRoles());
            }
        }
        catch (NoSuchRoleMappingException e) {
            this.log.debug("No user role mapping found for user: {}", (Object)userId);
        }
        return roles;
    }

    private Set<RoleIdentifier> getUsersRoles(Set<String> roleIds) {
        HashSet<RoleIdentifier> roles = new HashSet<RoleIdentifier>();
        for (String roleId : roleIds) {
            RoleIdentifier role = this.toRole(roleId, "default");
            if (role == null) continue;
            roles.add(role);
        }
        return roles;
    }

    @Override
    public Set<User> searchUsers(UserSearchCriteria criteria) {
        HashSet<User> users = new HashSet<User>();
        users.addAll(this.filterListInMemeory(this.listUsers(), criteria));
        if (criteria.getSource() == null) {
            List<CUserRoleMapping> roleMappings = this.configuration.listUserRoleMappings();
            for (CUserRoleMapping roleMapping : roleMappings) {
                if ("default".equals(roleMapping.getSource()) || !this.matchesCriteria(roleMapping.getUserId(), roleMapping.getSource(), roleMapping.getRoles(), criteria)) continue;
                try {
                    User user = this.securitySystem.getUser(roleMapping.getUserId(), roleMapping.getSource());
                    users.add(user);
                }
                catch (UserNotFoundException e) {
                    this.log.debug("User: '{}' of source: '{}' could not be found.", new Object[]{roleMapping.getUserId(), roleMapping.getSource(), e});
                }
                catch (NoSuchUserManagerException e) {
                    this.log.warn("User: '{}' of source: '{}' could not be found.", new Object[]{roleMapping.getUserId(), roleMapping.getSource(), e});
                }
            }
        }
        return users;
    }

    @Override
    public boolean isConfigured() {
        return true;
    }

    private String hashPassword(String clearPassword) {
        this.passwordValidator.validate(clearPassword);
        if (clearPassword != null && clearPassword.trim().length() > 0) {
            return this.passwordService.encryptPassword((Object)clearPassword);
        }
        return clearPassword;
    }

    @Override
    public void setUsersRoles(String userId, String userSource, Set<RoleIdentifier> roleIdentifiers) throws UserNotFoundException {
        if (roleIdentifiers == null || roleIdentifiers.isEmpty()) {
            try {
                this.configuration.deleteUserRoleMapping(userId, userSource);
                this.eventManager.post((Object)new UserRoleMappingDeletedEvent(userId, userSource));
            }
            catch (NoSuchRoleMappingException e) {
                this.log.debug("User role mapping for user: {} source: {} could not be deleted because it does not exist.", (Object)userId, (Object)userSource);
            }
        } else {
            try {
                CUserRoleMapping roleMapping = this.configuration.readUserRoleMapping(userId, userSource).clone();
                roleMapping.setRoles(Sets.newHashSet());
                this.updateRoles(roleMapping, roleIdentifiers);
                this.configuration.updateUserRoleMapping(roleMapping);
                this.eventManager.post((Object)new UserRoleMappingUpdatedEvent(userId, userSource, roleMapping.getRoles()));
            }
            catch (NoSuchRoleMappingException e) {
                CUserRoleMapping roleMapping = this.configuration.newUserRoleMapping();
                roleMapping.setUserId(userId);
                roleMapping.setSource(userSource);
                this.updateRoles(roleMapping, roleIdentifiers);
                this.log.debug("Update of user role mapping for user: {} source: {} did not exist, creating new one.", (Object)userId, (Object)userSource);
                this.configuration.createUserRoleMapping(roleMapping);
                this.eventManager.post((Object)new UserRoleMappingCreatedEvent(userId, userSource, roleMapping.getRoles()));
            }
        }
    }

    private void updateRoles(CUserRoleMapping roleMapping, Set<RoleIdentifier> roleIdentifiers) {
        for (RoleIdentifier roleIdentifier : roleIdentifiers) {
            if (!this.getSource().equals(roleIdentifier.getSource())) continue;
            roleMapping.addRole(roleIdentifier.getRoleId());
        }
    }

    @Override
    public String getAuthenticationRealmName() {
        return "NexusAuthenticatingRealm";
    }

    private Set<String> getRoleIdsFromUser(User user) {
        HashSet<String> roles = new HashSet<String>();
        for (RoleIdentifier roleIdentifier : user.getRoles()) {
            roles.add(roleIdentifier.getRoleId());
        }
        return roles;
    }
}

