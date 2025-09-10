/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.ws.rs.Path
 */
package org.sonatype.nexus.security.internal.rest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Path;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.config.AdminPasswordFileManager;
import org.sonatype.nexus.security.internal.rest.UserApiResource;

@Named
@Singleton
@Path(value="/v1/security/users/")
public class UserApiResourceV1
extends UserApiResource {
    static final String RESOURCE_URI = "/v1/security/users/";

    @Inject
    public UserApiResourceV1(SecuritySystem securitySystem, AdminPasswordFileManager adminPasswordFileManager) {
        super(securitySystem, adminPasswordFileManager);
    }
}

