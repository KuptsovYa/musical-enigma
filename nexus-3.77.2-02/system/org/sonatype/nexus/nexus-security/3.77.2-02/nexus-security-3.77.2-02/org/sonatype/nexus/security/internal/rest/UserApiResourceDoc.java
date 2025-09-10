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
 */
package org.sonatype.nexus.security.internal.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.sonatype.nexus.security.internal.rest.ApiCreateUser;
import org.sonatype.nexus.security.internal.rest.ApiUser;

@Api(value="Security management: users")
public interface UserApiResourceDoc {
    public static final String USER_ID_DESCRIPTION = "The userid the request should apply to.";
    public static final String REALM_DESCRIPTION = "The realm the request should apply to.";
    public static final String PASSWORD_DESCRIPTION = "The new password to use.";
    public static final String PASSWORD_REQUIRED = "Password was not supplied in the body of the request";

    @ApiOperation(value="Retrieve a list of users.")
    @ApiResponses(value={@ApiResponse(code=400, message="Password was not supplied in the body of the request"), @ApiResponse(code=403, message="The user does not have permission to perform the operation.")})
    public Collection<ApiUser> getUsers(@ApiParam(value="An optional term to search userids for.") String var1, @ApiParam(value="An optional user source to restrict the search to.") String var2);

    @ApiOperation(value="Create a new user in the default source.")
    @ApiResponses(value={@ApiResponse(code=400, message="Password was not supplied in the body of the request"), @ApiResponse(code=403, message="The user does not have permission to perform the operation.")})
    public ApiUser createUser(@ApiParam(value="A representation of the user to create.") @NotNull @Valid ApiCreateUser var1);

    @ApiOperation(value="Update an existing user.")
    @ApiResponses(value={@ApiResponse(code=400, message="Password was not supplied in the body of the request"), @ApiResponse(code=403, message="The user does not have permission to perform the operation."), @ApiResponse(code=404, message="User or user source not found in the system.")})
    public void updateUser(@ApiParam(value="The userid the request should apply to.") String var1, @ApiParam(value="A representation of the user to update.") @NotNull @Valid ApiUser var2);

    @ApiOperation(value="Delete a user.")
    @ApiResponses(value={@ApiResponse(code=400, message="There was problem deleting a user. Consult the response body for more details"), @ApiResponse(code=403, message="The user does not have permission to perform the operation."), @ApiResponse(code=404, message="User or user source not found in the system.")})
    public void deleteUser(@ApiParam(value="The userid the request should apply to.") String var1, @ApiParam(value="The realm the request should apply to.") String var2);

    @ApiOperation(value="Change a user's password.")
    @ApiResponses(value={@ApiResponse(code=400, message="Password was not supplied in the body of the request"), @ApiResponse(code=403, message="The user does not have permission to perform the operation."), @ApiResponse(code=404, message="User not found in the system.")})
    public void changePassword(@ApiParam(value="The userid the request should apply to.") String var1, @ApiParam(value="The new password to use.") @NotNull String var2);
}

