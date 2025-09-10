/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.ws.rs.Path
 */
package org.sonatype.nexus.security.privilege.rest;

import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Path;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.privilege.PrivilegeDescriptor;
import org.sonatype.nexus.security.privilege.rest.PrivilegeApiResource;

@Named
@Singleton
@Path(value="/v1/security/privileges")
public class PrivilegeApiResourceV1
extends PrivilegeApiResource {
    static final String RESOURCE_URI = "/v1/security/privileges";

    @Inject
    public PrivilegeApiResourceV1(SecuritySystem securitySystem, Map<String, PrivilegeDescriptor> privilegeDescriptors) {
        super(securitySystem, privilegeDescriptors);
    }
}

