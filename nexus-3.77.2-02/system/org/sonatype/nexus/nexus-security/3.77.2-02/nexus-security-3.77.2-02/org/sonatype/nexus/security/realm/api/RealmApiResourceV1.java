/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.ws.rs.Path
 */
package org.sonatype.nexus.security.realm.api;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Path;
import org.sonatype.nexus.security.realm.RealmManager;
import org.sonatype.nexus.security.realm.api.RealmApiResource;

@Named
@Singleton
@Path(value="/v1/security/realms")
public class RealmApiResourceV1
extends RealmApiResource {
    static final String RESOURCE_URL = "/v1/security/realms";

    @Inject
    public RealmApiResourceV1(RealmManager realmManager) {
        super(realmManager);
    }
}

