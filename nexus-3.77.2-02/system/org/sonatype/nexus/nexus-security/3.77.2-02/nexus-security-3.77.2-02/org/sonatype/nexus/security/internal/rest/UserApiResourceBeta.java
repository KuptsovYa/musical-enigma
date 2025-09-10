/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.ws.rs.Path
 */
package org.sonatype.nexus.security.internal.rest;

import io.swagger.annotations.Api;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Path;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.config.AdminPasswordFileManager;
import org.sonatype.nexus.security.internal.rest.UserApiResource;

@Api(hidden=true)
@Named
@Singleton
@Path(value="/beta/security/users/")
@Deprecated
public class UserApiResourceBeta
extends UserApiResource {
    static final String RESOURCE_URI = "/beta/security/users/";

    @Inject
    public UserApiResourceBeta(SecuritySystem securitySystem, AdminPasswordFileManager adminPasswordFileManager) {
        super(securitySystem, adminPasswordFileManager);
    }
}

