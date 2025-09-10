/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.validation.Valid
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 *  javax.ws.rs.Consumes
 *  javax.ws.rs.DELETE
 *  javax.ws.rs.DefaultValue
 *  javax.ws.rs.GET
 *  javax.ws.rs.POST
 *  javax.ws.rs.PUT
 *  javax.ws.rs.Path
 *  javax.ws.rs.PathParam
 *  javax.ws.rs.Produces
 *  javax.ws.rs.QueryParam
 *  javax.ws.rs.core.Response$Status
 *  org.apache.commons.lang.StringUtils
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.rest.Resource
 *  org.sonatype.nexus.rest.WebApplicationMessageException
 */
package org.sonatype.nexus.security.role.rest;

import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.rest.Resource;
import org.sonatype.nexus.rest.WebApplicationMessageException;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.authz.AuthorizationManager;
import org.sonatype.nexus.security.authz.NoSuchAuthorizationManagerException;
import org.sonatype.nexus.security.privilege.NoSuchPrivilegeException;
import org.sonatype.nexus.security.role.DuplicateRoleException;
import org.sonatype.nexus.security.role.NoSuchRoleException;
import org.sonatype.nexus.security.role.ReadonlyRoleException;
import org.sonatype.nexus.security.role.Role;
import org.sonatype.nexus.security.role.RoleContainsItselfException;
import org.sonatype.nexus.security.role.rest.RoleApiResourceDoc;
import org.sonatype.nexus.security.role.rest.RoleXORequest;
import org.sonatype.nexus.security.role.rest.RoleXOResponse;

@Consumes(value={"application/json"})
@Produces(value={"application/json"})
public class RoleApiResource
extends ComponentSupport
implements Resource,
RoleApiResourceDoc {
    public static final String SOURCE_NOT_FOUND = "\"Source '%s' not found.\"";
    public static final String ROLE_NOT_FOUND = "\"Role '%s' not found.\"";
    public static final String ROLE_INTERNAL = "\"Role '%s' is internal and cannot be modified or deleted.\"";
    public static final String ROLE_UNIQUE = "\"Role '%s' already exists, use a unique roleId.\"";
    public static final String ROLE_CONFLICT = "\"The Role id '%s' does not match the id used in the path '%s'.\"";
    public static final String CONTAINED_ROLE_NOT_FOUND = "\"Role '%s' contained in role '%s' not found.\"";
    public static final String CONTAINED_PRIV_NOT_FOUND = "\"Privilege '%s' contained in role '%s' not found.\"";
    public static final String ROLE_CONTAINS_ITSELF = "\"Role '%s' cannot contain itself either directly or indirectly through child roles.\"";
    private final SecuritySystem securitySystem;

    @Inject
    public RoleApiResource(SecuritySystem securitySystem) {
        this.securitySystem = (SecuritySystem)Preconditions.checkNotNull((Object)securitySystem);
    }

    @Override
    @GET
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:roles:read"})
    public List<RoleXOResponse> getRoles(@QueryParam(value="source") String source) {
        if (StringUtils.isEmpty((String)source)) {
            return this.securitySystem.listRoles().stream().map(RoleXOResponse::fromRole).sorted(Comparator.comparing(RoleXOResponse::getId)).collect(Collectors.toList());
        }
        try {
            return this.securitySystem.listRoles(source).stream().map(RoleXOResponse::fromRole).sorted(Comparator.comparing(RoleXOResponse::getId)).collect(Collectors.toList());
        }
        catch (NoSuchAuthorizationManagerException e) {
            throw this.buildBadSourceException(source);
        }
    }

    @Override
    @POST
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:roles:create"})
    public RoleXOResponse create(@NotNull @Valid RoleXORequest roleXO) {
        try {
            Role role = this.getDefaultAuthorizationManager().addRole(this.fromXO(roleXO));
            return RoleXOResponse.fromRole(role);
        }
        catch (DuplicateRoleException e) {
            throw this.buildDuplicateRoleException(roleXO.getId());
        }
        catch (NoSuchRoleException e) {
            throw this.buildContainedRoleNotFoundException(e.getRoleId(), roleXO.getId());
        }
        catch (NoSuchPrivilegeException e) {
            throw this.buildContainedPrivilegeNotFoundException(e.getPrivilegeId(), roleXO.getId());
        }
    }

    @Override
    @GET
    @Path(value="/{id}")
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:roles:read"})
    public RoleXOResponse getRole(@DefaultValue(value="default") @QueryParam(value="source") String source, @PathParam(value="id") @NotEmpty String id) {
        try {
            return RoleXOResponse.fromRole(this.securitySystem.getAuthorizationManager(source).getRole(id));
        }
        catch (NoSuchAuthorizationManagerException e) {
            throw this.buildBadSourceException(source);
        }
        catch (NoSuchRoleException e) {
            throw this.buildRoleNotFoundException(id);
        }
    }

    @Override
    @PUT
    @Path(value="/{id}")
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:roles:update"})
    public void update(@PathParam(value="id") @NotEmpty String id, @NotNull @Valid RoleXORequest roleXO) {
        try {
            if (!roleXO.getId().equals(id)) {
                throw this.buildRoleConflictException(roleXO.getId(), id);
            }
            AuthorizationManager authorizationManager = this.getDefaultAuthorizationManager();
            int latestVersion = authorizationManager.getRole(id).getVersion();
            Role role = this.fromXO(roleXO);
            role.setRoleId(id);
            role.setVersion(latestVersion);
            authorizationManager.updateRole(role);
        }
        catch (ReadonlyRoleException e) {
            throw this.buildReadonlyRoleException(id);
        }
        catch (NoSuchRoleException e) {
            if (e.getRoleId().equals(id)) {
                throw this.buildRoleNotFoundException(e.getRoleId());
            }
            throw this.buildContainedRoleNotFoundException(e.getRoleId(), id);
        }
        catch (NoSuchPrivilegeException e) {
            throw this.buildContainedPrivilegeNotFoundException(e.getPrivilegeId(), id);
        }
        catch (RoleContainsItselfException e) {
            throw this.buildRoleContainsItselfException(e.getRoleId());
        }
    }

    @Override
    @DELETE
    @Path(value="/{id}")
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:roles:delete"})
    public void delete(@PathParam(value="id") @NotEmpty String id) {
        AuthorizationManager authorizationManager = this.getDefaultAuthorizationManager();
        try {
            authorizationManager.deleteRole(id);
        }
        catch (NoSuchRoleException e) {
            throw this.buildRoleNotFoundException(id);
        }
        catch (ReadonlyRoleException e) {
            throw this.buildReadonlyRoleException(id);
        }
    }

    private WebApplicationMessageException buildBadSourceException(String source) {
        this.log.debug("attempt to use invalid source {}", (Object)source);
        return new WebApplicationMessageException(Response.Status.BAD_REQUEST, (Object)String.format(SOURCE_NOT_FOUND, source), "application/json");
    }

    private WebApplicationMessageException buildDuplicateRoleException(String id) {
        this.log.debug("attempt to use duplicate role {}", (Object)id);
        return new WebApplicationMessageException(Response.Status.BAD_REQUEST, (Object)String.format(ROLE_UNIQUE, id), "application/json");
    }

    private WebApplicationMessageException buildReadonlyRoleException(String id) {
        this.log.debug("attempt to modify/delete readonly role {}", (Object)id);
        return new WebApplicationMessageException(Response.Status.BAD_REQUEST, (Object)String.format(ROLE_INTERNAL, id), "application/json");
    }

    private WebApplicationMessageException buildRoleNotFoundException(String id) {
        this.log.debug("Role {} not found", (Object)id);
        return new WebApplicationMessageException(Response.Status.NOT_FOUND, (Object)String.format(ROLE_NOT_FOUND, id), "application/json");
    }

    private WebApplicationMessageException buildContainedRoleNotFoundException(String containedId, String roleId) {
        this.log.debug("Role {} in role {} not found", (Object)containedId, (Object)roleId);
        return new WebApplicationMessageException(Response.Status.BAD_REQUEST, (Object)String.format(CONTAINED_ROLE_NOT_FOUND, containedId, roleId), "application/json");
    }

    private WebApplicationMessageException buildContainedPrivilegeNotFoundException(String containedId, String roleId) {
        this.log.debug("Privilege {} in role {} not found", (Object)containedId, (Object)roleId);
        return new WebApplicationMessageException(Response.Status.BAD_REQUEST, (Object)String.format(CONTAINED_PRIV_NOT_FOUND, containedId, roleId), "application/json");
    }

    private WebApplicationMessageException buildRoleConflictException(String xoId, String pathId) {
        this.log.debug("XO id {} and path id {} do not match", (Object)xoId, (Object)pathId);
        return new WebApplicationMessageException(Response.Status.CONFLICT, (Object)String.format(ROLE_CONFLICT, xoId, pathId), "application/json");
    }

    private WebApplicationMessageException buildRoleContainsItselfException(String roleId) {
        this.log.debug("Role {} cannot contain itself either directly or indirectly.", (Object)roleId);
        return new WebApplicationMessageException(Response.Status.BAD_REQUEST, (Object)String.format(ROLE_CONTAINS_ITSELF, roleId), "application/json");
    }

    private Role fromXO(RoleXORequest roleXO) {
        return new Role(roleXO.getId(), roleXO.getName(), roleXO.getDescription(), "default", false, roleXO.getRoles(), roleXO.getPrivileges());
    }

    private AuthorizationManager getDefaultAuthorizationManager() {
        try {
            return this.securitySystem.getAuthorizationManager("default");
        }
        catch (NoSuchAuthorizationManagerException e) {
            this.log.error("Unable to retrieve the default authorization manager", (Throwable)e);
            return null;
        }
    }
}

