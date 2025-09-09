/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  io.swagger.annotations.ApiParam
 *  io.swagger.annotations.Example
 *  io.swagger.annotations.ExampleProperty
 *  javax.validation.constraints.NotNull
 */
package com.sonatype.nexus.licensing.internal.rest;

import com.sonatype.nexus.licensing.internal.rest.ApiLicenseDetailsXO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import java.io.IOException;
import java.io.InputStream;
import javax.validation.constraints.NotNull;

@Api(value="Product licensing")
public interface LicenseApiResourceDoc {
    @ApiOperation(value="Get the current license status.")
    public ApiLicenseDetailsXO getLicenseStatus();

    @ApiOperation(value="Upload a new license file.", notes="Server must be restarted to take effect")
    public ApiLicenseDetailsXO setLicense(@ApiParam(examples=@Example(value={@ExampleProperty(mediaType="application/octet-stream", value="")})) @NotNull InputStream var1) throws IOException;

    @ApiOperation(value="Uninstall license if present.")
    public void removeLicense();
}

