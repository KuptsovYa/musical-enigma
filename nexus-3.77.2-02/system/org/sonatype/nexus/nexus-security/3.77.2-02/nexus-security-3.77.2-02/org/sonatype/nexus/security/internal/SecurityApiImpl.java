/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.Sets
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.goodies.common.ComponentSupport
 */
package org.sonatype.nexus.security.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.security.SecurityApi;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.anonymous.AnonymousConfiguration;
import org.sonatype.nexus.security.anonymous.AnonymousManager;
import org.sonatype.nexus.security.authz.NoSuchAuthorizationManagerException;
import org.sonatype.nexus.security.role.Role;
import org.sonatype.nexus.security.role.RoleIdentifier;
import org.sonatype.nexus.security.user.NoSuchUserManagerException;
import org.sonatype.nexus.security.user.User;
import org.sonatype.nexus.security.user.UserNotFoundException;
import org.sonatype.nexus.security.user.UserStatus;

@Named
@Singleton
public class SecurityApiImpl
extends ComponentSupport
implements SecurityApi {
    private final AnonymousManager anonymousManager;
    private final SecuritySystem securitySystem;

    @Inject
    public SecurityApiImpl(AnonymousManager anonymousManager, SecuritySystem securitySystem) {
        this.anonymousManager = anonymousManager;
        this.securitySystem = securitySystem;
    }

    @Override
    public AnonymousConfiguration setAnonymousAccess(boolean enabled) {
        AnonymousConfiguration anonymousConfiguration = this.anonymousManager.getConfiguration();
        if (!this.anonymousManager.isConfigured() || anonymousConfiguration.isEnabled() != enabled) {
            anonymousConfiguration.setEnabled(enabled);
            this.anonymousManager.setConfiguration(anonymousConfiguration);
            this.log.info("Anonymous access configuration updated to: {}", (Object)anonymousConfiguration);
        } else {
            this.log.info("Anonymous access configuration unchanged at: {}", (Object)anonymousConfiguration);
        }
        return anonymousConfiguration;
    }

    @Override
    public User addUser(String id, String firstName, String lastName, String email, boolean active, String password, List<String> roleIds) throws NoSuchUserManagerException {
        User user = new User();
        user.setUserId((String)Preconditions.checkNotNull((Object)id));
        user.setSource("default");
        user.setFirstName((String)Preconditions.checkNotNull((Object)firstName));
        user.setLastName((String)Preconditions.checkNotNull((Object)lastName));
        user.setEmailAddress((String)Preconditions.checkNotNull((Object)email));
        user.setStatus(active ? UserStatus.active : UserStatus.disabled);
        user.setRoles(SecurityApiImpl.toIdentifiers(roleIds));
        return this.securitySystem.addUser(user, password);
    }

    @Override
    public Role addRole(String id, String name, String description, List<String> privileges, List<String> roles) throws NoSuchAuthorizationManagerException {
        Role role = new Role();
        role.setRoleId((String)Preconditions.checkNotNull((Object)id));
        role.setSource("default");
        role.setName((String)Preconditions.checkNotNull((Object)name));
        role.setDescription(description);
        role.setPrivileges(Sets.newHashSet((Iterable)((Iterable)Preconditions.checkNotNull(privileges))));
        role.setRoles(Sets.newHashSet((Iterable)((Iterable)Preconditions.checkNotNull(roles))));
        return this.securitySystem.getAuthorizationManager("default").addRole(role);
    }

    @Override
    public User setUserRoles(String userId, List<String> roleIds) throws UserNotFoundException, NoSuchUserManagerException {
        User user = this.securitySystem.getUser(userId, "default");
        user.setRoles(SecurityApiImpl.toIdentifiers(roleIds));
        return this.securitySystem.updateUser(user);
    }

    private static Set<RoleIdentifier> toIdentifiers(Collection<String> roleIds) {
        return ((Collection)Preconditions.checkNotNull(roleIds)).stream().map(roleId -> new RoleIdentifier("default", (String)roleId)).collect(Collectors.toSet());
    }
}

