/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security;

public class UserPrincipalsExpired {
    private final String userId;
    private final String source;

    public UserPrincipalsExpired(String userId, String source) {
        this.userId = userId;
        this.source = source;
    }

    public UserPrincipalsExpired() {
        this(null, null);
    }

    public String getUserId() {
        return this.userId;
    }

    public String getSource() {
        return this.source;
    }
}

