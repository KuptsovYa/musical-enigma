/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.ws.rs.Path
 *  org.sonatype.nexus.common.app.ApplicationLicense
 */
package com.sonatype.nexus.licensing.internal.rest;

import com.sonatype.nexus.licensing.internal.LicenseUtil;
import com.sonatype.nexus.licensing.internal.rest.LicenseApiResource;
import io.swagger.annotations.Api;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Path;
import org.sonatype.nexus.common.app.ApplicationLicense;

@Api(hidden=true)
@Named
@Singleton
@Path(value="/beta/system/license")
@Deprecated
public class LicenseApiResourceBeta
extends LicenseApiResource {
    public static final String RESOURCE_URI = "/beta/system/license";

    @Inject
    public LicenseApiResourceBeta(ApplicationLicense applicationLicense, LicenseUtil licenseUtil) {
        super(applicationLicense, licenseUtil);
    }
}

