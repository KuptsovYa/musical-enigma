/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.authc;

public abstract class SecurityEvent {
    private final String realm;
    private final String principal;

    protected SecurityEvent(String principal, String realm) {
        this.realm = realm;
        this.principal = principal;
    }

    public String getRealm() {
        return this.realm;
    }

    public String getPrincipal() {
        return this.principal;
    }
}

