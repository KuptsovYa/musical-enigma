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

import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Path;
import org.sonatype.nexus.security.internal.rest.SecurityApiResource;
import org.sonatype.nexus.security.user.UserManager;

@Named
@Singleton
@Path(value="/v1/security/")
public class SecurityApiResourceV1
extends SecurityApiResource {
    public static final String V1_RESOURCE_URI = "/v1/security/";

    @Inject
    public SecurityApiResourceV1(Map<String, UserManager> userManagers) {
        super(userManagers);
    }
}

