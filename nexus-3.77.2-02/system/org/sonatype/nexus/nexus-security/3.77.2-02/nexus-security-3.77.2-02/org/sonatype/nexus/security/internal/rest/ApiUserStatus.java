/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.internal.rest;

import org.sonatype.nexus.security.user.UserStatus;

public enum ApiUserStatus {
    active(UserStatus.active),
    locked(UserStatus.locked),
    disabled(UserStatus.disabled),
    changepassword(UserStatus.changepassword);

    private UserStatus status;

    private ApiUserStatus(UserStatus status) {
        this.status = status;
    }

    UserStatus getStatus() {
        return this.status;
    }

    public static ApiUserStatus convert(UserStatus status) {
        if (status == null) {
            return null;
        }
        return ApiUserStatus.valueOf(status.toString());
    }
}

