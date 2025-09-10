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
 *  javax.ws.rs.POST
 *  javax.ws.rs.PUT
 *  javax.ws.rs.Path
 *  javax.ws.rs.PathParam
 *  javax.ws.rs.Produces
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.log.LogManager
 *  org.sonatype.nexus.common.log.LoggerLevel
 *  org.sonatype.nexus.rest.Resource
 */
package org.sonatype.nexus.coreui.internal.log;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.log.LogManager;
import org.sonatype.nexus.common.log.LoggerLevel;
import org.sonatype.nexus.coreui.internal.log.LoggerXO;
import org.sonatype.nexus.coreui.internal.log.UpdateLoggingConfigurationRequest;
import org.sonatype.nexus.rest.Resource;

@Named
@Singleton
@Consumes(value={"application/json"})
@Produces(value={"application/json"})
@Path(value="internal/ui/loggingConfiguration")
public class LoggingConfigurationResource
extends ComponentSupport
implements Resource {
    static final String RESOURCE_PATH = "internal/ui/loggingConfiguration";
    private static final String ROOT = "ROOT";
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
    private LogManager logManager;

    @Inject
    public LoggingConfigurationResource(LogManager logManager) {
        this.logManager = (LogManager)Preconditions.checkNotNull((Object)logManager);
    }

    @GET
    @RequiresPermissions(value={"nexus:logging:read"})
    public Collection<LoggerXO> readAll() {
        lock.readLock().lock();
        try {
            Collection collection = this.logManager.getEffectiveLoggersUpdatedByFetchedOverrides().entrySet().stream().map(LoggerXO::fromEntry).collect(Collectors.toSet());
            return collection;
        }
        finally {
            lock.readLock().unlock();
        }
    }

    @POST
    @Path(value="/reset")
    @RequiresPermissions(value={"nexus:logging:update"})
    public void resetAll() {
        lock.writeLock().lock();
        try {
            this.logManager.resetLoggers();
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @GET
    @Path(value="/{name}")
    @RequiresPermissions(value={"nexus:logging:read"})
    public LoggerXO read(@PathParam(value="name") String name) {
        lock.readLock().lock();
        try {
            LoggerXO logger = new LoggerXO();
            logger.setName(name);
            logger.setLevel(this.logManager.getLoggerEffectiveLevel(name));
            logger.setOverride(this.logManager.getLoggers().containsKey(name));
            LoggerXO loggerXO = logger;
            return loggerXO;
        }
        finally {
            lock.readLock().unlock();
        }
    }

    @PUT
    @Path(value="/{name}")
    @RequiresPermissions(value={"nexus:logging:update"})
    public void update(@PathParam(value="name") String name, UpdateLoggingConfigurationRequest request) {
        lock.writeLock().lock();
        try {
            if (request.getLevel() == LoggerLevel.DEFAULT) {
                this.logManager.unsetLoggerLevel(name);
            } else {
                this.logManager.setLoggerLevel(name, request.getLevel());
            }
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    @POST
    @Path(value="/{name}/reset")
    @RequiresPermissions(value={"nexus:logging:update"})
    public void reset(@PathParam(value="name") String name) {
        lock.writeLock().lock();
        try {
            this.logManager.unsetLoggerLevel(name);
        }
        finally {
            lock.writeLock().unlock();
        }
    }
}

