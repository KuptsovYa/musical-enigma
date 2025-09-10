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
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Path;
import org.sonatype.nexus.security.internal.rest.SecurityApiResource;
import org.sonatype.nexus.security.user.UserManager;

@Api(hidden=true)
@Named
@Singleton
@Path(value="/beta/security/")
@Deprecated
public class SecurityApiResourceBeta
extends SecurityApiResource {
    public static final String BETA_RESOURCE_URI = "/beta/security/";

    @Inject
    public SecurityApiResourceBeta(Map<String, UserManager> userManagers) {
        super(userManagers);
    }
}

