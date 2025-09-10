/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.ws.rs.Path
 */
package org.sonatype.nexus.security.role.rest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Path;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.role.rest.RoleApiResource;

@Named
@Singleton
@Path(value="/v1/security/roles")
public class RoleApiResourceV1
extends RoleApiResource {
    public static final String RESOURCE_URI = "/v1/security/roles";

    @Inject
    public RoleApiResourceV1(SecuritySystem securitySystem) {
        super(securitySystem);
    }
}

