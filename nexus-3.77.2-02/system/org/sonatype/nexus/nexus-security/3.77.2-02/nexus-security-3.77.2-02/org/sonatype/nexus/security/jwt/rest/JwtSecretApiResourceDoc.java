/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  io.swagger.annotations.ApiResponse
 *  io.swagger.annotations.ApiResponses
 *  javax.ws.rs.core.Response
 */
package org.sonatype.nexus.security.jwt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.ws.rs.core.Response;

@Api(value="Security management: JWT")
public interface JwtSecretApiResourceDoc {
    @ApiOperation(value="Reset JWT secret (note that session will be expired for the all logged-in users)")
    @ApiResponses(value={@ApiResponse(code=401, message="Authentication required"), @ApiResponse(code=403, message="Insufficient permissions")})
    public Response resetSecret();
}

