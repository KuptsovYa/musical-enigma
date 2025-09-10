/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.ws.rs.GET
 *  javax.ws.rs.Path
 *  javax.ws.rs.Produces
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.rest.Resource
 */
package org.sonatype.nexus.security.internal.rest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.rest.Resource;
import org.sonatype.nexus.security.internal.rest.ApiUserSource;
import org.sonatype.nexus.security.internal.rest.SecurityApiResourceDoc;
import org.sonatype.nexus.security.user.UserManager;

@RequiresAuthentication
@Produces(value={"application/json"})
public class SecurityApiResource
extends ComponentSupport
implements Resource,
SecurityApiResourceDoc {
    private final Map<String, UserManager> userManagers;

    @Inject
    public SecurityApiResource(Map<String, UserManager> userManagers) {
        this.userManagers = userManagers;
    }

    @Override
    @GET
    @Path(value="user-sources")
    @RequiresPermissions(value={"nexus:users:read"})
    public List<ApiUserSource> getUserSources() {
        return this.userManagers.values().stream().filter(um -> !"allConfigured".equals(um.getSource())).map(um -> new ApiUserSource((UserManager)um)).collect(Collectors.toList());
    }
}

