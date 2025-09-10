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
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 */
package org.sonatype.nexus.security.role.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.sonatype.nexus.security.authz.NoSuchAuthorizationManagerException;
import org.sonatype.nexus.security.role.rest.RoleXORequest;
import org.sonatype.nexus.security.role.rest.RoleXOResponse;

@Api(value="Security management: roles")
public interface RoleApiResourceDoc {
    @ApiOperation(value="List roles")
    @ApiResponses(value={@ApiResponse(code=400, message="The specified source does not exist"), @ApiResponse(code=403, message="Insufficient permissions to read roles")})
    public List<RoleXOResponse> getRoles(@ApiParam(value="The id of the user source to filter the roles by, if supplied. Otherwise roles from all user sources will be returned.") String var1);

    @ApiOperation(value="Create role")
    @ApiResponses(value={@ApiResponse(code=403, message="Insufficient permissions to create role")})
    public RoleXOResponse create(@ApiParam(value="A role configuration", required=true) @NotNull @Valid RoleXORequest var1) throws NoSuchAuthorizationManagerException;

    @ApiOperation(value="Get role")
    @ApiResponses(value={@ApiResponse(code=400, message="The specified source does not exist"), @ApiResponse(code=403, message="Insufficient permissions to read roles"), @ApiResponse(code=404, message="Role not found")})
    public RoleXOResponse getRole(@ApiParam(value="The id of the user source to filter the roles by. Available sources can be fetched using the 'User Sources' endpoint.", defaultValue="default") String var1, @ApiParam(value="The id of the role to get", required=true) @NotEmpty String var2);

    @ApiOperation(value="Update role")
    @ApiResponses(value={@ApiResponse(code=403, message="Insufficient permissions to update role"), @ApiResponse(code=404, message="Role not found")})
    public void update(@ApiParam(value="The id of the role to update", required=true) @NotEmpty String var1, @ApiParam(value="A role configuration", required=true) @NotNull @Valid RoleXORequest var2) throws NoSuchAuthorizationManagerException;

    @ApiOperation(value="Delete role")
    @ApiResponses(value={@ApiResponse(code=403, message="Insufficient permissions to delete role"), @ApiResponse(code=404, message="Role not found")})
    public void delete(@ApiParam(value="The id of the role to delete", required=true) @NotEmpty String var1) throws NoSuchAuthorizationManagerException;
}

