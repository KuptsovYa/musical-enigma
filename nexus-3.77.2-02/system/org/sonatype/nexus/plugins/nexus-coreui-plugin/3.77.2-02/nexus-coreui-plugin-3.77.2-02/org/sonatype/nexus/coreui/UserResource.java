/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.validation.Valid
 *  javax.validation.constraints.NotNull
 *  javax.ws.rs.Consumes
 *  javax.ws.rs.GET
 *  javax.ws.rs.PUT
 *  javax.ws.rs.Path
 *  javax.ws.rs.PathParam
 *  javax.ws.rs.Produces
 *  javax.ws.rs.core.Response$Status
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.apache.shiro.authz.annotation.RequiresUser
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.wonderland.AuthTicketService
 *  org.sonatype.nexus.rest.Resource
 *  org.sonatype.nexus.rest.WebApplicationMessageException
 *  org.sonatype.nexus.security.SecuritySystem
 *  org.sonatype.nexus.security.anonymous.AnonymousConfiguration
 *  org.sonatype.nexus.security.anonymous.AnonymousManager
 *  org.sonatype.nexus.security.user.NoSuchUserManagerException
 *  org.sonatype.nexus.security.user.User
 *  org.sonatype.nexus.security.user.UserNotFoundException
 *  org.sonatype.nexus.validation.Validate
 */
package org.sonatype.nexus.coreui;

import com.google.common.base.Preconditions;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.wonderland.AuthTicketService;
import org.sonatype.nexus.coreui.UserAccountPasswordXO;
import org.sonatype.nexus.coreui.UserAccountXO;
import org.sonatype.nexus.rest.Resource;
import org.sonatype.nexus.rest.WebApplicationMessageException;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.anonymous.AnonymousConfiguration;
import org.sonatype.nexus.security.anonymous.AnonymousManager;
import org.sonatype.nexus.security.user.NoSuchUserManagerException;
import org.sonatype.nexus.security.user.User;
import org.sonatype.nexus.security.user.UserNotFoundException;
import org.sonatype.nexus.validation.Validate;

@Named
@Singleton
@Consumes(value={"application/json"})
@Produces(value={"application/json"})
@Path(value="internal/ui/user")
public class UserResource
extends ComponentSupport
implements Resource {
    static final String RESOURCE_PATH = "internal/ui/user";
    private final SecuritySystem securitySystem;
    private final AuthTicketService authTickets;
    private final AnonymousManager anonymousManager;

    @Inject
    public UserResource(SecuritySystem securitySystem, AuthTicketService authTickets, AnonymousManager anonymousManager) {
        this.securitySystem = (SecuritySystem)Preconditions.checkNotNull((Object)securitySystem);
        this.authTickets = (AuthTicketService)Preconditions.checkNotNull((Object)authTickets);
        this.anonymousManager = (AnonymousManager)Preconditions.checkNotNull((Object)anonymousManager);
    }

    @GET
    @RequiresUser
    public UserAccountXO readAccount() throws UserNotFoundException {
        return this.convert(this.getCurrentUser());
    }

    @PUT
    @RequiresUser
    @RequiresAuthentication
    @Validate
    public void updateAccount(@NotNull @Valid UserAccountXO xo) throws UserNotFoundException, NoSuchUserManagerException {
        User user = this.getCurrentUser();
        if (!user.getUserId().equals(xo.getUserId())) {
            throw new WebApplicationMessageException(Response.Status.BAD_REQUEST, "Mismatch between authenticated user and user to update.");
        }
        user.setFirstName(xo.getFirstName());
        user.setLastName(xo.getLastName());
        user.setEmailAddress(xo.getEmail());
        this.securitySystem.updateUser(user);
    }

    @PUT
    @Path(value="/{userId}/password")
    @RequiresUser
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:userschangepw:create"})
    @Validate
    public void changePassword(@PathParam(value="userId") @NotNull String userId, @NotNull @Valid UserAccountPasswordXO xo) throws Exception {
        if (this.authTickets.redeemTicket(xo.getAuthToken())) {
            if (this.isAnonymousUser(userId)) {
                throw new WebApplicationMessageException(Response.Status.BAD_REQUEST, "Password cannot be changed for user " + userId + ", as it is configured as the Anonymous user");
            }
        } else {
            throw new WebApplicationMessageException(Response.Status.FORBIDDEN, "Invalid authentication ticket");
        }
        this.securitySystem.changePassword(userId, xo.getPassword());
    }

    private User getCurrentUser() throws UserNotFoundException {
        User user = this.securitySystem.currentUser();
        if (user != null) {
            return user;
        }
        throw new UserNotFoundException("Unable to get current user");
    }

    UserAccountXO convert(User user) {
        UserAccountXO xo = new UserAccountXO();
        xo.setUserId(user.getUserId());
        xo.setFirstName(user.getFirstName());
        xo.setLastName(user.getLastName());
        xo.setEmail(user.getEmailAddress());
        xo.setExternal(!"default".equals(user.getSource()));
        return xo;
    }

    private boolean isAnonymousUser(String userId) {
        AnonymousConfiguration config = this.anonymousManager.getConfiguration();
        return config.isEnabled() && config.getUserId().equals(userId);
    }
}

