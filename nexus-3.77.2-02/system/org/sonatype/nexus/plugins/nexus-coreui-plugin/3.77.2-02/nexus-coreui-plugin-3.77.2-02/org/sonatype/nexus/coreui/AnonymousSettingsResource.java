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
 *  javax.ws.rs.Produces
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.rest.Resource
 *  org.sonatype.nexus.security.anonymous.AnonymousConfiguration
 *  org.sonatype.nexus.security.anonymous.AnonymousManager
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
import javax.ws.rs.Produces;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.coreui.AnonymousSettingsXO;
import org.sonatype.nexus.rest.Resource;
import org.sonatype.nexus.security.anonymous.AnonymousConfiguration;
import org.sonatype.nexus.security.anonymous.AnonymousManager;

@Named
@Singleton
@Consumes(value={"application/json"})
@Produces(value={"application/json"})
@Path(value="internal/ui/anonymous-settings")
public class AnonymousSettingsResource
extends ComponentSupport
implements Resource {
    static final String RESOURCE_PATH = "internal/ui/anonymous-settings";
    private final AnonymousManager anonymousManager;

    @Inject
    public AnonymousSettingsResource(AnonymousManager anonymousManager) {
        this.anonymousManager = (AnonymousManager)Preconditions.checkNotNull((Object)anonymousManager);
    }

    @GET
    @RequiresPermissions(value={"nexus:settings:read"})
    public AnonymousSettingsXO read() {
        AnonymousConfiguration config = this.anonymousManager.getConfiguration();
        AnonymousSettingsXO xo = new AnonymousSettingsXO();
        xo.setEnabled(config.isEnabled());
        xo.setUserId(config.getUserId());
        xo.setRealmName(config.getRealmName());
        return xo;
    }

    @PUT
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:settings:update"})
    public void update(@NotNull @Valid AnonymousSettingsXO anonymousXO) {
        AnonymousConfiguration configuration = this.anonymousManager.newConfiguration();
        configuration.setEnabled(anonymousXO.getEnabled().booleanValue());
        configuration.setRealmName(anonymousXO.getRealmName());
        configuration.setUserId(anonymousXO.getUserId());
        this.anonymousManager.setConfiguration(configuration);
    }
}

