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
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.rest.Resource
 *  org.sonatype.nexus.security.privilege.PrivilegeDescriptor
 */
package org.sonatype.nexus.coreui.internal.privileges;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.coreui.internal.privileges.PrivilegesTypesUIResponse;
import org.sonatype.nexus.rest.Resource;
import org.sonatype.nexus.security.privilege.PrivilegeDescriptor;

@Named
@Singleton
@Consumes(value={"application/json"})
@Produces(value={"application/json"})
@Path(value="/internal/ui/privileges")
public class PrivilegesUIResource
extends ComponentSupport
implements Resource {
    private final Map<String, PrivilegeDescriptor> privilegeDescriptors;

    @Inject
    public PrivilegesUIResource(Map<String, PrivilegeDescriptor> privilegeDescriptors) {
        this.privilegeDescriptors = (Map)Preconditions.checkNotNull(privilegeDescriptors);
    }

    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:privileges:read"})
    @GET
    @Path(value="/types")
    public List<PrivilegesTypesUIResponse> listPrivilegesTypes() {
        return this.privilegeDescriptors.entrySet().stream().map(PrivilegesTypesUIResponse::new).collect(Collectors.toList());
    }
}

