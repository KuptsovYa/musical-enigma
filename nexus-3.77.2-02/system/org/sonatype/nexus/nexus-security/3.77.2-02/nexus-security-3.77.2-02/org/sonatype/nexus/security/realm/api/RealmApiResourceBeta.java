/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.ws.rs.Path
 */
package org.sonatype.nexus.security.realm.api;

import io.swagger.annotations.Api;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Path;
import org.sonatype.nexus.security.realm.RealmManager;
import org.sonatype.nexus.security.realm.api.RealmApiResource;

@Api(hidden=true)
@Named
@Singleton
@Path(value="/beta/security/realms")
@Deprecated
public class RealmApiResourceBeta
extends RealmApiResource {
    static final String RESOURCE_URL = "/beta/security/realms";

    @Inject
    public RealmApiResourceBeta(RealmManager realmManager) {
        super(realmManager);
    }
}

