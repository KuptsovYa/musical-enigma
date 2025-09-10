/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.base.Strings
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.ws.rs.Consumes
 *  javax.ws.rs.POST
 *  javax.ws.rs.Path
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.log.LogMarker
 *  org.sonatype.nexus.rest.Resource
 */
package org.sonatype.nexus.coreui.internal.log;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.log.LogMarker;
import org.sonatype.nexus.rest.Resource;

@Named
@Singleton
@Path(value="/internal/logging/log")
public class LogResource
extends ComponentSupport
implements Resource {
    public static final String RESOURCE_URI = "/internal/logging/log";
    public static final String DEFAULT_MARK = "MARK";
    private final LogMarker logMarker;

    @Inject
    public LogResource(LogMarker logMarker) {
        this.logMarker = (LogMarker)Preconditions.checkNotNull((Object)logMarker);
    }

    @POST
    @Path(value="/mark")
    @Consumes(value={"text/plain"})
    @RequiresPermissions(value={"nexus:logging:create"})
    public void mark(String message) {
        if (Strings.isNullOrEmpty((String)message)) {
            this.logMarker.markLog(DEFAULT_MARK);
        } else {
            this.logMarker.markLog(message);
        }
    }
}

