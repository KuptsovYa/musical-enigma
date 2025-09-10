/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  io.swagger.annotations.ApiResponse
 *  io.swagger.annotations.ApiResponses
 *  javax.validation.Valid
 */
package org.sonatype.nexus.security.anonymous.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import org.sonatype.nexus.security.anonymous.rest.AnonymousAccessSettingsXO;

@Api(value="Security Management: Anonymous Access")
public interface AnonymousAccessApiResourceDoc {
    @ApiOperation(value="Get Anonymous Access settings")
    @ApiResponses(value={@ApiResponse(code=403, message="Insufficient permissions to update settings")})
    public AnonymousAccessSettingsXO read();

    @ApiOperation(value="Update Anonymous Access settings")
    @ApiResponses(value={@ApiResponse(code=403, message="Insufficient permissions to update settings")})
    public AnonymousAccessSettingsXO update(@Valid AnonymousAccessSettingsXO var1);
}

