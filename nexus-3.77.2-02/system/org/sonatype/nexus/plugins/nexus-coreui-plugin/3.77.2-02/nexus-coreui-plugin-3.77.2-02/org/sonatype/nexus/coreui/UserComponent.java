/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.annotation.ExceptionMetered
 *  com.codahale.metrics.annotation.Timed
 *  com.google.common.base.Preconditions
 *  com.google.inject.Key
 *  com.softwarementors.extjs.djn.config.annotations.DirectAction
 *  com.softwarementors.extjs.djn.config.annotations.DirectMethod
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.validation.Valid
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 *  javax.validation.groups.Default
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.apache.shiro.authz.annotation.RequiresUser
 *  org.apache.shiro.subject.Subject
 *  org.eclipse.sisu.inject.BeanLocator
 *  org.sonatype.nexus.common.text.Strings2
 *  org.sonatype.nexus.common.wonderland.AuthTicketService
 *  org.sonatype.nexus.extdirect.DirectComponentSupport
 *  org.sonatype.nexus.extdirect.model.StoreLoadParameters
 *  org.sonatype.nexus.rapture.PasswordPlaceholder
 *  org.sonatype.nexus.security.SecuritySystem
 *  org.sonatype.nexus.security.anonymous.AnonymousConfiguration
 *  org.sonatype.nexus.security.anonymous.AnonymousManager
 *  org.sonatype.nexus.security.role.RoleIdentifier
 *  org.sonatype.nexus.security.user.NoSuchUserManagerException
 *  org.sonatype.nexus.security.user.User
 *  org.sonatype.nexus.security.user.UserManager
 *  org.sonatype.nexus.security.user.UserNotFoundException
 *  org.sonatype.nexus.security.user.UserSearchCriteria
 *  org.sonatype.nexus.validation.Validate
 *  org.sonatype.nexus.validation.group.Create
 *  org.sonatype.nexus.validation.group.Update
 */
package org.sonatype.nexus.coreui;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Preconditions;
import com.google.inject.Key;
import com.softwarementors.extjs.djn.config.annotations.DirectAction;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.eclipse.sisu.inject.BeanLocator;
import org.sonatype.nexus.common.text.Strings2;
import org.sonatype.nexus.common.wonderland.AuthTicketService;
import org.sonatype.nexus.coreui.ReferenceXO;
import org.sonatype.nexus.coreui.UserRoleMappingsXO;
import org.sonatype.nexus.coreui.UserXO;
import org.sonatype.nexus.extdirect.DirectComponentSupport;
import org.sonatype.nexus.extdirect.model.StoreLoadParameters;
import org.sonatype.nexus.rapture.PasswordPlaceholder;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.anonymous.AnonymousConfiguration;
import org.sonatype.nexus.security.anonymous.AnonymousManager;
import org.sonatype.nexus.security.role.RoleIdentifier;
import org.sonatype.nexus.security.user.NoSuchUserManagerException;
import org.sonatype.nexus.security.user.User;
import org.sonatype.nexus.security.user.UserManager;
import org.sonatype.nexus.security.user.UserNotFoundException;
import org.sonatype.nexus.security.user.UserSearchCriteria;
import org.sonatype.nexus.validation.Validate;
import org.sonatype.nexus.validation.group.Create;
import org.sonatype.nexus.validation.group.Update;

@Named
@Singleton
@DirectAction(action={"coreui_User"})
public class UserComponent
extends DirectComponentSupport {
    private final SecuritySystem securitySystem;
    private final AnonymousManager anonymousManager;
    private final AuthTicketService authTickets;
    private final BeanLocator beanLocator;

    @Inject
    public UserComponent(SecuritySystem securitySystem, AnonymousManager anonymousManager, AuthTicketService authTickets, BeanLocator beanLocator) {
        this.securitySystem = (SecuritySystem)Preconditions.checkNotNull((Object)securitySystem);
        this.anonymousManager = (AnonymousManager)Preconditions.checkNotNull((Object)anonymousManager);
        this.authTickets = (AuthTicketService)Preconditions.checkNotNull((Object)authTickets);
        this.beanLocator = (BeanLocator)Preconditions.checkNotNull((Object)beanLocator);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:users:read"})
    public UserXO get(String userId, String source) throws UserNotFoundException, NoSuchUserManagerException {
        return this.convert(this.securitySystem.getUser(userId, source));
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:users:read"})
    public List<UserXO> read(@Nullable StoreLoadParameters parameters) {
        Optional<StoreLoadParameters> optParameters = Optional.ofNullable(parameters);
        String source = optParameters.map(p -> p.getFilter("source")).orElse("default");
        String userId = optParameters.map(p -> p.getFilter("userId")).orElse(null);
        Integer limit = optParameters.map(StoreLoadParameters::getLimit).orElse(null);
        UserSearchCriteria searchCriteria = new UserSearchCriteria();
        searchCriteria.setSource(source);
        searchCriteria.setUserId(userId);
        searchCriteria.setLimit(limit);
        return this.securitySystem.searchUsers(searchCriteria).stream().map(this::convert).collect(Collectors.toList());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:users:read"})
    public List<ReferenceXO> readSources() {
        return StreamSupport.stream(this.beanLocator.locate(Key.get(UserManager.class, Named.class)).spliterator(), false).map(entry -> new ReferenceXO(((Named)entry.getKey()).value(), Strings2.isBlank((String)entry.getDescription()) ? ((Named)entry.getKey()).value() : entry.getDescription())).collect(Collectors.toList());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:users:create"})
    @Validate(groups={Create.class, Default.class})
    public UserXO create(@NotNull @Valid UserXO userXO) throws NoSuchUserManagerException {
        User user = new User();
        user.setUserId(userXO.getUserId());
        user.setSource("default");
        user.setFirstName(userXO.getFirstName());
        user.setLastName(userXO.getLastName());
        user.setEmailAddress(userXO.getEmail());
        user.setStatus(userXO.getStatus());
        user.setRoles(this.getRoles(userXO));
        return this.convert(this.securitySystem.addUser(user, userXO.getPassword()));
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:users:update"})
    @Validate(groups={Update.class, Default.class})
    public UserXO update(@NotNull @Valid UserXO userXO) throws UserNotFoundException, NoSuchUserManagerException {
        User user = new User();
        user.setUserId(userXO.getUserId());
        user.setVersion(Integer.parseInt(userXO.getVersion()));
        user.setSource("default");
        user.setFirstName(userXO.getFirstName());
        user.setLastName(userXO.getLastName());
        user.setEmailAddress(userXO.getEmail());
        user.setStatus(userXO.getStatus());
        user.setRoles(this.getRoles(userXO));
        return this.convert(this.securitySystem.updateUser(user));
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:users:update"})
    @Validate(groups={Update.class, Default.class})
    public UserXO updateRoleMappings(@NotNull @Valid UserRoleMappingsXO userRoleMappingsXO) throws UserNotFoundException, NoSuchUserManagerException {
        Set<String> mappedRoles = userRoleMappingsXO.getRoles();
        if (mappedRoles != null && !mappedRoles.isEmpty()) {
            User user = this.securitySystem.getUser(userRoleMappingsXO.getUserId(), userRoleMappingsXO.getRealm());
            user.getRoles().forEach(role -> {
                if (role.getSource().equals(userRoleMappingsXO.getRealm())) {
                    mappedRoles.remove(role.getRoleId());
                }
            });
        }
        this.securitySystem.setUsersRoles(userRoleMappingsXO.getUserId(), userRoleMappingsXO.getRealm(), mappedRoles != null && !mappedRoles.isEmpty() ? mappedRoles.stream().map(roleId -> new RoleIdentifier("default", roleId)).collect(Collectors.toSet()) : null);
        return this.convert(this.securitySystem.getUser(userRoleMappingsXO.getUserId(), userRoleMappingsXO.getRealm()));
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresUser
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:userschangepw:create"})
    @Validate
    public void changePassword(@NotEmpty String authToken, @NotEmpty String userId, @NotEmpty String password) throws Exception {
        if (this.authTickets.redeemTicket(authToken)) {
            if (this.isAnonymousUser(userId)) {
                throw new Exception("Password cannot be changed for user " + userId + ", since is marked as the Anonymous user");
            }
        } else {
            throw new IllegalAccessException("Invalid authentication ticket");
        }
        this.securitySystem.changePassword(userId, password);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:users:delete"})
    @Validate
    public void remove(@NotEmpty String id, @NotEmpty String source) throws Exception {
        if (this.isAnonymousUser(id)) {
            throw new Exception("User " + id + " cannot be deleted, since is marked as the Anonymous user");
        }
        if (this.isCurrentUser(id)) {
            throw new Exception("User " + id + " cannot be deleted, since is the user currently logged into the application");
        }
        this.securitySystem.deleteUser(id, source);
    }

    private UserXO convert(User user) {
        UserXO userXO = new UserXO();
        userXO.setUserId(user.getUserId());
        userXO.setVersion(String.valueOf(user.getVersion()));
        userXO.setRealm(user.getSource());
        userXO.setFirstName(user.getFirstName());
        userXO.setLastName(user.getLastName());
        userXO.setEmail(user.getEmailAddress());
        userXO.setStatus(user.getStatus());
        userXO.setPassword(PasswordPlaceholder.get());
        userXO.setRoles(user.getRoles().stream().map(RoleIdentifier::getRoleId).collect(Collectors.toSet()));
        userXO.setExternal(!"default".equals(user.getSource()));
        if (Boolean.TRUE.equals(userXO.isExternal())) {
            userXO.setExternalRoles(user.getRoles().stream().filter(role -> !"default".equals(role.getSource())).map(RoleIdentifier::getRoleId).collect(Collectors.toSet()));
        }
        return userXO;
    }

    private boolean isAnonymousUser(String userId) {
        AnonymousConfiguration config = this.anonymousManager.getConfiguration();
        return config.isEnabled() && config.getUserId().equals(userId);
    }

    private boolean isCurrentUser(String userId) {
        Subject subject = this.securitySystem.getSubject();
        if (subject == null || subject.getPrincipal() == null) {
            return false;
        }
        return subject.getPrincipal().equals(userId);
    }

    private Set<RoleIdentifier> getRoles(UserXO userXO) {
        if (userXO.getRoles() == null) {
            return null;
        }
        return userXO.getRoles().stream().map(id -> new RoleIdentifier("default", id)).collect(Collectors.toSet());
    }
}

