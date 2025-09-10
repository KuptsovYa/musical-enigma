/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  io.swagger.annotations.ApiParam
 *  io.swagger.annotations.ApiResponse
 *  io.swagger.annotations.ApiResponses
 *  javax.validation.Valid
 *  javax.validation.constraints.NotNull
 *  javax.ws.rs.core.Response
 */
package org.sonatype.nexus.security.privilege.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;
import org.sonatype.nexus.security.privilege.rest.ApiPrivilege;
import org.sonatype.nexus.security.privilege.rest.ApiPrivilegeApplicationRequest;
import org.sonatype.nexus.security.privilege.rest.ApiPrivilegeWildcardRequest;

@Api(value="Security management: privileges")
public interface PrivilegeApiResourceDoc {
    @ApiOperation(value="Retrieve a list of privileges.")
    @ApiResponses(value={@ApiResponse(code=403, message="The user does not have permission to perform the operation.")})
    public Collection<ApiPrivilege> getPrivileges();

    @ApiOperation(value="Retrieve a privilege by name.")
    @ApiResponses(value={@ApiResponse(code=403, message="The user does not have permission to perform the operation."), @ApiResponse(code=404, message="Privilege not found in the system.")})
    public ApiPrivilege getPrivilege(@ApiParam(value="The name of the privilege to retrieve.") @NotNull String var1);

    @ApiOperation(value="Delete a privilege by name.")
    @ApiResponses(value={@ApiResponse(code=400, message="The privilege is internal and may not be altered."), @ApiResponse(code=403, message="The user does not have permission to perform the operation."), @ApiResponse(code=404, message="Privilege not found in the system.")})
    public void deletePrivilege(@ApiParam(value="The name of the privilege to delete.") @NotNull String var1);

    @ApiOperation(value="Create an application type privilege.")
    @ApiResponses(value={@ApiResponse(code=400, message="Privilege object not configured properly."), @ApiResponse(code=403, message="The user does not have permission to perform the operation.")})
    public Response createPrivilege(@ApiParam(value="The privilege to create.") @NotNull @Valid ApiPrivilegeApplicationRequest var1);

    @ApiOperation(value="Update an application type privilege.")
    @ApiResponses(value={@ApiResponse(code=400, message="Privilege object not configured properly."), @ApiResponse(code=403, message="The user does not have permission to perform the operation."), @ApiResponse(code=404, message="Privilege not found in the system.")})
    public void updatePrivilege(@ApiParam(value="The name of the privilege to update.") @NotNull String var1, @ApiParam(value="The privilege to update.") @NotNull @Valid ApiPrivilegeApplicationRequest var2);

    @ApiOperation(value="Create a wildcard type privilege.")
    @ApiResponses(value={@ApiResponse(code=400, message="Privilege object not configured properly."), @ApiResponse(code=403, message="The user does not have permission to perform the operation.")})
    public Response createPrivilege(@ApiParam(value="The privilege to create.") @NotNull @Valid ApiPrivilegeWildcardRequest var1);

    @ApiOperation(value="Update a wildcard type privilege.")
    @ApiResponses(value={@ApiResponse(code=400, message="Privilege object not configured properly."), @ApiResponse(code=403, message="The user does not have permission to perform the operation."), @ApiResponse(code=404, message="Privilege not found in the system.")})
    public void updatePrivilege(@ApiParam(value="The name of the privilege to update.") @NotNull String var1, @ApiParam(value="The privilege to update.") @NotNull @Valid ApiPrivilegeWildcardRequest var2);
}

