/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.role;

import org.sonatype.nexus.security.role.RoleException;

public class NoSuchRoleException
extends RoleException {
    private static final long serialVersionUID = -3551757972830003397L;

    public NoSuchRoleException(String roleId, Throwable cause) {
        super("Role not found: %s", roleId, cause);
    }

    public NoSuchRoleException(String role) {
        this(role, (Throwable)null);
    }
}

