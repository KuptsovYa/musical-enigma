/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.Maps
 *  javax.annotation.Nullable
 *  org.apache.shiro.util.CollectionUtils
 */
package org.sonatype.nexus.security.config;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nullable;
import org.apache.shiro.util.CollectionUtils;
import org.sonatype.nexus.security.config.CPrivilege;
import org.sonatype.nexus.security.config.CRole;
import org.sonatype.nexus.security.config.CUser;
import org.sonatype.nexus.security.config.CUserRoleMapping;
import org.sonatype.nexus.security.config.SecurityConfiguration;
import org.sonatype.nexus.security.config.SecuritySourceUtil;
import org.sonatype.nexus.security.config.memory.MemoryCPrivilege;
import org.sonatype.nexus.security.config.memory.MemoryCRole;
import org.sonatype.nexus.security.config.memory.MemoryCUser;
import org.sonatype.nexus.security.config.memory.MemoryCUserRoleMapping;
import org.sonatype.nexus.security.privilege.NoSuchPrivilegeException;
import org.sonatype.nexus.security.role.NoSuchRoleException;
import org.sonatype.nexus.security.user.NoSuchRoleMappingException;
import org.sonatype.nexus.security.user.UserNotFoundException;

public class MemorySecurityConfiguration
implements SecurityConfiguration,
Serializable,
Cloneable {
    private final ConcurrentMap<String, CUser> users = Maps.newConcurrentMap();
    private final ConcurrentMap<String, CRole> roles = Maps.newConcurrentMap();
    private final ConcurrentMap<String, CPrivilege> privileges = Maps.newConcurrentMap();
    private final ConcurrentMap<String, CUserRoleMapping> userRoleMappings = Maps.newConcurrentMap();

    @Override
    public List<CUser> getUsers() {
        return ImmutableList.copyOf(this.users.values());
    }

    @Override
    public CUser getUser(String id) {
        Preconditions.checkNotNull((Object)id);
        return (CUser)this.users.get(id);
    }

    private void addUser(CUser user) {
        Preconditions.checkNotNull((Object)user);
        Preconditions.checkNotNull((Object)user.getId());
        Preconditions.checkState((this.users.putIfAbsent(user.getId(), user) == null ? 1 : 0) != 0, (String)"%s already exists", (Object)user.getId());
    }

    @Override
    public void addUser(CUser user, Set<String> roles) {
        this.addUser(user);
        MemoryCUserRoleMapping mapping = new MemoryCUserRoleMapping();
        mapping.setUserId(user.getId());
        mapping.setSource("default");
        mapping.setRoles(roles);
        this.addUserRoleMapping(mapping);
    }

    @Override
    public void addRoleMapping(String userId, Set<String> roles, String source) {
    }

    @Override
    public CUser newUser() {
        return new MemoryCUser();
    }

    public void setUsers(Collection<CUser> users) {
        this.users.clear();
        if (users != null) {
            for (CUser user : users) {
                this.addUser(user);
            }
        }
    }

    public MemorySecurityConfiguration withUsers(CUser ... users) {
        this.setUsers(Arrays.asList(users));
        return this;
    }

    @Override
    public void updateUser(CUser user) throws UserNotFoundException {
        Preconditions.checkNotNull((Object)user);
        Preconditions.checkNotNull((Object)user.getId());
        if (this.users.replace(user.getId(), user) == null) {
            throw new UserNotFoundException(user.getId());
        }
    }

    @Override
    public void updateUser(CUser user, Set<String> roles) throws UserNotFoundException {
        this.updateUser(user);
        MemoryCUserRoleMapping mapping = new MemoryCUserRoleMapping();
        mapping.setUserId(user.getId());
        mapping.setSource("default");
        mapping.setRoles(roles);
        try {
            this.updateUserRoleMapping(mapping);
        }
        catch (NoSuchRoleMappingException e) {
            this.addUserRoleMapping(mapping);
        }
    }

    @Override
    public boolean removeUser(String id) {
        Preconditions.checkNotNull((Object)id);
        if (this.users.remove(id) != null) {
            this.removeUserRoleMapping(id, "default");
            return true;
        }
        return false;
    }

    @Override
    public List<CUserRoleMapping> getUserRoleMappings() {
        return ImmutableList.copyOf(this.userRoleMappings.values());
    }

    @Override
    public CUserRoleMapping getUserRoleMapping(String userId, String source) {
        Preconditions.checkNotNull((Object)userId);
        Preconditions.checkNotNull((Object)source);
        return (CUserRoleMapping)this.userRoleMappings.get(this.userRoleMappingKey(userId, source));
    }

    @Override
    public void addUserRoleMapping(CUserRoleMapping mapping) {
        Preconditions.checkNotNull((Object)mapping);
        Preconditions.checkNotNull((Object)mapping.getUserId());
        Preconditions.checkNotNull((Object)mapping.getSource());
        Preconditions.checkState((this.userRoleMappings.putIfAbsent(this.userRoleMappingKey(mapping.getUserId(), mapping.getSource()), mapping) == null ? 1 : 0) != 0, (String)"%s/%s already exists", (Object)mapping.getUserId(), (Object)mapping.getSource());
    }

    public void setUserRoleMappings(Collection<CUserRoleMapping> mappings) {
        this.userRoleMappings.clear();
        if (mappings != null) {
            for (CUserRoleMapping mapping : mappings) {
                this.addUserRoleMapping(mapping);
            }
        }
    }

    public MemorySecurityConfiguration withUserRoleMappings(CUserRoleMapping ... mappings) {
        this.setUserRoleMappings(Arrays.asList(mappings));
        return this;
    }

    @Override
    public void updateUserRoleMapping(CUserRoleMapping mapping) throws NoSuchRoleMappingException {
        Preconditions.checkNotNull((Object)mapping);
        Preconditions.checkNotNull((Object)mapping.getUserId());
        Preconditions.checkNotNull((Object)mapping.getSource());
        if (this.userRoleMappings.replace(this.userRoleMappingKey(mapping.getUserId(), mapping.getSource()), mapping) == null) {
            throw new NoSuchRoleMappingException(mapping.getUserId());
        }
    }

    @Override
    public boolean removeUserRoleMapping(String userId, String source) {
        Preconditions.checkNotNull((Object)userId);
        Preconditions.checkNotNull((Object)source);
        return this.userRoleMappings.remove(this.userRoleMappingKey(userId, source)) != null;
    }

    @Override
    public List<CPrivilege> getPrivileges() {
        return ImmutableList.copyOf(this.privileges.values());
    }

    @Override
    public CPrivilege getPrivilege(String id) {
        Preconditions.checkNotNull((Object)id);
        return (CPrivilege)this.privileges.get(id);
    }

    @Override
    @Nullable
    public CPrivilege getPrivilegeByName(String name) {
        return Optional.ofNullable(name).flatMap(n -> this.privileges.values().stream().filter(p -> p.getName().equals(n)).findFirst()).orElse(null);
    }

    @Override
    public List<CPrivilege> getPrivileges(Set<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return ids.stream().map(this.privileges::get).filter(Objects::nonNull).toList();
    }

    @Override
    public CPrivilege addPrivilege(CPrivilege privilege) {
        Preconditions.checkNotNull((Object)privilege);
        Preconditions.checkNotNull((Object)privilege.getId());
        Preconditions.checkState((this.privileges.putIfAbsent(privilege.getId(), privilege) == null ? 1 : 0) != 0, (String)"%s already exists", (Object)privilege.getId());
        return privilege;
    }

    public void setPrivileges(Collection<CPrivilege> privileges) {
        this.privileges.clear();
        if (privileges != null) {
            for (CPrivilege privilege : privileges) {
                this.addPrivilege(privilege);
            }
        }
    }

    public MemorySecurityConfiguration withPrivileges(CPrivilege ... privileges) {
        this.setPrivileges(new ArrayList<CPrivilege>(Arrays.asList(privileges)));
        return this;
    }

    @Override
    public void updatePrivilege(CPrivilege privilege) {
        Preconditions.checkNotNull((Object)privilege);
        Preconditions.checkNotNull((Object)privilege.getId());
        if (this.privileges.replace(privilege.getId(), privilege) == null) {
            throw new NoSuchPrivilegeException(privilege.getId());
        }
    }

    @Override
    public void updatePrivilegeByName(CPrivilege privilege) {
        this.updatePrivilege(privilege);
    }

    @Override
    public boolean removePrivilege(String id) {
        Preconditions.checkNotNull((Object)id);
        return this.privileges.remove(id) != null;
    }

    @Override
    public boolean removePrivilegeByName(String name) {
        return Optional.ofNullable(name).map(this::getPrivilegeByName).map(CPrivilege::getId).map(this::removePrivilege).orElse(false);
    }

    @Override
    public List<CRole> getRoles() {
        return ImmutableList.copyOf(this.roles.values());
    }

    @Override
    public CRole getRole(String id) {
        Preconditions.checkNotNull((Object)id);
        return (CRole)this.roles.get(id);
    }

    @Override
    public void addRole(CRole role) {
        Preconditions.checkNotNull((Object)role);
        Preconditions.checkNotNull((Object)role.getId());
        Preconditions.checkState((this.roles.putIfAbsent(role.getId(), role) == null ? 1 : 0) != 0, (String)"%s already exists", (Object)role.getId());
    }

    public void setRoles(Collection<CRole> roles) {
        this.roles.clear();
        if (roles != null) {
            for (CRole role : roles) {
                this.addRole(role);
            }
        }
    }

    public MemorySecurityConfiguration withRoles(CRole ... roles) {
        this.setRoles(Arrays.asList(roles));
        return this;
    }

    @Override
    public void updateRole(CRole role) {
        Preconditions.checkNotNull((Object)role);
        Preconditions.checkNotNull((Object)role.getId());
        if (this.roles.replace(role.getId(), role) == null) {
            throw new NoSuchRoleException(role.getId());
        }
    }

    @Override
    public boolean removeRole(String id) {
        Preconditions.checkNotNull((Object)id);
        return this.roles.remove(id) != null;
    }

    public MemorySecurityConfiguration clone() throws CloneNotSupportedException {
        MemorySecurityConfiguration copy = (MemorySecurityConfiguration)super.clone();
        copy.users.putAll(this.users);
        copy.roles.putAll(this.roles);
        copy.privileges.putAll(this.privileges);
        copy.userRoleMappings.putAll(this.userRoleMappings);
        return copy;
    }

    private String userRoleMappingKey(String userId, String source) {
        return (SecuritySourceUtil.isCaseInsensitiveSource(source) ? userId.toLowerCase() : userId) + "|" + source;
    }

    @Override
    public CUserRoleMapping newUserRoleMapping() {
        return new MemoryCUserRoleMapping();
    }

    @Override
    public CRole newRole() {
        return new MemoryCRole();
    }

    @Override
    public CPrivilege newPrivilege() {
        return new MemoryCPrivilege();
    }
}

