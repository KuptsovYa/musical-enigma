/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.ws.rs.GET
 *  javax.ws.rs.Path
 *  javax.ws.rs.Produces
 *  javax.ws.rs.core.Response
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.atlas.SystemInformationGenerator
 *  org.sonatype.nexus.rest.Resource
 */
package org.sonatype.nexus.coreui.internal.atlas;

import com.google.common.base.Preconditions;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.atlas.SystemInformationGenerator;
import org.sonatype.nexus.rest.Resource;

@Named
@Singleton
@Path(value="/atlas/system-information")
public class SystemInformationResource
extends ComponentSupport
implements Resource {
    public static final String RESOURCE_URI = "/atlas/system-information";
    private final SystemInformationGenerator systemInformationGenerator;

    @Inject
    public SystemInformationResource(SystemInformationGenerator systemInformationGenerator) {
        this.systemInformationGenerator = (SystemInformationGenerator)Preconditions.checkNotNull((Object)systemInformationGenerator);
    }

    @GET
    @Produces(value={"application/json"})
    @RequiresPermissions(value={"nexus:atlas:read"})
    public Response report() {
        Map report = this.systemInformationGenerator.report();
        return Response.ok((Object)report).header("Content-Disposition", (Object)"attachment; filename=\"sysinfo.json\"").build();
    }
}

