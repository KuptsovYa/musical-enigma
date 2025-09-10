/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.role;

import org.sonatype.nexus.security.role.RoleException;

public class DuplicateRoleException
extends RoleException {
    private static final String ERROR_TEXT = "Role %s already exists.";

    public DuplicateRoleException(String roleId, Throwable cause) {
        super(ERROR_TEXT, roleId, cause);
    }

    public DuplicateRoleException(String role) {
        this(role, (Throwable)null);
    }
}

