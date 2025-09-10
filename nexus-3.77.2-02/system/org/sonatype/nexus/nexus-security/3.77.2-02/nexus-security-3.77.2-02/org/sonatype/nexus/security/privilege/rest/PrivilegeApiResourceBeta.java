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
package org.sonatype.nexus.security.privilege.rest;

import io.swagger.annotations.Api;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Path;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.privilege.PrivilegeDescriptor;
import org.sonatype.nexus.security.privilege.rest.PrivilegeApiResource;

@Api(hidden=true)
@Named
@Singleton
@Path(value="/beta/security/privileges")
@Deprecated
public class PrivilegeApiResourceBeta
extends PrivilegeApiResource {
    static final String RESOURCE_URI = "/beta/security/privileges";

    @Inject
    public PrivilegeApiResourceBeta(SecuritySystem securitySystem, Map<String, PrivilegeDescriptor> privilegeDescriptors) {
        super(securitySystem, privilegeDescriptors);
    }
}

