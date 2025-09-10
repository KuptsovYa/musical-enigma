/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.user;

public enum UserStatus {
    active(true),
    locked(false),
    disabled(false),
    changepassword(true);

    private boolean isActive;

    private UserStatus(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isActive() {
        return this.isActive;
    }
}

