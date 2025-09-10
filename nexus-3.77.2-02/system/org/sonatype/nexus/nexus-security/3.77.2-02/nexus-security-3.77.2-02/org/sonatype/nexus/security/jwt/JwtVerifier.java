/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.auth0.jwt.JWT
 *  com.auth0.jwt.JWTVerifier
 *  com.auth0.jwt.algorithms.Algorithm
 *  com.auth0.jwt.exceptions.JWTVerificationException
 *  com.auth0.jwt.interfaces.DecodedJWT
 *  org.sonatype.goodies.common.ComponentSupport
 */
package org.sonatype.nexus.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.security.jwt.JwtVerificationException;

public class JwtVerifier
extends ComponentSupport {
    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final String secret;

    public JwtVerifier(String secret) {
        this.secret = secret;
        this.algorithm = Algorithm.HMAC256((String)secret);
        this.verifier = JWT.require((Algorithm)this.algorithm).withIssuer(new String[]{"sonatype"}).build();
    }

    public DecodedJWT verify(String jwt) throws JwtVerificationException {
        DecodedJWT decodedJWT;
        try {
            decodedJWT = this.verifier.verify(jwt);
        }
        catch (JWTVerificationException e) {
            String errorMsg = "Can't verify the token";
            this.log.debug(errorMsg, (Throwable)e);
            throw new JwtVerificationException(errorMsg);
        }
        return decodedJWT;
    }

    public Algorithm getAlgorithm() {
        return this.algorithm;
    }

    public String getSecret() {
        return this.secret;
    }
}

