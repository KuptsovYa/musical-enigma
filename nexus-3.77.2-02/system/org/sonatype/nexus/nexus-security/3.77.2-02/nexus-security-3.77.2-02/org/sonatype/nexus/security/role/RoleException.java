/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.role;

public class RoleException
extends RuntimeException {
    private final String roleId;

    public RoleException(String message, String roleId) {
        super(String.format(message, roleId));
        this.roleId = roleId;
    }

    public RoleException(String message, String roleId, Throwable cause) {
        super(String.format(message, roleId), cause);
        this.roleId = roleId;
    }

    public String getRoleId() {
        return this.roleId;
    }
}

