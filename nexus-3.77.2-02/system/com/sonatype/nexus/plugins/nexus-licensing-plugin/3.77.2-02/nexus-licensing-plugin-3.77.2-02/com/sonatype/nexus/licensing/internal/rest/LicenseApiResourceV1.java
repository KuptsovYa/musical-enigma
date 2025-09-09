/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.ws.rs.Path
 *  org.sonatype.nexus.common.app.ApplicationLicense
 */
package com.sonatype.nexus.licensing.internal.rest;

import com.sonatype.nexus.licensing.internal.LicenseUtil;
import com.sonatype.nexus.licensing.internal.rest.LicenseApiResource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Path;
import org.sonatype.nexus.common.app.ApplicationLicense;

@Named
@Singleton
@Path(value="/v1/system/license")
public class LicenseApiResourceV1
extends LicenseApiResource {
    public static final String RESOURCE_URI = "/v1/system/license";

    @Inject
    public LicenseApiResourceV1(ApplicationLicense applicationLicense, LicenseUtil licenseUtil) {
        super(applicationLicense, licenseUtil);
    }
}

