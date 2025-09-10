/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.ws.rs.GET
 *  javax.ws.rs.NotFoundException
 *  javax.ws.rs.Path
 *  javax.ws.rs.PathParam
 *  javax.ws.rs.Produces
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.rest.Resource
 *  org.sonatype.nexus.security.SecuritySystem
 *  org.sonatype.nexus.security.authz.AuthorizationManager
 *  org.sonatype.nexus.security.authz.NoSuchAuthorizationManagerException
 *  org.sonatype.nexus.security.privilege.NoSuchPrivilegeException
 *  org.sonatype.nexus.security.privilege.Privilege
 *  org.sonatype.nexus.security.role.NoSuchRoleException
 *  org.sonatype.nexus.security.role.Role
 *  org.sonatype.nexus.security.user.User
 *  org.sonatype.nexus.security.user.UserNotFoundException
 */
package org.sonatype.nexus.coreui.internal.atlas;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.rest.Resource;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.authz.AuthorizationManager;
import org.sonatype.nexus.security.authz.NoSuchAuthorizationManagerException;
import org.sonatype.nexus.security.privilege.NoSuchPrivilegeException;
import org.sonatype.nexus.security.privilege.Privilege;
import org.sonatype.nexus.security.role.NoSuchRoleException;
import org.sonatype.nexus.security.role.Role;
import org.sonatype.nexus.security.user.User;
import org.sonatype.nexus.security.user.UserNotFoundException;

@Named
@Singleton
@Path(value="/atlas/security-diagnostic")
@Produces(value={"application/json"})
public class SecurityDiagnosticResource
extends ComponentSupport
implements Resource {
    public static final String RESOURCE_URI = "/atlas/security-diagnostic";
    private static final String USER_FIELD = "user";
    private static final String USERID_FIELD = "userId";
    private static final String NAME_FIELD = "name";
    private static final String FIRST_NAME_FIELD = "firstName";
    private static final String LAST_NAME_FIELD = "lastName";
    private static final String EMAIL_FIELD = "emailAddress";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String SOURCE_FIELD = "source";
    private static final String STATUS_FIELD = "status";
    private static final String VERSION_FIELD = "version";
    private static final String ROLES_FIELD = "roles";
    private static final String PRIVILEGES_FIELD = "privileges";
    private static final String PERMISSION_FIELD = "permission";
    private static final String PROPERTIES_FIELD = "properties";
    private final SecuritySystem securitySystem;

    @Inject
    public SecurityDiagnosticResource(SecuritySystem securitySystem) {
        this.securitySystem = (SecuritySystem)Preconditions.checkNotNull((Object)securitySystem);
    }

    @GET
    @Path(value="user/{userId}")
    @RequiresPermissions(value={"nexus:atlas:read"})
    public Map<String, Object> userDiagnostic(@PathParam(value="userId") String userId) {
        try {
            this.log.info("Generating security diagnostics for user: {}", (Object)userId);
            HashMap<String, Object> userDataMap = new HashMap<String, Object>();
            this.populateUserDataMap(userDataMap, this.securitySystem.getAuthorizationManager("default"), userId);
            return userDataMap;
        }
        catch (NoSuchAuthorizationManagerException e) {
            this.log.debug("Default AuthorizationManager not found", (Throwable)e);
            throw new RuntimeException(e);
        }
    }

    private void populateUserDataMap(Map<String, Object> userDataMap, AuthorizationManager authorizationManager, String id) {
        try {
            userDataMap.put(USER_FIELD, this.toMap(this.securitySystem.getUser(id), authorizationManager));
        }
        catch (UserNotFoundException e) {
            this.log.debug("User not found: {}", (Object)id, (Object)e);
            throw new NotFoundException("User not found");
        }
    }

    private void populateRoleDataMap(Map<String, Object> roleDataMap, AuthorizationManager authorizationManager, String id) {
        try {
            roleDataMap.put(id, this.toMap(authorizationManager.getRole(id), authorizationManager));
        }
        catch (NoSuchRoleException e) {
            roleDataMap.put(id, "ERROR: Failed to resolve role: " + id + " caused by: " + e.getMessage());
        }
    }

    private void populatePrivilegeDataMap(Map<String, Object> privilegeDataMap, AuthorizationManager authorizationManager, String id) {
        try {
            privilegeDataMap.put(id, this.toMap(authorizationManager.getPrivilege(id)));
        }
        catch (NoSuchPrivilegeException e) {
            privilegeDataMap.put(id, "ERROR: Failed to resolve privilege: " + id + " caused by: " + e.getMessage());
        }
    }

    private Map<String, Object> toMap(User user, AuthorizationManager authorizationManager) {
        HashMap<String, Object> userData = new HashMap<String, Object>();
        userData.put(USERID_FIELD, user.getUserId());
        userData.put(NAME_FIELD, user.getName());
        userData.put(FIRST_NAME_FIELD, user.getFirstName());
        userData.put(LAST_NAME_FIELD, user.getLastName());
        userData.put(EMAIL_FIELD, user.getEmailAddress());
        userData.put(SOURCE_FIELD, user.getSource());
        userData.put(STATUS_FIELD, user.getStatus());
        userData.put(VERSION_FIELD, user.getVersion());
        HashMap roleDataMap = new HashMap();
        userData.put(ROLES_FIELD, roleDataMap);
        user.getRoles().forEach(childRole -> this.populateRoleDataMap(roleDataMap, authorizationManager, childRole.getRoleId()));
        return userData;
    }

    private Map<String, Object> toMap(Role role, AuthorizationManager authorizationManager) {
        HashMap<String, Object> roleData = new HashMap<String, Object>();
        roleData.put(NAME_FIELD, role.getName());
        roleData.put(SOURCE_FIELD, role.getSource());
        roleData.put(DESCRIPTION_FIELD, role.getDescription());
        roleData.put(VERSION_FIELD, role.getVersion());
        HashMap childRoleData = new HashMap();
        roleData.put(ROLES_FIELD, childRoleData);
        role.getRoles().forEach(childRole -> this.populateRoleDataMap(childRoleData, authorizationManager, (String)childRole));
        HashMap privilegeData = new HashMap();
        roleData.put(PRIVILEGES_FIELD, privilegeData);
        role.getPrivileges().forEach(privilege -> this.populatePrivilegeDataMap(privilegeData, authorizationManager, (String)privilege));
        return roleData;
    }

    private Map<String, Object> toMap(Privilege privilege) {
        HashMap<String, Object> privilegeData = new HashMap<String, Object>();
        privilegeData.put(DESCRIPTION_FIELD, privilege.getDescription());
        privilegeData.put(NAME_FIELD, privilege.getName());
        privilegeData.put(PERMISSION_FIELD, privilege.getPermission());
        privilegeData.put(PROPERTIES_FIELD, privilege.getProperties());
        privilegeData.put(VERSION_FIELD, privilege.getVersion());
        return privilegeData;
    }
}

