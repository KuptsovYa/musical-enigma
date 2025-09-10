/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.authc;

import java.util.Collections;
import java.util.Set;
import org.sonatype.nexus.security.authc.AuthenticationFailureReason;

public class AuthenticationEvent {
    private final String userId;
    private final boolean successful;
    private final Set<AuthenticationFailureReason> authenticationFailureReasons;

    public AuthenticationEvent(String userId, boolean successful) {
        this(userId, successful, Collections.emptySet());
    }

    public AuthenticationEvent(String userId, boolean successful, Set<AuthenticationFailureReason> authenticationFailureReasons) {
        this.userId = userId;
        this.successful = successful;
        this.authenticationFailureReasons = authenticationFailureReasons;
    }

    public String getUserId() {
        return this.userId;
    }

    public boolean isSuccessful() {
        return this.successful;
    }

    public Set<AuthenticationFailureReason> getAuthenticationFailureReasons() {
        return this.authenticationFailureReasons;
    }

    public String toString() {
        return "AuthenticationEvent{userId='" + this.userId + "', successful=" + this.successful + ", failureReasons=" + this.authenticationFailureReasons + "}";
    }
}

