/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.ws.rs.Path
 *  org.apache.shiro.mgt.RealmSecurityManager
 */
package org.sonatype.nexus.security.anonymous.rest;

import io.swagger.annotations.Api;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Path;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.sonatype.nexus.security.anonymous.AnonymousManager;
import org.sonatype.nexus.security.anonymous.rest.AnonymousAccessApiResource;

@Api(hidden=true)
@Named
@Singleton
@Path(value="/beta/security/anonymous")
@Deprecated
public class AnonymousAccessApiResourceBeta
extends AnonymousAccessApiResource {
    static final String RESOURCE_URI = "/beta/security/anonymous";

    @Inject
    public AnonymousAccessApiResourceBeta(AnonymousManager anonymousManager, RealmSecurityManager realmSecurityManager) {
        super(anonymousManager, realmSecurityManager);
    }
}

