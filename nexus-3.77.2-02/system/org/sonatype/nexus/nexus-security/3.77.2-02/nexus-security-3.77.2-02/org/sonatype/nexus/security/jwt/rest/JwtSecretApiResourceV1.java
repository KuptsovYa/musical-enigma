/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.ws.rs.Consumes
 *  javax.ws.rs.PUT
 *  javax.ws.rs.Path
 *  javax.ws.rs.Produces
 *  javax.ws.rs.core.Response
 *  javax.ws.rs.core.Response$Status
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.app.FeatureFlag
 *  org.sonatype.nexus.rest.Resource
 */
package org.sonatype.nexus.security.jwt.rest;

import com.google.common.base.Preconditions;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.app.FeatureFlag;
import org.sonatype.nexus.rest.Resource;
import org.sonatype.nexus.security.jwt.SecretStore;
import org.sonatype.nexus.security.jwt.rest.JwtSecretApiResourceDoc;

@FeatureFlag(name="nexus.jwt.enabled")
@Consumes(value={"application/json"})
@Produces(value={"application/json"})
@Path(value="/v1/security/jwt")
@Named
@Singleton
public class JwtSecretApiResourceV1
extends ComponentSupport
implements Resource,
JwtSecretApiResourceDoc {
    public static final String PATH = "/v1/security/jwt";
    private final SecretStore secretStore;

    @Inject
    public JwtSecretApiResourceV1(SecretStore secretStore) {
        this.secretStore = (SecretStore)Preconditions.checkNotNull((Object)secretStore);
    }

    @Override
    @PUT
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:settings:update"})
    public Response resetSecret() {
        String secret = UUID.randomUUID().toString();
        this.secretStore.setSecret(secret);
        return Response.status((Response.Status)Response.Status.OK).build();
    }
}

