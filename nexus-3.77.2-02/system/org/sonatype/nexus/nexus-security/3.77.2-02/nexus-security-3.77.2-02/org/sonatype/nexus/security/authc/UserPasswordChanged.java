/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.authc;

public class UserPasswordChanged {
    private final String userId;
    private final boolean clearCache;

    public UserPasswordChanged(String userId) {
        this(userId, true);
    }

    public UserPasswordChanged(String userId, boolean clearCache) {
        this.userId = userId;
        this.clearCache = clearCache;
    }

    public String getUserId() {
        return this.userId;
    }

    public boolean isClearCache() {
        return this.clearCache;
    }
}

