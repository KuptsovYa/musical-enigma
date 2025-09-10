/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.annotation.ExceptionMetered
 *  com.codahale.metrics.annotation.Timed
 *  com.google.common.base.Preconditions
 *  com.softwarementors.extjs.djn.config.annotations.DirectAction
 *  com.softwarementors.extjs.djn.config.annotations.DirectMethod
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.validation.Valid
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 *  javax.validation.groups.Default
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.nexus.common.text.Strings2
 *  org.sonatype.nexus.extdirect.DirectComponentSupport
 *  org.sonatype.nexus.security.SecuritySystem
 *  org.sonatype.nexus.security.authz.AuthorizationManager
 *  org.sonatype.nexus.security.authz.NoSuchAuthorizationManagerException
 *  org.sonatype.nexus.security.role.Role
 *  org.sonatype.nexus.validation.Validate
 *  org.sonatype.nexus.validation.group.Create
 *  org.sonatype.nexus.validation.group.Update
 */
package org.sonatype.nexus.coreui;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Preconditions;
import com.softwarementors.extjs.djn.config.annotations.DirectAction;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.nexus.common.text.Strings2;
import org.sonatype.nexus.coreui.ReferenceXO;
import org.sonatype.nexus.coreui.RoleXO;
import org.sonatype.nexus.extdirect.DirectComponentSupport;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.authz.AuthorizationManager;
import org.sonatype.nexus.security.authz.NoSuchAuthorizationManagerException;
import org.sonatype.nexus.security.role.Role;
import org.sonatype.nexus.validation.Validate;
import org.sonatype.nexus.validation.group.Create;
import org.sonatype.nexus.validation.group.Update;

@Named
@Singleton
@DirectAction(action={"coreui_Role"})
public class RoleComponent
extends DirectComponentSupport {
    private final SecuritySystem securitySystem;
    private final List<AuthorizationManager> authorizationManagers;

    @Inject
    public RoleComponent(SecuritySystem securitySystem, List<AuthorizationManager> authorizationManagers) {
        this.securitySystem = (SecuritySystem)Preconditions.checkNotNull((Object)securitySystem);
        this.authorizationManagers = (List)Preconditions.checkNotNull(authorizationManagers);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:roles:read"})
    public List<RoleXO> read() throws NoSuchAuthorizationManagerException {
        return this.securitySystem.listRoles("default").stream().map(this::convert).collect(Collectors.toList());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:roles:read"})
    public List<ReferenceXO> readReferences() throws NoSuchAuthorizationManagerException {
        return this.securitySystem.listRoles("default").stream().map(input -> new ReferenceXO(input.getRoleId(), input.getName())).collect(Collectors.toList());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    public List<ReferenceXO> readSources() {
        return this.authorizationManagers.stream().filter(manager -> !"default".equals(manager.getSource())).map(manager -> new ReferenceXO(manager.getSource(), manager.getSource())).collect(Collectors.toList());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:roles:read"})
    @Validate
    public List<RoleXO> readFromSource(@NotEmpty String source) throws NoSuchAuthorizationManagerException {
        return this.securitySystem.listRoles(source).stream().map(this::convert).collect(Collectors.toList());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:roles:create"})
    @Validate(groups={Create.class, Default.class})
    public RoleXO create(@NotNull @Valid RoleXO roleXO) throws NoSuchAuthorizationManagerException {
        if ("LDAP".equals(roleXO.getSource())) {
            this.securitySystem.getAuthorizationManager(roleXO.getSource()).getRole(roleXO.getId());
        }
        return this.convert(this.securitySystem.getAuthorizationManager("default").addRole(new Role(roleXO.getId(), roleXO.getName(), roleXO.getDescription(), roleXO.getSource(), false, roleXO.getRoles(), roleXO.getPrivileges())));
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:roles:update"})
    @Validate(groups={Update.class, Default.class})
    public RoleXO update(@NotNull @Valid RoleXO roleXO) throws NoSuchAuthorizationManagerException {
        Role roleToUpdate = new Role();
        roleToUpdate.setRoleId(roleXO.getId());
        roleToUpdate.setName(roleXO.getName());
        roleToUpdate.setDescription(roleXO.getDescription());
        roleToUpdate.setSource(roleXO.getSource());
        roleToUpdate.setReadOnly(false);
        roleToUpdate.setRoles(roleXO.getRoles());
        roleToUpdate.setPrivileges(roleXO.getPrivileges());
        roleToUpdate.setVersion(Integer.parseInt(roleXO.getVersion()));
        return this.convert(this.securitySystem.getAuthorizationManager("default").updateRole(roleToUpdate));
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:roles:delete"})
    @Validate
    public void remove(@NotEmpty String id) throws NoSuchAuthorizationManagerException {
        this.securitySystem.getAuthorizationManager("default").deleteRole(id);
    }

    private RoleXO convert(Role input) {
        RoleXO roleXO = new RoleXO();
        roleXO.setId(input.getRoleId());
        roleXO.setVersion(String.valueOf(input.getVersion()));
        roleXO.setSource("default".equals(input.getSource()) || Strings2.isBlank((String)input.getSource()) ? "Nexus" : input.getSource());
        roleXO.setName(Strings2.isBlank((String)input.getName()) ? input.getRoleId() : input.getName());
        roleXO.setDescription(Strings2.isBlank((String)input.getDescription()) ? input.getRoleId() : input.getDescription());
        roleXO.setReadOnly(input.isReadOnly());
        roleXO.setPrivileges(input.getPrivileges());
        roleXO.setRoles(input.getRoles());
        return roleXO;
    }
}

