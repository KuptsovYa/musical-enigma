/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.annotation.Timed
 *  com.codahale.metrics.health.HealthCheck$Result
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.validation.constraints.NotNull
 *  javax.ws.rs.GET
 *  javax.ws.rs.Path
 *  javax.ws.rs.PathParam
 *  javax.ws.rs.Produces
 *  javax.ws.rs.core.Response$Status
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.rest.Resource
 *  org.sonatype.nexus.rest.WebApplicationMessageException
 *  org.sonatype.nexus.systemchecks.NodeSystemCheckResult
 *  org.sonatype.nexus.systemchecks.SystemCheckService
 */
package com.sonatype.nexus.pro.systemchecks.internal.status.rest.datastore;

import com.codahale.metrics.annotation.Timed;
import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.Preconditions;
import com.sonatype.nexus.pro.systemchecks.internal.status.rest.datastore.StatusResourceDoc;
import com.sonatype.nexus.pro.systemchecks.internal.status.rest.datastore.SystemCheckResultsApiDTO;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.rest.Resource;
import org.sonatype.nexus.rest.WebApplicationMessageException;
import org.sonatype.nexus.systemchecks.NodeSystemCheckResult;
import org.sonatype.nexus.systemchecks.SystemCheckService;

@Named
@Singleton
@Path(value="/beta/status/check")
@Produces(value={"application/json"})
public class StatusResource
extends ComponentSupport
implements Resource,
StatusResourceDoc {
    public static final String RESOURCE_URI = "/beta/status/check";
    private final SystemCheckService service;

    @Inject
    public StatusResource(SystemCheckService service) {
        this.service = (SystemCheckService)Preconditions.checkNotNull((Object)service);
    }

    @Override
    @GET
    @Path(value="{nodeId}")
    @Timed
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:metrics:read"})
    public SystemCheckResultsApiDTO getNodeSystemStatusChecks(@NotNull @PathParam(value="nodeId") String nodeId) {
        return this.service.getResults().filter(result -> nodeId.equals(result.getNodeId())).findFirst().map(StatusResource::convert).orElseThrow(() -> new WebApplicationMessageException(Response.Status.NOT_FOUND, (Object)"No recent system checks found for specified node", "application/json"));
    }

    @Override
    @Path(value="cluster")
    @GET
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:metrics:read"})
    public List<SystemCheckResultsApiDTO> getClusterSystemStatusChecks() {
        return this.service.getResults().map(StatusResource::convert).collect(Collectors.toList());
    }

    private static SystemCheckResultsApiDTO convert(NodeSystemCheckResult nodeSystemCheckResult) {
        Map<String, SystemCheckResultsApiDTO.SystemCheckResultDTO> results = nodeSystemCheckResult.getResult().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> StatusResource.convert((HealthCheck.Result)entry.getValue())));
        return new SystemCheckResultsApiDTO(nodeSystemCheckResult.getNodeId(), nodeSystemCheckResult.getHostname(), results);
    }

    private static SystemCheckResultsApiDTO.SystemCheckResultDTO convert(HealthCheck.Result result) {
        return new SystemCheckResultsApiDTO.SystemCheckResultDTO(result.isHealthy(), result.getMessage());
    }
}

