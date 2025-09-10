/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.authc;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import org.sonatype.nexus.security.ClientInfo;
import org.sonatype.nexus.security.authc.AuthenticationFailureReason;

public class NexusAuthenticationEvent {
    private final ClientInfo clientInfo;
    private final boolean successful;
    private final Date date;
    private final Set<AuthenticationFailureReason> authenticationFailureReasons;

    public NexusAuthenticationEvent(ClientInfo info, boolean successful) {
        this(info, successful, Collections.emptySet());
    }

    public NexusAuthenticationEvent(ClientInfo info, boolean successful, Set<AuthenticationFailureReason> authenticationFailureReasons) {
        this.clientInfo = info;
        this.successful = successful;
        this.date = new Date();
        this.authenticationFailureReasons = authenticationFailureReasons;
    }

    public ClientInfo getClientInfo() {
        return this.clientInfo;
    }

    public boolean isSuccessful() {
        return this.successful;
    }

    public Date getEventDate() {
        return this.date;
    }

    public Set<AuthenticationFailureReason> getAuthenticationFailureReasons() {
        return this.authenticationFailureReasons;
    }
}

