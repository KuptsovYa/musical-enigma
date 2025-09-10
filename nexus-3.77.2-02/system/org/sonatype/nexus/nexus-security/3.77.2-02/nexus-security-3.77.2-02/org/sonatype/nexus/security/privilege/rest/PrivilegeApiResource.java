/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.ws.rs.Consumes
 *  javax.ws.rs.DELETE
 *  javax.ws.rs.GET
 *  javax.ws.rs.POST
 *  javax.ws.rs.PUT
 *  javax.ws.rs.Path
 *  javax.ws.rs.PathParam
 *  javax.ws.rs.Produces
 *  javax.ws.rs.core.Response
 *  javax.ws.rs.core.Response$Status
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.nexus.rest.Resource
 *  org.sonatype.nexus.rest.WebApplicationMessageException
 */
package org.sonatype.nexus.security.privilege.rest;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.nexus.rest.Resource;
import org.sonatype.nexus.rest.WebApplicationMessageException;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.privilege.NoSuchPrivilegeException;
import org.sonatype.nexus.security.privilege.PrivilegeDescriptor;
import org.sonatype.nexus.security.privilege.ReadonlyPrivilegeException;
import org.sonatype.nexus.security.privilege.rest.ApiPrivilege;
import org.sonatype.nexus.security.privilege.rest.ApiPrivilegeApplicationRequest;
import org.sonatype.nexus.security.privilege.rest.ApiPrivilegeWildcardRequest;
import org.sonatype.nexus.security.privilege.rest.PrivilegeApiResourceDoc;
import org.sonatype.nexus.security.privilege.rest.PrivilegeApiResourceSupport;

@Consumes(value={"application/json"})
@Produces(value={"application/json"})
public class PrivilegeApiResource
extends PrivilegeApiResourceSupport
implements Resource,
PrivilegeApiResourceDoc {
    @Inject
    public PrivilegeApiResource(SecuritySystem securitySystem, Map<String, PrivilegeDescriptor> privilegeDescriptors) {
        super(securitySystem, privilegeDescriptors);
    }

    @GET
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:privileges:read"})
    public List<ApiPrivilege> getPrivileges() {
        return this.getSecuritySystem().listPrivileges().stream().map(this::toApiPrivilege).sorted(Comparator.comparing(ApiPrivilege::getName)).collect(Collectors.toList());
    }

    @Override
    @GET
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:privileges:read"})
    @Path(value="{privilegeName}")
    public ApiPrivilege getPrivilege(@PathParam(value="privilegeName") String privilegeName) {
        try {
            return this.toApiPrivilege(this.getDefaultAuthorizationManager().getPrivilegeByName(privilegeName));
        }
        catch (NoSuchPrivilegeException e) {
            this.log.debug("Attempt to retrieve privilege '{}' failed, as it wasn't found in the system.", (Object)privilegeName, (Object)e);
            throw new WebApplicationMessageException(Response.Status.NOT_FOUND, (Object)String.format("\"Privilege '%s' not found.\"", privilegeName), "application/json");
        }
    }

    @Override
    @DELETE
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:privileges:delete"})
    @Path(value="{privilegeName}")
    public void deletePrivilege(@PathParam(value="privilegeName") String privilegeName) {
        try {
            this.getDefaultAuthorizationManager().deletePrivilegeByName(privilegeName);
        }
        catch (NoSuchPrivilegeException e) {
            this.log.debug("Attempt to delete privilege '{}' failed, as it wasn't found in the system.", (Object)privilegeName, (Object)e);
            throw new WebApplicationMessageException(Response.Status.NOT_FOUND, (Object)String.format("\"Privilege '%s' not found.\"", privilegeName), "application/json");
        }
        catch (ReadonlyPrivilegeException e) {
            this.log.debug("Attempt to delete privilege '{}' failed, as it is readonly.", (Object)privilegeName, (Object)e);
            throw new WebApplicationMessageException(Response.Status.BAD_REQUEST, (Object)String.format("\"Privilege '%s' is internal and cannot be modified or deleted.\"", privilegeName), "application/json");
        }
    }

    @Override
    @POST
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:privileges:create"})
    @Path(value="application")
    public Response createPrivilege(ApiPrivilegeApplicationRequest privilege) {
        return this.doCreate("application", privilege);
    }

    @Override
    @PUT
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:privileges:update"})
    @Path(value="application/{privilegeName}")
    public void updatePrivilege(@PathParam(value="privilegeName") String privilegeName, ApiPrivilegeApplicationRequest privilege) {
        this.doUpdate(privilegeName, "application", privilege);
    }

    @Override
    @POST
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:privileges:create"})
    @Path(value="wildcard")
    public Response createPrivilege(ApiPrivilegeWildcardRequest privilege) {
        return this.doCreate("wildcard", privilege);
    }

    @Override
    @PUT
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:privileges:update"})
    @Path(value="wildcard/{privilegeName}")
    public void updatePrivilege(@PathParam(value="privilegeName") String privilegeName, ApiPrivilegeWildcardRequest privilege) {
        this.doUpdate(privilegeName, "wildcard", privilege);
    }
}

