/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.ws.rs.Consumes
 *  javax.ws.rs.POST
 *  javax.ws.rs.Path
 *  javax.ws.rs.Produces
 *  javax.ws.rs.WebApplicationException
 *  javax.ws.rs.core.Response$Status
 *  org.apache.shiro.SecurityUtils
 *  org.apache.shiro.authc.AuthenticationException
 *  org.apache.shiro.authc.AuthenticationToken
 *  org.apache.shiro.authc.UsernamePasswordToken
 *  org.apache.shiro.subject.Subject
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.text.Strings2
 *  org.sonatype.nexus.common.wonderland.AuthTicketService
 *  org.sonatype.nexus.rest.NotCacheable
 *  org.sonatype.nexus.rest.Resource
 */
package org.sonatype.nexus.coreui.internal.wonderland;

import com.google.common.base.Preconditions;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.text.Strings2;
import org.sonatype.nexus.common.wonderland.AuthTicketService;
import org.sonatype.nexus.coreui.internal.wonderland.AuthTicketXO;
import org.sonatype.nexus.coreui.internal.wonderland.AuthTokenXO;
import org.sonatype.nexus.rest.NotCacheable;
import org.sonatype.nexus.rest.Resource;

@Named
@Singleton
@Path(value="/wonderland/authenticate")
public class AuthenticateResource
extends ComponentSupport
implements Resource {
    public static final String RESOURCE_URI = "/wonderland/authenticate";
    private final AuthTicketService authTickets;

    @Inject
    public AuthenticateResource(AuthTicketService authTickets) {
        this.authTickets = (AuthTicketService)Preconditions.checkNotNull((Object)authTickets);
    }

    @POST
    @Consumes(value={"application/xml", "application/json"})
    @Produces(value={"application/xml", "application/json"})
    @NotCacheable
    public AuthTicketXO post(AuthTokenXO token) {
        String principalName;
        Preconditions.checkNotNull((Object)token);
        String username = Strings2.decodeBase64((String)token.getU());
        String password = Strings2.decodeBase64((String)token.getP());
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        String string = principalName = principal == null ? "" : principal.toString();
        if (this.log.isDebugEnabled()) {
            this.log.debug("payload username: {}, payload password: {}, principal: {}", new Object[]{username, Strings2.mask((String)password), principalName});
        }
        if (!principalName.equals(username)) {
            this.log.warn("auth token request denied - authenticated user {} does not match payload user {}", (Object)principalName, (Object)username);
            throw new WebApplicationException("Username mismatch", Response.Status.BAD_REQUEST);
        }
        try {
            SecurityUtils.getSecurityManager().authenticate((AuthenticationToken)new UsernamePasswordToken(username, password));
        }
        catch (AuthenticationException e) {
            this.log.trace("Authentication failed", (Throwable)e);
            throw new WebApplicationException("Authentication failed", Response.Status.FORBIDDEN);
        }
        Optional realmName = subject.getPrincipals().getRealmNames().stream().findFirst();
        return new AuthTicketXO().withT(this.authTickets.createTicket(username, (String)realmName.orElse(null)));
    }
}

