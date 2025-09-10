/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.validation.constraints.NotEmpty
 *  javax.ws.rs.GET
 *  javax.ws.rs.PUT
 *  javax.ws.rs.Path
 *  javax.ws.rs.Produces
 *  javax.ws.rs.WebApplicationException
 *  javax.ws.rs.core.Response$Status
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.rest.Resource
 *  org.sonatype.nexus.security.SecuritySystem
 *  org.sonatype.nexus.security.config.AdminPasswordFileManager
 *  org.sonatype.nexus.security.user.UserNotFoundException
 *  org.sonatype.nexus.validation.Validate
 */
package org.sonatype.nexus.onboarding.internal;

import com.google.common.base.Preconditions;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.onboarding.OnboardingItem;
import org.sonatype.nexus.onboarding.OnboardingManager;
import org.sonatype.nexus.rest.Resource;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.config.AdminPasswordFileManager;
import org.sonatype.nexus.security.user.UserNotFoundException;
import org.sonatype.nexus.validation.Validate;

@Singleton
@Named
@Path(value="internal/ui/onboarding")
public class OnboardingResource
extends ComponentSupport
implements Resource {
    public static final String PASSWORD_REQUIRED = "password is a required field, and cannot be empty.";
    public static final String RESOURCE_URI = "internal/ui/onboarding";
    private final OnboardingManager onboardingManager;
    private final SecuritySystem securitySystem;
    private final AdminPasswordFileManager adminPasswordFileManager;

    @Inject
    public OnboardingResource(OnboardingManager onboardingManager, SecuritySystem securitySystem, AdminPasswordFileManager adminPasswordFileManager) {
        this.onboardingManager = (OnboardingManager)Preconditions.checkNotNull((Object)onboardingManager);
        this.securitySystem = (SecuritySystem)Preconditions.checkNotNull((Object)securitySystem);
        this.adminPasswordFileManager = (AdminPasswordFileManager)Preconditions.checkNotNull((Object)adminPasswordFileManager);
    }

    @GET
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:*"})
    @Produces(value={"application/json"})
    public List<OnboardingItem> getOnboardingItems() {
        return this.onboardingManager.getOnboardingItems();
    }

    @PUT
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:*"})
    @Path(value="/change-admin-password")
    @Validate
    public void changeAdminPassword(@NotEmpty(message="password is a required field, and cannot be empty.") @NotEmpty(message="password is a required field, and cannot be empty.") String password) {
        try {
            this.securitySystem.changePassword("admin", password, false);
            this.adminPasswordFileManager.removeFile();
        }
        catch (UserNotFoundException e) {
            this.log.error("Unable to locate 'admin' user to change password", (Throwable)e);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }
}

