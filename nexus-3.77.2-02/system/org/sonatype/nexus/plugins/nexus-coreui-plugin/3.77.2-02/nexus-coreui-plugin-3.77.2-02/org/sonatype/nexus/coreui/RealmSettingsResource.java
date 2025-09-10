/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.ws.rs.Consumes
 *  javax.ws.rs.GET
 *  javax.ws.rs.Path
 *  javax.ws.rs.Produces
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.rest.Resource
 *  org.sonatype.nexus.security.anonymous.AnonymousHelper
 *  org.sonatype.nexus.security.realm.RealmManager
 *  org.sonatype.nexus.security.realm.SecurityRealm
 *  org.sonatype.nexus.security.user.UserManager
 */
package org.sonatype.nexus.coreui;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.rest.Resource;
import org.sonatype.nexus.security.anonymous.AnonymousHelper;
import org.sonatype.nexus.security.realm.RealmManager;
import org.sonatype.nexus.security.realm.SecurityRealm;
import org.sonatype.nexus.security.user.UserManager;

@Named
@Singleton
@Consumes(value={"application/json"})
@Produces(value={"application/json"})
@Path(value="internal/ui/realms")
public class RealmSettingsResource
extends ComponentSupport
implements Resource {
    static final String RESOURCE_PATH = "internal/ui/realms";
    private final RealmManager realmManager;
    private final List<String> authenticationRealms;

    @Inject
    public RealmSettingsResource(RealmManager realmManager, List<UserManager> userManagers) {
        this.realmManager = (RealmManager)Preconditions.checkNotNull((Object)realmManager);
        Preconditions.checkNotNull(userManagers);
        this.authenticationRealms = AnonymousHelper.getAuthenticationRealms(userManagers);
    }

    @GET
    @Path(value="/types")
    @RequiresPermissions(value={"nexus:settings:read"})
    public List<SecurityRealm> readRealmTypes() {
        return this.realmManager.getAvailableRealms(true).stream().filter(securityRealm -> this.authenticationRealms.contains(securityRealm.getId())).collect(Collectors.toList());
    }
}

