/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.ws.rs.Consumes
 *  javax.ws.rs.GET
 *  javax.ws.rs.Path
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
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
import org.sonatype.nexus.security.authz.NoSuchAuthorizationManagerException;
import org.sonatype.nexus.security.role.rest.RoleXOResponse;

@Named
@Singleton
@Consumes(value={"application/json"})
@Produces(value={"application/json"})
@Path(value="internal/ui/roles")
public class RoleInternalResource
extends ComponentSupport
implements Resource {
    static final String RESOURCE_PATH = "internal/ui/roles";
    private final SecuritySystem securitySystem;

    @Inject
    public RoleInternalResource(SecuritySystem securitySystem) {
        this.securitySystem = (SecuritySystem)Preconditions.checkNotNull((Object)securitySystem);
    }

    @GET
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:roles:read"})
    public List<RoleXOResponse> searchRoles(@QueryParam(value="source") String source, @QueryParam(value="search") String search) {
        if (StringUtils.isEmpty((String)source)) {
            return this.securitySystem.listRoles().stream().map(RoleXOResponse::fromRole).sorted(Comparator.comparing(RoleXOResponse::getId)).collect(Collectors.toList());
        }
        try {
            return this.securitySystem.searchRoles(source, search).stream().map(RoleXOResponse::fromRole).sorted(Comparator.comparing(RoleXOResponse::getId)).collect(Collectors.toList());
        }
        catch (NoSuchAuthorizationManagerException e) {
            throw this.buildBadSourceException(source);
        }
    }

    private WebApplicationMessageException buildBadSourceException(String source) {
        this.log.debug("attempt to use invalid source {}", (Object)source);
        return new WebApplicationMessageException(Response.Status.NOT_FOUND, (Object)String.format("\"Source '%s' not found.\"", source), "application/json");
    }
}

