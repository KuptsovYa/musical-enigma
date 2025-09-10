/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 *  com.google.common.collect.Sets$SetView
 *  com.google.common.eventbus.AllowConcurrentEvents
 *  com.google.common.eventbus.Subscribe
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.apache.shiro.authc.credential.PasswordService
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.event.EventAware
 *  org.sonatype.nexus.common.event.EventManager
 *  org.sonatype.nexus.common.text.Strings2
 */
package org.sonatype.nexus.security.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.shiro.authc.credential.PasswordService;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.event.EventAware;
import org.sonatype.nexus.common.event.EventManager;
import org.sonatype.nexus.common.text.Strings2;
import org.sonatype.nexus.security.authz.AuthorizationConfigurationChanged;
import org.sonatype.nexus.security.config.CPrivilege;
import org.sonatype.nexus.security.config.CRole;
import org.sonatype.nexus.security.config.CUser;
import org.sonatype.nexus.security.config.CUserRoleMapping;
import org.sonatype.nexus.security.config.MemorySecurityConfiguration;
import org.sonatype.nexus.security.config.SecurityConfiguration;
import org.sonatype.nexus.security.config.SecurityConfigurationCleaner;
import org.sonatype.nexus.security.config.SecurityConfigurationManager;
import org.sonatype.nexus.security.config.SecurityConfigurationSource;
import org.sonatype.nexus.security.config.SecurityContributor;
import org.sonatype.nexus.security.internal.SecurityContributionChangedEvent;
import org.sonatype.nexus.security.privilege.DuplicatePrivilegeException;
import org.sonatype.nexus.security.privilege.NoSuchPrivilegeException;
import org.sonatype.nexus.security.privilege.ReadonlyPrivilegeException;
import org.sonatype.nexus.security.role.DuplicateRoleException;
import org.sonatype.nexus.security.role.NoSuchRoleException;
import org.sonatype.nexus.security.role.ReadonlyRoleException;
import org.sonatype.nexus.security.role.RoleContainsItselfException;
import org.sonatype.nexus.security.user.NoSuchRoleMappingException;
import org.sonatype.nexus.security.user.UserNotFoundException;

@Named(value="default")
@Singleton
public class SecurityConfigurationManagerImpl
extends ComponentSupport
implements SecurityConfigurationManager,
EventAware {
    private final SecurityConfigurationSource configurationSource;
    private final SecurityConfigurationCleaner configCleaner;
    private final PasswordService passwordService;
    private final EventManager eventManager;
    private final List<SecurityContributor> securityContributors = new ArrayList<SecurityContributor>();
    private volatile SecurityConfiguration defaultConfiguration;
    private volatile SecurityConfiguration mergedConfiguration;
    private final AtomicInteger mergedConfigurationDirty = new AtomicInteger(1);
    private boolean firstTimeConfiguration = true;

    @Inject
    public SecurityConfigurationManagerImpl(SecurityConfigurationSource configurationSource, SecurityConfigurationCleaner configCleaner, PasswordService passwordService, EventManager eventManager) {
        this.configurationSource = configurationSource;
        this.eventManager = eventManager;
        this.configCleaner = configCleaner;
        this.passwordService = passwordService;
    }

    @Override
    public List<CPrivilege> listPrivileges() {
        ArrayList privileges = Lists.newArrayList();
        privileges.addAll(this.getDefaultConfiguration().getPrivileges());
        privileges.addAll(this.getMergedConfiguration().getPrivileges());
        return Collections.unmodifiableList(privileges);
    }

    @Override
    public List<CRole> listRoles() {
        ArrayList roles = Lists.newArrayList();
        roles.addAll(this.getDefaultConfiguration().getRoles());
        roles.addAll(this.getMergedConfiguration().getRoles());
        return Collections.unmodifiableList(roles);
    }

    @Override
    public List<CUser> listUsers() {
        return Collections.unmodifiableList(this.getDefaultConfiguration().getUsers());
    }

    @Override
    public List<CUserRoleMapping> listUserRoleMappings() {
        return Collections.unmodifiableList(this.getDefaultConfiguration().getUserRoleMappings());
    }

    @Override
    public void createPrivilege(CPrivilege privilege) {
        if (this.getMergedConfiguration().getPrivileges().stream().anyMatch(p -> p.getId().equals(privilege.getId()))) {
            throw new DuplicatePrivilegeException(privilege.getId());
        }
        this.getDefaultConfiguration().addPrivilege(privilege);
    }

    @Override
    public void createRole(CRole role) {
        if (this.getMergedConfiguration().getRoles().stream().anyMatch(p -> p.getId().equals(role.getId()))) {
            throw new DuplicateRoleException(role.getId());
        }
        this.validateContainedRolesAndPrivileges(role);
        this.getDefaultConfiguration().addRole(role);
    }

    @Override
    public void createUser(CUser user, Set<String> roles) {
        this.createUser(user, null, roles);
    }

    @Override
    public void createUser(CUser user, String password, Set<String> roles) {
        if (!Strings2.isBlank((String)password)) {
            user.setPassword(this.passwordService.encryptPassword((Object)password));
        }
        this.getDefaultConfiguration().addUser(user, roles);
    }

    @Override
    public void deletePrivilege(String id) {
        try {
            this.getDefaultConfiguration().removePrivilege(id);
        }
        catch (NoSuchPrivilegeException e) {
            if (this.getMergedConfiguration().getPrivileges().stream().anyMatch(p -> p.getId().equals(id))) {
                throw new ReadonlyPrivilegeException(id);
            }
            throw new NoSuchPrivilegeException(id);
        }
        this.cleanRemovedPrivilege(id);
    }

    @Override
    public void deletePrivilegeByName(String name) {
        CPrivilege existing = this.readPrivilegeByName(name);
        try {
            this.getDefaultConfiguration().removePrivilegeByName(name);
        }
        catch (NoSuchPrivilegeException e) {
            boolean isReadOnly = this.getMergedConfiguration().getPrivileges().stream().anyMatch(p -> p.getName().equals(name));
            if (isReadOnly) {
                throw new ReadonlyPrivilegeException(name);
            }
            throw new NoSuchPrivilegeException(name);
        }
        this.cleanRemovedPrivilege(existing.getId());
    }

    @Override
    public void deleteRole(String id) {
        try {
            this.getDefaultConfiguration().removeRole(id);
        }
        catch (NoSuchRoleException e) {
            if (this.getMergedConfiguration().getRoles().stream().anyMatch(p -> p.getId().equals(id))) {
                throw new ReadonlyRoleException(id);
            }
            throw e;
        }
        this.configCleaner.roleRemoved(this.getDefaultConfiguration(), id);
    }

    @Override
    public void deleteUser(String id) throws UserNotFoundException {
        boolean found = this.getDefaultConfiguration().removeUser(id);
        if (!found) {
            throw new UserNotFoundException(id);
        }
    }

    @Override
    public CPrivilege newPrivilege() {
        return this.getDefaultConfiguration().newPrivilege();
    }

    @Override
    public CRole newRole() {
        return this.getDefaultConfiguration().newRole();
    }

    @Override
    public CUser newUser() {
        return this.getDefaultConfiguration().newUser();
    }

    @Override
    public CUserRoleMapping newUserRoleMapping() {
        return this.getDefaultConfiguration().newUserRoleMapping();
    }

    @Override
    public CPrivilege readPrivilege(String id) {
        CPrivilege privilege = this.getMergedConfiguration().getPrivilege(id);
        if (privilege != null) {
            return privilege;
        }
        privilege = this.validateExistingPrivilege(id);
        if (privilege != null) {
            return privilege;
        }
        throw new NoSuchPrivilegeException(id);
    }

    @Override
    public CPrivilege readPrivilegeByName(String name) {
        return Optional.of(name).map(n -> this.getMergedConfiguration().getPrivilegeByName((String)n)).orElseGet(() -> Optional.ofNullable(this.getDefaultConfiguration().getPrivilegeByName(name)).orElseThrow(() -> new NoSuchPrivilegeException(name)));
    }

    @Override
    public List<CPrivilege> readPrivileges(Set<String> ids) {
        List<CPrivilege> inMemoryPrivileges = this.getMergedConfiguration().getPrivileges(ids);
        Set foundPrivilegeIds = inMemoryPrivileges.stream().map(CPrivilege::getId).collect(Collectors.toSet());
        Sets.SetView notFoundIds = Sets.difference(ids, foundPrivilegeIds);
        if (notFoundIds.isEmpty()) {
            return inMemoryPrivileges;
        }
        List<CPrivilege> privileges = this.getDefaultConfiguration().getPrivileges((Set<String>)notFoundIds);
        foundPrivilegeIds = privileges.stream().map(CPrivilege::getId).collect(Collectors.toSet());
        if (!(notFoundIds = Sets.difference((Set)notFoundIds, foundPrivilegeIds)).isEmpty()) {
            this.log.debug("Unable to find privileges for ids={}", (Object)notFoundIds);
        }
        return Stream.concat(privileges.stream(), inMemoryPrivileges.stream()).collect(Collectors.toList());
    }

    @Override
    public CRole readRole(String id) {
        CRole role = this.getMergedConfiguration().getRole(id);
        if (role != null) {
            return role;
        }
        role = this.getDefaultConfiguration().getRole(id);
        if (role != null) {
            return role;
        }
        throw new NoSuchRoleException(id);
    }

    @Override
    public CUser readUser(String id) throws UserNotFoundException {
        CUser user = this.getDefaultConfiguration().getUser(id);
        if (user != null) {
            return user;
        }
        throw new UserNotFoundException(id);
    }

    @Override
    public void updatePrivilege(CPrivilege privilege) {
        CPrivilege existing = this.getDefaultConfiguration().getPrivilege(privilege.getId());
        if (existing == null) {
            if (this.getMergedConfiguration().getPrivileges().stream().anyMatch(p -> p.getId().equals(privilege.getId()))) {
                throw new ReadonlyPrivilegeException(privilege.getId());
            }
            throw new NoSuchPrivilegeException(privilege.getId());
        }
        this.getDefaultConfiguration().updatePrivilege(privilege);
    }

    @Override
    public void updatePrivilegeByName(CPrivilege privilege) {
        boolean onDefaultConfig = Optional.ofNullable(this.getDefaultConfiguration().getPrivilegeByName(privilege.getName())).isPresent();
        if (!onDefaultConfig) {
            boolean isReadOnly = this.getMergedConfiguration().getPrivileges().stream().anyMatch(p -> p.getName().equals(privilege.getName()));
            if (isReadOnly) {
                throw new ReadonlyPrivilegeException(privilege.getName());
            }
            throw new NoSuchPrivilegeException(privilege.getName());
        }
        this.getDefaultConfiguration().updatePrivilegeByName(privilege);
    }

    @Override
    public void updateRole(CRole role) {
        CRole existing = this.getDefaultConfiguration().getRole(role.getId());
        if (existing == null) {
            if (this.getMergedConfiguration().getRoles().stream().anyMatch(p -> p.getId().equals(role.getId()))) {
                throw new ReadonlyRoleException(role.getId());
            }
            throw new NoSuchRoleException(role.getId());
        }
        this.validateContainedRolesAndPrivileges(role);
        this.validateRoleDoesntContainItself(role);
        this.getDefaultConfiguration().updateRole(role);
    }

    @Override
    public void updateUser(CUser user) throws UserNotFoundException {
        this.getDefaultConfiguration().updateUser(user);
    }

    @Override
    public void updateUser(CUser user, Set<String> roles) throws UserNotFoundException {
        this.getDefaultConfiguration().updateUser(user, roles);
    }

    @Override
    public void createUserRoleMapping(CUserRoleMapping userRoleMapping) {
        this.getDefaultConfiguration().addUserRoleMapping(userRoleMapping);
    }

    @Override
    public CUserRoleMapping readUserRoleMapping(String userId, String source) throws NoSuchRoleMappingException {
        CUserRoleMapping mapping = this.getDefaultConfiguration().getUserRoleMapping(userId, source);
        if (mapping != null) {
            return mapping;
        }
        throw new NoSuchRoleMappingException(userId);
    }

    @Override
    public void updateUserRoleMapping(CUserRoleMapping userRoleMapping) throws NoSuchRoleMappingException {
        this.getDefaultConfiguration().updateUserRoleMapping(userRoleMapping);
    }

    @Override
    public void deleteUserRoleMapping(String userId, String source) throws NoSuchRoleMappingException {
        boolean found = this.getDefaultConfiguration().removeUserRoleMapping(userId, source);
        if (!found) {
            throw new NoSuchRoleMappingException(userId);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addContributor(SecurityContributor contributor) {
        SecurityConfigurationManagerImpl securityConfigurationManagerImpl = this;
        synchronized (securityConfigurationManagerImpl) {
            this.securityContributors.add(contributor);
        }
        this.eventManager.post((Object)new SecurityContributionChangedEvent());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void removeContributor(SecurityContributor contributor) {
        SecurityConfigurationManagerImpl securityConfigurationManagerImpl = this;
        synchronized (securityConfigurationManagerImpl) {
            this.securityContributors.remove(contributor);
        }
        this.eventManager.post((Object)new SecurityContributionChangedEvent());
    }

    @Override
    public void cleanRemovedPrivilege(String privilegeId) {
        this.configCleaner.privilegeRemoved(this.getDefaultConfiguration(), privilegeId);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private SecurityConfiguration getDefaultConfiguration() {
        SecurityConfiguration configuration = this.defaultConfiguration;
        if (configuration == null) {
            SecurityConfigurationManagerImpl securityConfigurationManagerImpl = this;
            synchronized (securityConfigurationManagerImpl) {
                configuration = this.defaultConfiguration;
                if (configuration == null) {
                    this.defaultConfiguration = configuration = this.doGetDefaultConfiguration();
                }
            }
        }
        return configuration;
    }

    private SecurityConfiguration doGetDefaultConfiguration() {
        return this.configurationSource.loadConfiguration();
    }

    @AllowConcurrentEvents
    @Subscribe
    public void on(SecurityContributionChangedEvent event) {
        this.mergedConfigurationDirty.incrementAndGet();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private SecurityConfiguration getMergedConfiguration() {
        if (this.mergedConfigurationDirty.get() != 0) {
            boolean rebuiltConfiguration = false;
            SecurityConfigurationManagerImpl securityConfigurationManagerImpl = this;
            synchronized (securityConfigurationManagerImpl) {
                int dirty = this.mergedConfigurationDirty.get();
                if (dirty != 0) {
                    this.mergedConfiguration = this.doGetMergedConfiguration();
                    this.mergedConfigurationDirty.compareAndSet(dirty, 0);
                    rebuiltConfiguration = !this.firstTimeConfiguration;
                    this.firstTimeConfiguration = false;
                }
            }
            if (rebuiltConfiguration) {
                this.eventManager.post((Object)new AuthorizationConfigurationChanged());
            }
        }
        return this.mergedConfiguration;
    }

    private MemorySecurityConfiguration doGetMergedConfiguration() {
        MemorySecurityConfiguration configuration = new MemorySecurityConfiguration();
        for (SecurityContributor contributor : this.securityContributors) {
            SecurityConfiguration contribution = contributor.getContribution();
            if (contribution == null) continue;
            Preconditions.checkState((contribution.getUsers() == null || contribution.getUsers().isEmpty() ? 1 : 0) != 0, (Object)"Security contributions cannot have users");
            Preconditions.checkState((contribution.getUserRoleMappings() == null || contribution.getUserRoleMappings().isEmpty() ? 1 : 0) != 0, (Object)"Security contributions cannot have user/role mappings");
            this.appendConfig(configuration, contribution);
        }
        return configuration;
    }

    private SecurityConfiguration appendConfig(SecurityConfiguration to, SecurityConfiguration from) {
        for (CPrivilege privilege : from.getPrivileges()) {
            privilege.setReadOnly(true);
            to.addPrivilege(privilege);
        }
        HashMap<String, CRole> roles = new HashMap<String, CRole>();
        for (CRole role : to.getRoles()) {
            roles.put(role.getId(), role);
        }
        for (CRole role : from.getRoles()) {
            CRole eachRole = (CRole)roles.get(role.getId());
            if (eachRole != null) {
                role = this.mergeRolesContents(role, eachRole);
                to.removeRole(role.getId());
            }
            role.setReadOnly(true);
            to.addRole(role);
            roles.put(role.getId(), role);
        }
        return to;
    }

    private CRole mergeRolesContents(CRole roleA, CRole roleB) {
        HashSet<String> roles = new HashSet<String>();
        if (roleA.getRoles() != null) {
            roles.addAll(roleA.getRoles());
        }
        if (roleB.getRoles() != null) {
            roles.addAll(roleB.getRoles());
        }
        HashSet<String> privs = new HashSet<String>();
        if (roleA.getPrivileges() != null) {
            privs.addAll(roleA.getPrivileges());
        }
        if (roleB.getPrivileges() != null) {
            privs.addAll(roleB.getPrivileges());
        }
        CRole newRole = this.newRole();
        newRole.setId(roleA.getId());
        newRole.setRoles(Sets.newHashSet(roles));
        newRole.setPrivileges(Sets.newHashSet(privs));
        if (!Strings2.isBlank((String)roleA.getName())) {
            newRole.setName(roleA.getName());
        } else {
            newRole.setName(roleB.getName());
        }
        if (!Strings2.isBlank((String)roleA.getDescription())) {
            newRole.setDescription(roleA.getDescription());
        } else {
            newRole.setDescription(roleB.getDescription());
        }
        return newRole;
    }

    private void validateContainedRolesAndPrivileges(CRole role) {
        role.getRoles().forEach(this::readRole);
        role.getPrivileges().forEach(this::readPrivilege);
    }

    private void validateRoleDoesntContainItself(CRole role) {
        this.validateRoleDoesntContainItself(role, role, new HashSet<String>());
    }

    private void validateRoleDoesntContainItself(CRole role, CRole child, Set<String> checkedRoles) {
        child.getRoles().forEach(r -> {
            if (r.equals(role.getId())) {
                throw new RoleContainsItselfException(role.getId());
            }
            if (!checkedRoles.contains(r)) {
                checkedRoles.add((String)r);
                this.validateRoleDoesntContainItself(role, this.readRole((String)r), checkedRoles);
            }
        });
    }

    private CPrivilege validateExistingPrivilege(String identifier) {
        CPrivilege privilege = this.getDefaultConfiguration().getPrivilege(identifier);
        if (privilege == null) {
            privilege = this.getDefaultConfiguration().getPrivilegeByName(identifier);
            return privilege;
        }
        return privilege;
    }
}

