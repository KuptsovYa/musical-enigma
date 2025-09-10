/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.ws.rs.core.Response
 *  javax.ws.rs.core.Response$Status
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.rest.WebApplicationMessageException
 */
package org.sonatype.nexus.security.privilege.rest;

import com.google.common.base.Preconditions;
import java.util.Map;
import javax.ws.rs.core.Response;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.rest.WebApplicationMessageException;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.authz.AuthorizationManager;
import org.sonatype.nexus.security.authz.NoSuchAuthorizationManagerException;
import org.sonatype.nexus.security.privilege.DuplicatePrivilegeException;
import org.sonatype.nexus.security.privilege.NoSuchPrivilegeException;
import org.sonatype.nexus.security.privilege.Privilege;
import org.sonatype.nexus.security.privilege.PrivilegeDescriptor;
import org.sonatype.nexus.security.privilege.ReadonlyPrivilegeException;
import org.sonatype.nexus.security.privilege.rest.ApiPrivilege;
import org.sonatype.nexus.security.privilege.rest.ApiPrivilegeRequest;

public abstract class PrivilegeApiResourceSupport
extends ComponentSupport {
    public static final String PRIV_NOT_FOUND = "\"Privilege '%s' not found.\"";
    public static final String PRIV_INTERNAL = "\"Privilege '%s' is internal and cannot be modified or deleted.\"";
    public static final String PRIV_UNIQUE = "\"Privilege '%s' already exists, use a unique name.\"";
    public static final String PRIV_CONFLICT = "\"The privilege name '%s' does not match the name used in the path '%s'.\"";
    private final SecuritySystem securitySystem;
    private final Map<String, PrivilegeDescriptor> privilegeDescriptors;

    public PrivilegeApiResourceSupport(SecuritySystem securitySystem, Map<String, PrivilegeDescriptor> privilegeDescriptors) {
        this.securitySystem = (SecuritySystem)Preconditions.checkNotNull((Object)securitySystem);
        this.privilegeDescriptors = (Map)Preconditions.checkNotNull(privilegeDescriptors);
    }

    protected Response doCreate(String type, ApiPrivilegeRequest apiPrivilege) {
        try {
            PrivilegeDescriptor privilegeDescriptor = this.privilegeDescriptors.get(type);
            privilegeDescriptor.validate(apiPrivilege);
            this.getDefaultAuthorizationManager().addPrivilege(apiPrivilege.asPrivilege());
            return Response.status((Response.Status)Response.Status.CREATED).build();
        }
        catch (DuplicatePrivilegeException e) {
            this.log.debug("Attempt to create privilege '{}' failed, the name is already in use.", (Object)apiPrivilege.getName(), (Object)e);
            throw new WebApplicationMessageException(Response.Status.BAD_REQUEST, (Object)String.format(PRIV_UNIQUE, apiPrivilege.getName()), "application/json");
        }
    }

    protected void doUpdate(String privilegeName, String type, ApiPrivilegeRequest apiPrivilege) {
        try {
            if (!apiPrivilege.getName().equals(privilegeName)) {
                throw new WebApplicationMessageException(Response.Status.CONFLICT, (Object)String.format(PRIV_CONFLICT, apiPrivilege.getName(), privilegeName), "application/json");
            }
            PrivilegeDescriptor privilegeDescriptor = this.privilegeDescriptors.get(type);
            privilegeDescriptor.validate(apiPrivilege);
            AuthorizationManager authorizationManager = this.getDefaultAuthorizationManager();
            Privilege privilege = authorizationManager.getPrivilegeByName(privilegeName);
            Privilege newPrivilege = apiPrivilege.asPrivilege();
            privilege.setDescription(newPrivilege.getDescription());
            privilege.setProperties(newPrivilege.getProperties());
            authorizationManager.updatePrivilegeByName(privilege);
        }
        catch (NoSuchPrivilegeException e) {
            this.log.debug("Attempt to update privilege '{}' failed, as it wasn't found in the system.", (Object)privilegeName, (Object)e);
            throw new WebApplicationMessageException(Response.Status.NOT_FOUND, (Object)String.format(PRIV_NOT_FOUND, privilegeName), "application/json");
        }
        catch (ReadonlyPrivilegeException e) {
            this.log.debug("Attempt to update internal privilege '{}' failed.", (Object)privilegeName, (Object)e);
            throw new WebApplicationMessageException(Response.Status.BAD_REQUEST, (Object)String.format(PRIV_INTERNAL, privilegeName), "application/json");
        }
    }

    protected ApiPrivilege toApiPrivilege(Privilege privilege) {
        if (privilege == null) {
            return null;
        }
        PrivilegeDescriptor privilegeDescriptor = this.privilegeDescriptors.get(privilege.getType());
        if (privilegeDescriptor == null) {
            return null;
        }
        Object apiPrivilege = privilegeDescriptor.createApiPrivilegeImpl(privilege);
        return apiPrivilege;
    }

    protected SecuritySystem getSecuritySystem() {
        return this.securitySystem;
    }

    protected AuthorizationManager getDefaultAuthorizationManager() {
        try {
            return this.securitySystem.getAuthorizationManager("default");
        }
        catch (NoSuchAuthorizationManagerException e) {
            this.log.error("Unable to retrieve the default authorization manager", (Throwable)e);
            return null;
        }
    }
}

