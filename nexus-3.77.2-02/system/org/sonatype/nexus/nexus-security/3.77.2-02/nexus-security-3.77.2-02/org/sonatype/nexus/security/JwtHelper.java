/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.auth0.jwt.JWT
 *  com.auth0.jwt.interfaces.DecodedJWT
 *  com.google.common.base.Preconditions
 *  com.google.common.eventbus.Subscribe
 *  com.google.inject.Provider
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.servlet.http.Cookie
 *  org.apache.shiro.subject.Subject
 *  org.sonatype.nexus.common.app.FeatureFlag
 *  org.sonatype.nexus.common.app.ManagedLifecycle
 *  org.sonatype.nexus.common.app.ManagedLifecycle$Phase
 *  org.sonatype.nexus.common.event.EventAware
 *  org.sonatype.nexus.common.stateguard.StateGuardLifecycleSupport
 */
package org.sonatype.nexus.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Provider;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.Cookie;
import org.apache.shiro.subject.Subject;
import org.sonatype.nexus.common.app.FeatureFlag;
import org.sonatype.nexus.common.app.ManagedLifecycle;
import org.sonatype.nexus.common.event.EventAware;
import org.sonatype.nexus.common.stateguard.StateGuardLifecycleSupport;
import org.sonatype.nexus.security.jwt.JwtSecretChanged;
import org.sonatype.nexus.security.jwt.JwtVerificationException;
import org.sonatype.nexus.security.jwt.JwtVerifier;
import org.sonatype.nexus.security.jwt.SecretStore;

@Named
@ManagedLifecycle(phase=ManagedLifecycle.Phase.SECURITY)
@Singleton
@FeatureFlag(name="nexus.jwt.enabled")
public class JwtHelper
extends StateGuardLifecycleSupport
implements EventAware {
    public static final String JWT_COOKIE_NAME = "NXSESSIONID";
    public static final String ISSUER = "sonatype";
    public static final String REALM = "realm";
    public static final String USER = "user";
    public static final String USER_SESSION_ID = "userSessionId";
    private final int expirySeconds;
    private final String contextPath;
    private final Provider<SecretStore> secretStoreProvider;
    private final boolean cookieSecure;
    private JwtVerifier verifier;

    public JwtHelper(int expirySeconds, String contextPath, Provider<SecretStore> secretStoreProvider) {
        this(expirySeconds, contextPath, secretStoreProvider, true);
    }

    @Inject
    public JwtHelper(@Named(value="${nexus.jwt.expiry:-1800}") int expirySeconds, @Named(value="${nexus-context-path}") String contextPath, Provider<SecretStore> secretStoreProvider, @Named(value="${nexus.session.secureCookie:-true}") boolean cookieSecure) {
        Preconditions.checkState((expirySeconds >= 0 ? 1 : 0) != 0, (Object)"JWT expiration period should be positive");
        this.expirySeconds = expirySeconds;
        this.contextPath = (String)Preconditions.checkNotNull((Object)contextPath);
        this.secretStoreProvider = (Provider)Preconditions.checkNotNull(secretStoreProvider);
        this.cookieSecure = cookieSecure;
    }

    protected void doStart() throws Exception {
        SecretStore store = (SecretStore)this.secretStoreProvider.get();
        if (!store.getSecret().isPresent()) {
            store.generateNewSecret();
        }
        this.verifier = new JwtVerifier(this.loadSecret());
    }

    public Cookie createJwtCookie(Subject subject, boolean secureRequest) {
        Preconditions.checkNotNull((Object)subject);
        String username = subject.getPrincipal().toString();
        Optional realm = subject.getPrincipals().getRealmNames().stream().findFirst();
        return this.createJwtCookie(username, realm.orElse(null), secureRequest);
    }

    public Cookie verifyAndRefreshJwtCookie(String jwt, boolean secureRequest) throws JwtVerificationException {
        Preconditions.checkNotNull((Object)jwt);
        DecodedJWT decoded = this.verifyJwt(jwt);
        return this.createJwtCookie(decoded.getClaim(USER).asString(), decoded.getClaim(REALM).asString(), decoded.getClaim(USER_SESSION_ID).asString(), secureRequest);
    }

    public DecodedJWT verifyJwt(String jwt) throws JwtVerificationException {
        return this.verifier.verify(jwt);
    }

    public int getExpirySeconds() {
        return this.expirySeconds;
    }

    @Subscribe
    public void on(JwtSecretChanged event) {
        this.log.debug("JWT secret has changed. Reset the cookies");
        this.verifier = new JwtVerifier(this.loadSecret());
    }

    private Cookie createJwtCookie(String user, String realm, boolean secureRequest) {
        String userSessionId = UUID.randomUUID().toString();
        return this.createJwtCookie(user, realm, userSessionId, secureRequest);
    }

    private Cookie createJwtCookie(String user, String realm, String userSessionId, boolean secureRequest) {
        String jwt = this.createToken(user, realm, userSessionId);
        return this.createCookie(jwt, secureRequest);
    }

    private String createToken(String user, String realm, String userSessionId) {
        Date issuedAt = new Date();
        Date expiresAt = this.getExpiresAt(issuedAt);
        return JWT.create().withIssuer(ISSUER).withClaim(USER, user).withClaim(REALM, realm).withClaim(USER_SESSION_ID, userSessionId).withIssuedAt(issuedAt).withExpiresAt(expiresAt).sign(this.verifier.getAlgorithm());
    }

    private Cookie createCookie(String jwt, boolean secureRequest) {
        Cookie cookie = new Cookie(JWT_COOKIE_NAME, jwt);
        cookie.setMaxAge(this.expirySeconds);
        cookie.setPath(this.contextPath);
        cookie.setHttpOnly(true);
        cookie.setSecure(this.cookieSecure && secureRequest);
        return cookie;
    }

    private Date getExpiresAt(Date issuedAt) {
        return new Date(issuedAt.getTime() + TimeUnit.SECONDS.toMillis(this.expirySeconds));
    }

    private String loadSecret() {
        return ((SecretStore)this.secretStoreProvider.get()).getSecret().orElseThrow(() -> new IllegalStateException("JWT secret not found in datastore"));
    }
}

