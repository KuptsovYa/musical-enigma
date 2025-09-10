/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.cache.Cache
 *  com.google.common.cache.CacheBuilder
 *  com.google.common.eventbus.AllowConcurrentEvents
 *  com.google.common.eventbus.Subscribe
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.apache.shiro.authz.Permission
 *  org.apache.shiro.authz.permission.RolePermissionResolver
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.event.EventHelper
 *  org.sonatype.nexus.common.event.EventManager
 *  org.sonatype.nexus.distributed.event.service.api.common.AuthorizationChangedDistributedEvent
 */
package org.sonatype.nexus.security.internal;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.event.EventHelper;
import org.sonatype.nexus.common.event.EventManager;
import org.sonatype.nexus.distributed.event.service.api.common.AuthorizationChangedDistributedEvent;
import org.sonatype.nexus.security.authz.AuthorizationConfigurationChanged;
import org.sonatype.nexus.security.config.CPrivilege;
import org.sonatype.nexus.security.config.CRole;
import org.sonatype.nexus.security.config.SecurityConfigurationManager;
import org.sonatype.nexus.security.internal.SecurityContributionChangedEvent;
import org.sonatype.nexus.security.privilege.NoSuchPrivilegeException;
import org.sonatype.nexus.security.privilege.PrivilegeDescriptor;
import org.sonatype.nexus.security.role.NoSuchRoleException;

@Named(value="default")
@Singleton
public class RolePermissionResolverImpl
extends ComponentSupport
implements RolePermissionResolver {
    private final SecurityConfigurationManager configuration;
    private final List<PrivilegeDescriptor> privilegeDescriptors;
    private final Cache<String, Permission> permissionsCache = CacheBuilder.newBuilder().softValues().build();
    private final Cache<String, Collection<Permission>> rolePermissionsCache = CacheBuilder.newBuilder().softValues().build();
    private final Cache<String, String> roleNotFoundCache;

    @Inject
    public RolePermissionResolverImpl(SecurityConfigurationManager configuration, List<PrivilegeDescriptor> privilegeDescriptors, EventManager eventManager, @Named(value="${security.roleNotFoundCacheSize:-100000}") int roleNotFoundCacheSize) {
        this.configuration = (SecurityConfigurationManager)Preconditions.checkNotNull((Object)configuration);
        this.privilegeDescriptors = (List)Preconditions.checkNotNull(privilegeDescriptors);
        this.roleNotFoundCache = CacheBuilder.newBuilder().maximumSize((long)roleNotFoundCacheSize).build();
        eventManager.register((Object)this);
    }

    private void invalidate() {
        this.permissionsCache.invalidateAll();
        this.rolePermissionsCache.invalidateAll();
        this.roleNotFoundCache.invalidateAll();
        this.log.trace("Cache invalidated");
    }

    @AllowConcurrentEvents
    @Subscribe
    public void on(AuthorizationConfigurationChanged event) {
        this.invalidate();
    }

    @AllowConcurrentEvents
    @Subscribe
    public void on(SecurityContributionChangedEvent event) {
        this.invalidate();
    }

    @AllowConcurrentEvents
    @Subscribe
    public void on(AuthorizationChangedDistributedEvent event) {
        if (EventHelper.isReplicating()) {
            this.invalidate();
        }
    }

    public Collection<Permission> resolvePermissionsInRole(String roleString) {
        Preconditions.checkNotNull((Object)roleString);
        Collection cachedPermissions = (Collection)this.rolePermissionsCache.getIfPresent((Object)roleString);
        if (cachedPermissions != null) {
            return cachedPermissions;
        }
        LinkedHashSet<Permission> permissions = new LinkedHashSet<Permission>();
        ArrayDeque<String> rolesToProcess = new ArrayDeque<String>();
        HashSet<String> processedRoleIds = new HashSet<String>();
        rolesToProcess.add(roleString);
        while (!rolesToProcess.isEmpty()) {
            String roleId = (String)rolesToProcess.removeFirst();
            if (!processedRoleIds.add(roleId)) continue;
            if (this.roleNotFoundCache.getIfPresent((Object)roleId) != null) {
                this.log.trace("Role {} found in NFC, role check skipped", (Object)roleId);
                continue;
            }
            try {
                cachedPermissions = (Collection)this.rolePermissionsCache.getIfPresent((Object)roleId);
                if (cachedPermissions != null) {
                    permissions.addAll(cachedPermissions);
                    continue;
                }
                CRole role = this.configuration.readRole(roleId);
                rolesToProcess.addAll(role.getRoles());
                for (String privilegeId : role.getPrivileges()) {
                    Permission permission = this.permission(privilegeId);
                    if (permission == null) continue;
                    permissions.add(permission);
                }
            }
            catch (NoSuchRoleException e) {
                this.log.trace("Ignoring missing role: {}", (Object)roleId, (Object)e);
                this.roleNotFoundCache.put((Object)roleId, (Object)"");
            }
        }
        this.rolePermissionsCache.put((Object)roleString, permissions);
        return permissions;
    }

    @Nullable
    private PrivilegeDescriptor descriptor(String privilegeType) {
        Preconditions.checkNotNull((Object)privilegeType);
        for (PrivilegeDescriptor descriptor : this.privilegeDescriptors) {
            if (!privilegeType.equals(descriptor.getType())) continue;
            return descriptor;
        }
        this.log.warn("Missing privilege-descriptor for type: {}", (Object)privilegeType);
        return null;
    }

    @Nullable
    private Permission permission(String privilegeId) {
        Preconditions.checkNotNull((Object)privilegeId);
        Permission permission = (Permission)this.permissionsCache.getIfPresent((Object)privilegeId);
        if (permission == null) {
            try {
                CPrivilege privilege = this.configuration.readPrivilege(privilegeId);
                PrivilegeDescriptor descriptor = this.descriptor(privilege.getType());
                if (descriptor != null) {
                    permission = descriptor.createPermission(privilege);
                    this.permissionsCache.put((Object)privilegeId, (Object)permission);
                }
            }
            catch (NoSuchPrivilegeException e) {
                this.log.trace("Ignoring missing privilege: {}", (Object)privilegeId, (Object)e);
            }
        }
        return permission;
    }
}

