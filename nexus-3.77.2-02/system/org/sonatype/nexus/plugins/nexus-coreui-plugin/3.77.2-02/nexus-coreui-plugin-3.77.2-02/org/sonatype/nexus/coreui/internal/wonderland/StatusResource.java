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
 *  org.apache.shiro.authz.annotation.RequiresUser
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.app.ApplicationVersion
 *  org.sonatype.nexus.rest.Resource
 */
package org.sonatype.nexus.coreui.internal.wonderland;

import com.google.common.base.Preconditions;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.app.ApplicationVersion;
import org.sonatype.nexus.coreui.internal.wonderland.StatusXO;
import org.sonatype.nexus.rest.Resource;

@Named
@Singleton
@Path(value="/wonderland/status")
public class StatusResource
extends ComponentSupport
implements Resource {
    public static final String RESOURCE_URI = "/wonderland/status";
    private final ApplicationVersion applicationVersion;

    @Inject
    public StatusResource(ApplicationVersion applicationVersion) {
        this.applicationVersion = (ApplicationVersion)Preconditions.checkNotNull((Object)applicationVersion);
    }

    @GET
    @Produces(value={"application/xml", "application/json"})
    @RequiresUser
    public StatusXO get() {
        StatusXO result = new StatusXO();
        result.setVersion(this.applicationVersion.getVersion());
        result.setEdition(this.applicationVersion.getEdition());
        return result;
    }
}

