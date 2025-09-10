/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.ws.rs.Consumes
 *  javax.ws.rs.DELETE
 *  javax.ws.rs.GET
 *  javax.ws.rs.POST
 *  javax.ws.rs.PUT
 *  javax.ws.rs.Path
 *  javax.ws.rs.PathParam
 *  javax.ws.rs.Produces
 *  javax.ws.rs.QueryParam
 *  javax.ws.rs.core.Response$Status
 *  org.apache.commons.lang.StringUtils
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.text.Strings2
 *  org.sonatype.nexus.rest.Resource
 *  org.sonatype.nexus.rest.ValidationErrorsException
 *  org.sonatype.nexus.rest.WebApplicationMessageException
 *  org.sonatype.nexus.validation.Validate
 */
package org.sonatype.nexus.security.internal.rest;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.text.Strings2;
import org.sonatype.nexus.rest.Resource;
import org.sonatype.nexus.rest.ValidationErrorsException;
import org.sonatype.nexus.rest.WebApplicationMessageException;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.authz.NoSuchAuthorizationManagerException;
import org.sonatype.nexus.security.config.AdminPasswordFileManager;
import org.sonatype.nexus.security.internal.RealmToSource;
import org.sonatype.nexus.security.internal.rest.ApiCreateUser;
import org.sonatype.nexus.security.internal.rest.ApiUser;
import org.sonatype.nexus.security.internal.rest.ApiUserStatus;
import org.sonatype.nexus.security.internal.rest.UserApiResourceDoc;
import org.sonatype.nexus.security.role.Role;
import org.sonatype.nexus.security.role.RoleIdentifier;
import org.sonatype.nexus.security.user.NoSuchUserManagerException;
import org.sonatype.nexus.security.user.User;
import org.sonatype.nexus.security.user.UserNotFoundException;
import org.sonatype.nexus.security.user.UserSearchCriteria;
import org.sonatype.nexus.validation.Validate;

@Consumes(value={"application/json"})
@Produces(value={"application/json"})
public class UserApiResource
extends ComponentSupport
implements Resource,
UserApiResourceDoc {
    public static final String ADMIN_USER_ID = "admin";
    private static final String SAML_SOURCE = "SAML";
    private final SecuritySystem securitySystem;
    private final AdminPasswordFileManager adminPasswordFileManager;

    @Inject
    public UserApiResource(SecuritySystem securitySystem, AdminPasswordFileManager adminPasswordFileManager) {
        this.securitySystem = (SecuritySystem)Preconditions.checkNotNull((Object)securitySystem);
        this.adminPasswordFileManager = (AdminPasswordFileManager)Preconditions.checkNotNull((Object)adminPasswordFileManager);
    }

    @Override
    @GET
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:users:read"})
    public Collection<ApiUser> getUsers(@QueryParam(value="userId") String userId, @QueryParam(value="source") String source) {
        UserSearchCriteria criteria = new UserSearchCriteria(userId, null, source);
        if (!"default".equals(source)) {
            criteria.setLimit(100);
        }
        return this.securitySystem.searchUsers(criteria).stream().map(this::fromUser).collect(Collectors.toList());
    }

    @Override
    @POST
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:users:create"})
    @Validate
    public ApiUser createUser(ApiCreateUser createUser) {
        if (Strings2.isBlank((String)createUser.getPassword())) {
            throw this.createWebException(Response.Status.BAD_REQUEST, "A non-empty password is required.");
        }
        try {
            User user = this.securitySystem.addUser(createUser.toUser(), createUser.getPassword());
            return this.fromUser(user);
        }
        catch (NoSuchUserManagerException e) {
            this.log.error("Unable to locate default usermanager.", (Throwable)e);
            throw this.createNoSuchUserManagerException("default");
        }
    }

    @Override
    @PUT
    @Path(value="{userId}")
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:users:update"})
    @Validate
    public void updateUser(@PathParam(value="userId") String userId, ApiUser apiUser) {
        if (!userId.equals(apiUser.getUserId())) {
            this.log.debug("The path userId '{}' does not match the userId supplied in the body '{}'.", (Object)userId, (Object)apiUser.getUserId());
            throw this.createWebException(Response.Status.BAD_REQUEST, "The path's userId does not match the body");
        }
        try {
            this.validateRoles(apiUser.getRoles());
            if ("default".equals(apiUser.getSource())) {
                this.securitySystem.updateUser(apiUser.toUser());
            } else {
                this.securitySystem.getUser(userId, apiUser.getSource());
                Set<RoleIdentifier> roleIdentifiers = apiUser.getRoles().stream().map(roleId -> new RoleIdentifier("default", (String)roleId)).collect(Collectors.toSet());
                this.securitySystem.setUsersRoles(userId, apiUser.getSource(), roleIdentifiers);
            }
        }
        catch (UserNotFoundException e) {
            this.log.debug("Unable to locate userId: {}", (Object)userId, (Object)e);
            throw this.createUnknownUserException(userId);
        }
        catch (NoSuchUserManagerException e) {
            this.log.debug("Unable to locate source: {}", new Object[]{userId, apiUser.getSource(), e});
            throw this.createNoSuchUserManagerException(apiUser.getSource());
        }
    }

    @Override
    @DELETE
    @Path(value="{userId}")
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:users:delete"})
    public void deleteUser(@PathParam(value="userId") String userId, @QueryParam(value="realm") String realm) {
        User user = null;
        try {
            if (realm == null) {
                user = this.securitySystem.getUser(userId);
                if (!"default".equals(user.getSource()) && !SAML_SOURCE.equals(user.getSource())) {
                    throw this.createWebException(Response.Status.BAD_REQUEST, "Non-local user cannot be deleted.");
                }
            } else {
                if (!this.securitySystem.isValidRealm(realm)) {
                    throw this.createWebException(Response.Status.BAD_REQUEST, "Invalid or empty realm name.");
                }
                user = this.securitySystem.getUser(userId, RealmToSource.getSource(realm));
            }
            this.securitySystem.deleteUser(userId, user.getSource());
        }
        catch (NoSuchUserManagerException e) {
            String source = user.getSource() != null ? user.getSource() : "";
            this.log.error("Unable to locate source: {} for userId: {}", new Object[]{source, userId, e});
            throw this.createNoSuchUserManagerException(source);
        }
        catch (UserNotFoundException e) {
            this.log.debug("Unable to locate userId: {}", (Object)userId, (Object)e);
            throw this.createUnknownUserException(userId);
        }
    }

    @Override
    @PUT
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:*"})
    @Path(value="{userId}/change-password")
    @Consumes(value={"text/plain"})
    @Validate
    public void changePassword(@PathParam(value="userId") String userId, String password) {
        if (StringUtils.isBlank((String)password)) {
            throw this.createWebException(Response.Status.BAD_REQUEST, "Password must be supplied.");
        }
        try {
            this.securitySystem.changePassword(userId, password);
            if (ADMIN_USER_ID.equals(userId)) {
                this.adminPasswordFileManager.removeFile();
            }
        }
        catch (UserNotFoundException e) {
            this.log.debug("Request to change password for invalid user '{}'.", (Object)userId);
            throw this.createUnknownUserException(userId);
        }
    }

    private boolean isReadOnly(User user) {
        try {
            return !this.securitySystem.getUserManager(user.getSource()).supportsWrite();
        }
        catch (NoSuchUserManagerException e) {
            this.log.debug("Unable to locate user manager: {}", (Object)user.getSource(), (Object)e);
            return true;
        }
    }

    @VisibleForTesting
    ApiUser fromUser(User user) {
        Predicate<RoleIdentifier> isLocal = r -> "default".equals(r.getSource());
        Set<String> internalRoles = user.getRoles().stream().filter(isLocal).map(RoleIdentifier::getRoleId).collect(Collectors.toSet());
        Set<String> externalRoles = user.getRoles().stream().filter(isLocal.negate()).map(RoleIdentifier::getRoleId).collect(Collectors.toSet());
        return new ApiUser(user.getUserId(), user.getFirstName(), user.getLastName(), user.getEmailAddress(), user.getSource(), ApiUserStatus.convert(user.getStatus()), this.isReadOnly(user), internalRoles, externalRoles);
    }

    private void validateRoles(Set<String> roleIds) {
        ValidationErrorsException errors = new ValidationErrorsException();
        try {
            Set localRoles = this.securitySystem.listRoles("default").stream().map(Role::getRoleId).collect(Collectors.toSet());
            for (String roleId : roleIds) {
                if (localRoles.contains(roleId)) continue;
                errors.withError("roles", "Unable to locate roleId: " + roleId);
            }
            if (errors.hasValidationErrors()) {
                throw errors;
            }
        }
        catch (NoSuchAuthorizationManagerException e) {
            this.log.error("Unable to locate default user manager", (Throwable)e);
            throw this.createWebException(Response.Status.INTERNAL_SERVER_ERROR, "Unable to locate default user manager");
        }
    }

    private WebApplicationMessageException createNoSuchUserManagerException(String source) {
        return this.createWebException(Response.Status.NOT_FOUND, "Unable to locate source: " + source);
    }

    private WebApplicationMessageException createUnknownUserException(String userId) {
        return this.createWebException(Response.Status.NOT_FOUND, "User '" + userId + "' not found.");
    }

    private WebApplicationMessageException createWebException(Response.Status status, String message) {
        return new WebApplicationMessageException(status, (Object)("\"" + message + "\""), "application/json");
    }
}

