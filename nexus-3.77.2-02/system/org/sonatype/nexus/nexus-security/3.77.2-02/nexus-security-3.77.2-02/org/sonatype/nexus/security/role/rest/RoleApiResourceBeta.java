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
package org.sonatype.nexus.security.role.rest;

import io.swagger.annotations.Api;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Path;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.role.rest.RoleApiResource;

@Api(hidden=true)
@Named
@Singleton
@Path(value="/beta/security/roles")
@Deprecated
public class RoleApiResourceBeta
extends RoleApiResource {
    static final String RESOURCE_URI = "/beta/security/roles";

    @Inject
    public RoleApiResourceBeta(SecuritySystem securitySystem) {
        super(securitySystem);
    }
}

