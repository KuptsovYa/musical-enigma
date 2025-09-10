/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  io.swagger.annotations.ApiResponse
 *  io.swagger.annotations.ApiResponses
 */
package org.sonatype.nexus.security.internal.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.sonatype.nexus.security.internal.rest.ApiUserSource;

@Api(value="Security management")
public interface SecurityApiResourceDoc {
    @ApiOperation(value="Retrieve a list of the available user sources.")
    @ApiResponses(value={@ApiResponse(code=403, message="The user does not have permission to perform the operation.")})
    public List<ApiUserSource> getUserSources();
}

