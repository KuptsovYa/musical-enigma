/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.ws.rs.Path
 *  org.apache.shiro.mgt.RealmSecurityManager
 */
package org.sonatype.nexus.security.anonymous.rest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Path;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.sonatype.nexus.security.anonymous.AnonymousManager;
import org.sonatype.nexus.security.anonymous.rest.AnonymousAccessApiResource;

@Named
@Singleton
@Path(value="/v1/security/anonymous")
public class AnonymousAccessApiResourceV1
extends AnonymousAccessApiResource {
    static final String RESOURCE_URI = "/v1/security/anonymous";

    @Inject
    public AnonymousAccessApiResourceV1(AnonymousManager anonymousManager, RealmSecurityManager realmSecurityManager) {
        super(anonymousManager, realmSecurityManager);
    }
}

