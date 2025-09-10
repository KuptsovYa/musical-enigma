/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.shiro.authc.AccountException
 */
package org.sonatype.nexus.security.authc;

import java.util.Set;
import org.apache.shiro.authc.AccountException;
import org.sonatype.nexus.security.authc.AuthenticationFailureReason;

public class NexusAuthenticationException
extends AccountException {
    private final Set<AuthenticationFailureReason> authenticationFailureReasons;

    public NexusAuthenticationException(String cause, Set<AuthenticationFailureReason> authenticationFailureReason) {
        super(cause);
        this.authenticationFailureReasons = authenticationFailureReason;
    }

    public Set<AuthenticationFailureReason> getAuthenticationFailureReasons() {
        return this.authenticationFailureReasons;
    }
}

