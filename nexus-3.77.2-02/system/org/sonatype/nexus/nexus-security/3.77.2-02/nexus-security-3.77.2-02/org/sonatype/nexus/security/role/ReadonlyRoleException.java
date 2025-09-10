/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.role;

import org.sonatype.nexus.security.role.RoleException;

public class ReadonlyRoleException
extends RoleException {
    private static final String ERROR_TEXT = "Role %s is read only.";

    public ReadonlyRoleException(String roleId, Throwable cause) {
        super(ERROR_TEXT, roleId, cause);
    }

    public ReadonlyRoleException(String role) {
        this(role, (Throwable)null);
    }
}

