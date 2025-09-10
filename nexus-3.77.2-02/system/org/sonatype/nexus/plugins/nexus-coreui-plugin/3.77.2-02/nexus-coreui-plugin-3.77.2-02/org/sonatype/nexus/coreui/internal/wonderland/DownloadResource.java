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
 *  javax.ws.rs.PathParam
 *  javax.ws.rs.Produces
 *  javax.ws.rs.WebApplicationException
 *  javax.ws.rs.core.Response
 *  javax.ws.rs.core.Response$Status
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.wonderland.AuthTicketService
 *  org.sonatype.nexus.common.wonderland.DownloadService
 *  org.sonatype.nexus.common.wonderland.DownloadService$Download
 *  org.sonatype.nexus.rest.NotCacheable
 *  org.sonatype.nexus.rest.Resource
 */
package org.sonatype.nexus.coreui.internal.wonderland;

import com.google.common.base.Preconditions;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.wonderland.AuthTicketService;
import org.sonatype.nexus.common.wonderland.DownloadService;
import org.sonatype.nexus.rest.NotCacheable;
import org.sonatype.nexus.rest.Resource;

@Named
@Singleton
@Path(value="/wonderland/download")
public class DownloadResource
extends ComponentSupport
implements Resource {
    public static final String RESOURCE_URI = "/wonderland/download";
    private final DownloadService downloadService;
    private final AuthTicketService authTicketService;

    @Inject
    public DownloadResource(DownloadService downloadService, AuthTicketService authTicketService) {
        this.downloadService = (DownloadService)Preconditions.checkNotNull((Object)downloadService);
        this.authTicketService = (AuthTicketService)Preconditions.checkNotNull((Object)authTicketService);
    }

    @GET
    @Path(value="{fileName}")
    @Produces(value={"application/zip"})
    @RequiresPermissions(value={"nexus:wonderland:download"})
    @NotCacheable
    public Response downloadZip(@PathParam(value="fileName") String fileName) {
        Preconditions.checkNotNull((Object)fileName);
        this.log.info("Download: {}", (Object)fileName);
        String authTicket = this.authTicketService.createTicket();
        if (authTicket == null) {
            throw new WebApplicationException("Missing authentication ticket", Response.Status.BAD_REQUEST);
        }
        try {
            DownloadService.Download download = this.downloadService.get(fileName, authTicket);
            if (download == null) {
                return Response.status((Response.Status)Response.Status.NOT_FOUND).build();
            }
            this.log.debug("Sending support ZIP file: {}", (Object)fileName);
            return Response.ok((Object)download.getBytes()).header("Content-Disposition", (Object)("attachment; filename=\"" + fileName + "\"")).header("Content-Length", (Object)download.getLength()).build();
        }
        catch (IOException e) {
            this.log.error("Failed to serve file for download {}", (Object)fileName, (Object)e);
            throw new WebApplicationException("Failed to service file for download", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}

