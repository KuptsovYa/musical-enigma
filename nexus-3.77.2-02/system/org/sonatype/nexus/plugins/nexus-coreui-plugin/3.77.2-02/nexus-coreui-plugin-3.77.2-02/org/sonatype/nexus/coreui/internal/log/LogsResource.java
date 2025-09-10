/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.ws.rs.GET
 *  javax.ws.rs.NotFoundException
 *  javax.ws.rs.Path
 *  javax.ws.rs.PathParam
 *  javax.ws.rs.Produces
 *  javax.ws.rs.QueryParam
 *  javax.ws.rs.core.Response
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.log.LogManager
 *  org.sonatype.nexus.logging.task.TaskLogHome
 *  org.sonatype.nexus.rest.Resource
 */
package org.sonatype.nexus.coreui.internal.log;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.log.LogManager;
import org.sonatype.nexus.coreui.internal.log.LogXO;
import org.sonatype.nexus.logging.task.TaskLogHome;
import org.sonatype.nexus.rest.Resource;

@Named
@Singleton
@Path(value="/internal/logging/logs")
public class LogsResource
extends ComponentSupport
implements Resource {
    public static final String RESOURCE_URI = "/internal/logging/logs";
    public static final String DEFAULT_MARK = "MARK";
    private final LogManager logManager;

    @Inject
    public LogsResource(LogManager logManager) {
        this.logManager = (LogManager)Preconditions.checkNotNull((Object)logManager);
    }

    @GET
    @Produces(value={"application/json"})
    @RequiresPermissions(value={"nexus:logging:read"})
    public Set<LogXO> listLogs() throws IOException {
        Set<LogXO> logs = this.logManager.getLogFiles().stream().map(file -> new LogXO(file.toPath())).collect(Collectors.toSet());
        if (TaskLogHome.getTaskLogsHome() != null) {
            this.aggregateLogs(logs, TaskLogHome.getTaskLogsHome());
        }
        if (TaskLogHome.getReplicationLogsHome().isPresent()) {
            this.aggregateLogs(logs, (String)TaskLogHome.getReplicationLogsHome().get());
        }
        return logs;
    }

    private void aggregateLogs(Set<LogXO> logs, String pathname) throws IOException {
        if (pathname != null) {
            try (Stream<java.nio.file.Path> paths = Files.list(Paths.get(pathname, new String[0]));){
                paths.filter(arg_0 -> ((LogManager)this.logManager).isValidLogFile(arg_0)).forEach(path -> logs.add(new LogXO((java.nio.file.Path)path)));
            }
        }
    }

    @GET
    @Path(value="/{filename: .*\\.log}")
    @Produces(value={"text/plain"})
    @RequiresPermissions(value={"nexus:logging:read"})
    public Response get(@PathParam(value="filename") String filename, @QueryParam(value="fromByte") Long fromByte, @QueryParam(value="bytesCount") Long bytesCount) throws NotFoundException, IOException {
        InputStream log;
        Long count;
        Long from = fromByte;
        if (from == null || from < 0L) {
            from = 0L;
        }
        if ((count = bytesCount) == null) {
            count = Long.MAX_VALUE;
        }
        if ((log = this.logManager.getLogFileStream(filename, from.longValue(), count.longValue())) == null) {
            throw new NotFoundException(String.format("%s not found", filename));
        }
        return Response.ok((Object)log).header("Content-Disposition", (Object)String.format("attachment; filename=\"%s\"", filename)).build();
    }
}

