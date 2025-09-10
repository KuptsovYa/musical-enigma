/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  io.swagger.annotations.ApiParam
 *  io.swagger.annotations.ApiResponse
 *  io.swagger.annotations.ApiResponses
 *  javax.validation.constraints.NotNull
 *  javax.ws.rs.GET
 */
package com.sonatype.nexus.pro.systemchecks.internal.status.rest.datastore;

import com.sonatype.nexus.pro.systemchecks.internal.status.rest.datastore.SystemCheckResultsApiDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;

@Api(value="Status")
public interface StatusResourceDoc {
    @GET
    @ApiOperation(value="Health check endpoint that returns the results of the system status checks of specified Node")
    @ApiResponses(value={@ApiResponse(code=404, message="System status information not found"), @ApiResponse(code=200, message="The system status check results")})
    public SystemCheckResultsApiDTO getNodeSystemStatusChecks(@NotNull @ApiParam(value="Node Id") String var1);

    @ApiOperation(value="Health check endpoint that returns the results of the system status checks")
    @ApiResponses(value={@ApiResponse(code=403, message="Insufficient permissions to retrieve system status checks")})
    public List<SystemCheckResultsApiDTO> getClusterSystemStatusChecks();
}

