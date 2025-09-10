/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.validation.Valid
 *  javax.ws.rs.Consumes
 *  javax.ws.rs.GET
 *  javax.ws.rs.PUT
 *  javax.ws.rs.Produces
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.apache.shiro.mgt.RealmSecurityManager
 *  org.apache.shiro.realm.Realm
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.rest.Resource
 *  org.sonatype.nexus.rest.ValidationErrorsException
 */
package org.sonatype.nexus.security.anonymous.rest;

import com.google.common.base.Preconditions;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.Realm;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.rest.Resource;
import org.sonatype.nexus.rest.ValidationErrorsException;
import org.sonatype.nexus.security.anonymous.AnonymousConfiguration;
import org.sonatype.nexus.security.anonymous.AnonymousManager;
import org.sonatype.nexus.security.anonymous.rest.AnonymousAccessApiResourceDoc;
import org.sonatype.nexus.security.anonymous.rest.AnonymousAccessSettingsXO;

@Consumes(value={"application/json"})
@Produces(value={"application/json"})
public class AnonymousAccessApiResource
extends ComponentSupport
implements Resource,
AnonymousAccessApiResourceDoc {
    private final AnonymousManager anonymousManager;
    private final RealmSecurityManager realmSecurityManager;

    @Inject
    public AnonymousAccessApiResource(AnonymousManager anonymousManager, RealmSecurityManager realmSecurityManager) {
        this.anonymousManager = (AnonymousManager)Preconditions.checkNotNull((Object)anonymousManager);
        this.realmSecurityManager = (RealmSecurityManager)Preconditions.checkNotNull((Object)realmSecurityManager);
    }

    @Override
    @GET
    @RequiresPermissions(value={"nexus:settings:read"})
    public AnonymousAccessSettingsXO read() {
        return new AnonymousAccessSettingsXO(this.anonymousManager.getConfiguration());
    }

    @Override
    @PUT
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:settings:update"})
    public AnonymousAccessSettingsXO update(@Valid AnonymousAccessSettingsXO anonymousXO) {
        Realm realm = this.validate(anonymousXO);
        AnonymousConfiguration configuration = this.anonymousManager.newConfiguration();
        configuration.setEnabled(anonymousXO.isEnabled());
        configuration.setUserId(anonymousXO.getUserId());
        configuration.setRealmName(realm.getName());
        this.anonymousManager.setConfiguration(configuration);
        return new AnonymousAccessSettingsXO(this.anonymousManager.getConfiguration());
    }

    Realm validate(AnonymousAccessSettingsXO settings) {
        return this.realmSecurityManager.getRealms().stream().filter(realm -> realm.getName().equals(settings.getRealmName())).findFirst().orElseThrow(() -> new ValidationErrorsException("realmName", "Realm does not exist"));
    }
}

