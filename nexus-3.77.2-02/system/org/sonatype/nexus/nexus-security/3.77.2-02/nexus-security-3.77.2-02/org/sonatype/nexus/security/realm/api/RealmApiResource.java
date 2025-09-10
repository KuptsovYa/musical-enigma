/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.base.Predicates
 *  javax.inject.Inject
 *  javax.ws.rs.Consumes
 *  javax.ws.rs.GET
 *  javax.ws.rs.PUT
 *  javax.ws.rs.Path
 *  javax.ws.rs.Produces
 *  javax.ws.rs.core.Response$Status
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.rest.Resource
 *  org.sonatype.nexus.rest.WebApplicationMessageException
 */
package org.sonatype.nexus.security.realm.api;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.rest.Resource;
import org.sonatype.nexus.rest.WebApplicationMessageException;
import org.sonatype.nexus.security.realm.RealmManager;
import org.sonatype.nexus.security.realm.SecurityRealm;
import org.sonatype.nexus.security.realm.api.RealmApiResourceDoc;
import org.sonatype.nexus.security.realm.api.RealmApiXO;

@Produces(value={"application/json"})
@Consumes(value={"application/json"})
public class RealmApiResource
extends ComponentSupport
implements RealmApiResourceDoc,
Resource {
    private final RealmManager realmManager;

    @Inject
    public RealmApiResource(RealmManager realmManager) {
        this.realmManager = (RealmManager)Preconditions.checkNotNull((Object)realmManager);
    }

    @Override
    @Path(value="available")
    @GET
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:settings:read"})
    public List<RealmApiXO> getRealms() {
        return this.realmManager.getAvailableRealms().stream().map(RealmApiXO::from).collect(Collectors.toList());
    }

    @Override
    @Path(value="active")
    @GET
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:settings:read"})
    public List<String> getActiveRealms() {
        return this.realmManager.getConfiguredRealmIds();
    }

    @Override
    @Path(value="active")
    @PUT
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:settings:update"})
    public void setActiveRealms(List<String> realmIds) {
        HashSet knownRealmIds = new HashSet();
        this.realmManager.getAvailableRealms().stream().map(SecurityRealm::getId).forEach(knownRealmIds::add);
        List unknownRealms = realmIds.stream().filter(Predicates.not(knownRealmIds::contains)).collect(Collectors.toList());
        if (!unknownRealms.isEmpty()) {
            this.log.debug("Request to set realms with unknown IDs: " + unknownRealms);
            throw new WebApplicationMessageException(Response.Status.BAD_REQUEST, "\"Unknown realmIds: " + unknownRealms + "\"");
        }
        this.realmManager.setConfiguredRealmIds(realmIds);
    }
}

