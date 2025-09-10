/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.shiro.authc.HostAuthenticationToken
 */
package org.sonatype.nexus.security.authc;

import org.apache.shiro.authc.HostAuthenticationToken;

public class NexusApiKeyAuthenticationToken
implements HostAuthenticationToken {
    private Object principal;
    private final char[] credentials;
    private final String host;

    public NexusApiKeyAuthenticationToken(Object principal, char[] credentials, String host) {
        this.principal = principal;
        this.credentials = credentials;
        this.host = host;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public Object getCredentials() {
        return this.credentials;
    }

    public String getHost() {
        return this.host;
    }

    public void setPrincipal(Object principal) {
        this.principal = principal;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder(this.getClass().getName());
        buf.append(" - ").append(this.getPrincipal());
        if (this.host != null) {
            buf.append(" (").append(this.host).append(")");
        }
        return buf.toString();
    }
}

