/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  com.google.common.eventbus.Subscribe
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.event.EventAware
 *  org.sonatype.nexus.common.event.EventHelper
 *  org.sonatype.nexus.common.event.EventManager
 *  org.sonatype.nexus.distributed.event.service.api.EventType
 *  org.sonatype.nexus.distributed.event.service.api.common.AuthorizationChangedDistributedEvent
 *  org.sonatype.nexus.distributed.event.service.api.common.PrivilegeConfigurationEvent
 *  org.sonatype.nexus.distributed.event.service.api.common.RoleConfigurationEvent
 */
package org.sonatype.nexus.security.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.event.EventAware;
import org.sonatype.nexus.common.event.EventHelper;
import org.sonatype.nexus.common.event.EventManager;
import org.sonatype.nexus.distributed.event.service.api.EventType;
import org.sonatype.nexus.distributed.event.service.api.common.AuthorizationChangedDistributedEvent;
import org.sonatype.nexus.distributed.event.service.api.common.PrivilegeConfigurationEvent;
import org.sonatype.nexus.distributed.event.service.api.common.RoleConfigurationEvent;
import org.sonatype.nexus.security.authz.AuthorizationConfigurationChanged;
import org.sonatype.nexus.security.authz.AuthorizationManager;
import org.sonatype.nexus.security.config.CPrivilege;
import org.sonatype.nexus.security.config.CRole;
import org.sonatype.nexus.security.config.SecurityConfigurationManager;
import org.sonatype.nexus.security.privilege.NoSuchPrivilegeException;
import org.sonatype.nexus.security.privilege.Privilege;
import org.sonatype.nexus.security.privilege.PrivilegeCreatedEvent;
import org.sonatype.nexus.security.privilege.PrivilegeDeletedEvent;
import org.sonatype.nexus.security.privilege.PrivilegeDescriptor;
import org.sonatype.nexus.security.privilege.PrivilegeUpdatedEvent;
import org.sonatype.nexus.security.role.NoSuchRoleException;
import org.sonatype.nexus.security.role.Role;
import org.sonatype.nexus.security.role.RoleCreatedEvent;
import org.sonatype.nexus.security.role.RoleDeletedEvent;
import org.sonatype.nexus.security.role.RoleUpdatedEvent;

@Named(value="default")
@Singleton
public class AuthorizationManagerImpl
extends ComponentSupport
implements AuthorizationManager,
EventAware {
    public static final String SOURCE = "default";
    private final SecurityConfigurationManager configuration;
    private final EventManager eventManager;
    private final List<PrivilegeDescriptor> privilegeDescriptors;

    @Inject
    public AuthorizationManagerImpl(SecurityConfigurationManager configuration, EventManager eventManager, List<PrivilegeDescriptor> privilegeDescriptors) {
        this.configuration = configuration;
        this.eventManager = eventManager;
        this.privilegeDescriptors = (List)Preconditions.checkNotNull(privilegeDescriptors);
    }

    @Override
    public String getSource() {
        return SOURCE;
    }

    @Override
    public String getRealmName() {
        return "NexusAuthenticatingRealm";
    }

    private Role convert(CRole source) {
        Role target = new Role();
        target.setRoleId(source.getId());
        target.setVersion(source.getVersion());
        target.setName(source.getName());
        target.setSource(SOURCE);
        target.setDescription(source.getDescription());
        target.setReadOnly(source.isReadOnly());
        target.setPrivileges(Sets.newHashSet(source.getPrivileges()));
        target.setRoles(Sets.newHashSet(source.getRoles()));
        return target;
    }

    private CRole convert(Role source) {
        CRole target = this.configuration.newRole();
        target.setId(source.getRoleId());
        target.setVersion(source.getVersion());
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setReadOnly(source.isReadOnly());
        if (source.getPrivileges() != null) {
            target.setPrivileges(Sets.newHashSet(source.getPrivileges()));
        } else {
            target.setPrivileges(Sets.newHashSet());
        }
        if (source.getRoles() != null) {
            target.setRoles(Sets.newHashSet(source.getRoles()));
        } else {
            target.setRoles(Sets.newHashSet());
        }
        return target;
    }

    private CPrivilege convert(Privilege source) {
        CPrivilege target = this.configuration.newPrivilege();
        target.setId(source.getId());
        target.setVersion(source.getVersion());
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setReadOnly(source.isReadOnly());
        target.setType(source.getType());
        if (source.getProperties() != null) {
            target.setProperties(Maps.newHashMap(source.getProperties()));
        }
        return target;
    }

    private Privilege convert(CPrivilege source) {
        Privilege target = new Privilege();
        target.setId(source.getId());
        target.setVersion(source.getVersion());
        target.setName(source.getName() == null ? source.getId() : source.getName());
        target.setDescription(source.getDescription());
        target.setReadOnly(source.isReadOnly());
        target.setType(source.getType());
        target.setProperties(Maps.newHashMap(source.getProperties()));
        PrivilegeDescriptor descriptor = this.descriptor(source.getType());
        if (descriptor != null) {
            target.setPermission(descriptor.createPermission(source));
        }
        return target;
    }

    @Nullable
    private PrivilegeDescriptor descriptor(String type) {
        for (PrivilegeDescriptor descriptor : this.privilegeDescriptors) {
            if (!type.equals(descriptor.getType())) continue;
            return descriptor;
        }
        return null;
    }

    @Override
    public Set<Role> listRoles() {
        HashSet<Role> roles = new HashSet<Role>();
        List<CRole> secRoles = this.configuration.listRoles();
        for (CRole CRole2 : secRoles) {
            roles.add(this.convert(CRole2));
        }
        return roles;
    }

    @Override
    public Role getRole(String roleId) throws NoSuchRoleException {
        return this.convert(this.configuration.readRole(roleId));
    }

    @Override
    public Set<Role> searchRoles(String query) {
        return this.listRoles();
    }

    @Override
    public Role addRole(Role role) {
        CRole secRole = this.convert(role);
        this.configuration.createRole(secRole);
        this.log.info("Added role {}", (Object)role.getName());
        this.fireRoleCreatedEvent(role);
        this.fireRoleConfigurationDistributedEvent(role.getRoleId(), EventType.CREATED);
        this.fireAuthorizationChangedEvent();
        return this.convert(secRole);
    }

    @Override
    public Role updateRole(Role role) throws NoSuchRoleException {
        CRole secRole = this.convert(role);
        this.configuration.updateRole(secRole);
        this.fireRoleUpdatedEvent(role);
        this.fireRoleConfigurationDistributedEvent(role.getRoleId(), EventType.UPDATED);
        this.fireAuthorizationChangedEvent();
        return this.convert(secRole);
    }

    @Override
    public void deleteRole(String roleId) throws NoSuchRoleException {
        Role role = this.getRole(roleId);
        this.configuration.deleteRole(roleId);
        this.log.info("Removed role {}", (Object)role.getName());
        this.fireRoleDeletedEvent(role);
        this.fireRoleConfigurationDistributedEvent(roleId, EventType.DELETED);
        this.fireAuthorizationChangedEvent();
    }

    @Override
    public Set<Privilege> listPrivileges() {
        HashSet<Privilege> privileges = new HashSet<Privilege>();
        List<CPrivilege> secPrivs = this.configuration.listPrivileges();
        for (CPrivilege CPrivilege2 : secPrivs) {
            privileges.add(this.convert(CPrivilege2));
        }
        return privileges;
    }

    @Override
    public Privilege getPrivilege(String privilegeId) throws NoSuchPrivilegeException {
        return this.convert(this.configuration.readPrivilege(privilegeId));
    }

    @Override
    public Privilege getPrivilegeByName(String privilegeName) throws NoSuchPrivilegeException {
        return this.convert(this.configuration.readPrivilegeByName(privilegeName));
    }

    @Override
    public List<Privilege> getPrivileges(Set<String> privilegeIds) {
        List<CPrivilege> privileges = this.configuration.readPrivileges(privilegeIds);
        return privileges.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public Privilege addPrivilege(Privilege privilege) {
        CPrivilege secPriv = this.convert(privilege);
        this.configuration.createPrivilege(secPriv);
        this.log.info("Added privilege {}", (Object)privilege.getName());
        this.firePrivilegeCreatedEvent(privilege);
        this.firePrivilegeConfigurationDistributedEvent(privilege.getId(), EventType.CREATED);
        this.fireAuthorizationChangedEvent();
        return this.convert(secPriv);
    }

    @Override
    public Privilege updatePrivilege(Privilege privilege) throws NoSuchPrivilegeException {
        CPrivilege secPriv = this.convert(privilege);
        this.configuration.updatePrivilege(secPriv);
        this.firePrivilegeUpdatedEvent(privilege);
        this.firePrivilegeConfigurationDistributedEvent(privilege.getId(), EventType.UPDATED);
        this.fireAuthorizationChangedEvent();
        return this.convert(secPriv);
    }

    @Override
    public Privilege updatePrivilegeByName(Privilege privilege) throws NoSuchPrivilegeException {
        CPrivilege toUpdate = this.convert(privilege);
        this.configuration.updatePrivilegeByName(toUpdate);
        this.firePrivilegeUpdatedEvent(privilege);
        this.firePrivilegeConfigurationDistributedEvent(privilege.getId(), EventType.UPDATED);
        this.fireAuthorizationChangedEvent();
        return this.convert(toUpdate);
    }

    @Override
    public void deletePrivilege(String privilegeId) throws NoSuchPrivilegeException {
        Privilege privilege = this.getPrivilege(privilegeId);
        this.configuration.deletePrivilege(privilegeId);
        this.log.info("Removed privilege {}", (Object)privilege.getName());
        this.firePrivilegeDeletedEvent(privilege);
        this.firePrivilegeConfigurationDistributedEvent(privilegeId, EventType.DELETED);
        this.fireAuthorizationChangedEvent();
    }

    @Override
    public void deletePrivilegeByName(String privilegeName) throws NoSuchPrivilegeException {
        Privilege privilege = this.getPrivilegeByName(privilegeName);
        this.configuration.deletePrivilegeByName(privilegeName);
        this.log.info("Removed privilege by name {}", (Object)privilegeName);
        this.firePrivilegeDeletedEvent(privilege);
        this.firePrivilegeConfigurationDistributedEvent(privilegeName, EventType.DELETED);
        this.fireAuthorizationChangedEvent();
    }

    @Override
    public boolean supportsWrite() {
        return true;
    }

    @Subscribe
    public void onRoleConfigurationEvent(RoleConfigurationEvent event) {
        if (!EventHelper.isReplicating()) {
            return;
        }
        Preconditions.checkNotNull((Object)event);
        String roleId = event.getRoleId();
        EventType eventType = event.getEventType();
        this.log.debug("Consume distributed RoleConfigurationEvent: roleId={}, type={}", (Object)roleId, (Object)eventType);
        switch (eventType) {
            case CREATED: {
                this.handleRoleCreatedDistributedEvent(roleId);
                break;
            }
            case UPDATED: {
                this.handleRoleUpdatedDistributedEvent(roleId);
                break;
            }
            case DELETED: {
                this.handleRoleDeletedDistributedEvent(roleId);
            }
        }
    }

    @Subscribe
    public void onPrivilegeConfigurationEvent(PrivilegeConfigurationEvent event) {
        if (!EventHelper.isReplicating()) {
            return;
        }
        Preconditions.checkNotNull((Object)event);
        String privilegeId = event.getPrivilegeId();
        EventType eventType = event.getEventType();
        this.log.debug("Consume distributed PrivilegeConfigurationEvent: privilegeId={}, type={}", (Object)privilegeId, (Object)eventType);
        switch (eventType) {
            case CREATED: {
                this.handlePrivilegeCreatedDistributedEvent(privilegeId);
                break;
            }
            case UPDATED: {
                this.handlePrivilegeUpdatedDistributedEvent(privilegeId);
                break;
            }
            case DELETED: {
                this.handlePrivilegeDeletedDistributedEvent(privilegeId);
            }
        }
    }

    private void handleRoleCreatedDistributedEvent(String roleId) {
        try {
            Role role = this.getRole(roleId);
            this.fireRoleCreatedEvent(role);
        }
        catch (NoSuchRoleException e) {
            this.log.error("Could not load role={} while handling distributed event", (Object)roleId, (Object)e);
        }
    }

    private void handleRoleUpdatedDistributedEvent(String roleId) {
        try {
            Role role = this.getRole(roleId);
            this.fireRoleUpdatedEvent(role);
        }
        catch (NoSuchRoleException e) {
            this.log.error("Could not load role={} while handling distributed event", (Object)roleId, (Object)e);
        }
    }

    private void handleRoleDeletedDistributedEvent(String roleId) {
        try {
            Role role = this.getRole(roleId);
            this.fireRoleDeletedEvent(role);
        }
        catch (NoSuchRoleException e) {
            this.log.error("Could not load role={} while handling distributed event", (Object)roleId);
        }
    }

    private void handlePrivilegeCreatedDistributedEvent(String privilegeId) {
        try {
            Privilege privilege = this.getPrivilege(privilegeId);
            this.firePrivilegeCreatedEvent(privilege);
        }
        catch (NoSuchPrivilegeException e) {
            this.log.error("Could not load privilege={} while handling distributed event", (Object)privilegeId);
        }
    }

    private void handlePrivilegeUpdatedDistributedEvent(String privilegeId) {
        try {
            Privilege privilege = this.getPrivilege(privilegeId);
            this.firePrivilegeUpdatedEvent(privilege);
        }
        catch (NoSuchPrivilegeException e) {
            this.log.error("Could not load privilege={} while handling distributed event", (Object)privilegeId);
        }
    }

    private void handlePrivilegeDeletedDistributedEvent(String privilegeId) {
        try {
            Privilege privilege = this.getPrivilege(privilegeId);
            this.firePrivilegeDeletedEvent(privilege);
        }
        catch (NoSuchPrivilegeException e) {
            this.log.error("Could not load privilege={} while handling distributed event", (Object)privilegeId);
        }
    }

    private void fireAuthorizationChangedEvent() {
        this.eventManager.post((Object)new AuthorizationConfigurationChanged());
        this.eventManager.post((Object)new AuthorizationChangedDistributedEvent());
    }

    private void fireRoleCreatedEvent(Role role) {
        this.eventManager.post((Object)new RoleCreatedEvent(role));
    }

    private void fireRoleUpdatedEvent(Role role) {
        this.eventManager.post((Object)new RoleUpdatedEvent(role));
    }

    private void fireRoleDeletedEvent(Role role) {
        this.eventManager.post((Object)new RoleDeletedEvent(role));
    }

    private void firePrivilegeCreatedEvent(Privilege privilege) {
        this.eventManager.post((Object)new PrivilegeCreatedEvent(privilege));
    }

    private void firePrivilegeUpdatedEvent(Privilege privilege) {
        this.eventManager.post((Object)new PrivilegeUpdatedEvent(privilege));
    }

    private void firePrivilegeDeletedEvent(Privilege privilege) {
        this.eventManager.post((Object)new PrivilegeDeletedEvent(privilege));
    }

    private void fireRoleConfigurationDistributedEvent(String roleId, EventType eventType) {
        this.log.debug("Distribute event: roleId={}, type={}", (Object)roleId, (Object)eventType);
        this.eventManager.post((Object)new RoleConfigurationEvent(roleId, eventType));
    }

    private void firePrivilegeConfigurationDistributedEvent(String privilegeId, EventType eventType) {
        this.log.debug("Distribute event: privilegeId={}, type={}", (Object)privilegeId, (Object)eventType);
        this.eventManager.post((Object)new PrivilegeConfigurationEvent(privilegeId, eventType));
    }
}

