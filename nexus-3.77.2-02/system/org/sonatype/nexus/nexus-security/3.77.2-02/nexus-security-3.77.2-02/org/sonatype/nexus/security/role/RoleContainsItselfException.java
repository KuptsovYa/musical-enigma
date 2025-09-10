/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.role;

import org.sonatype.nexus.security.role.RoleException;

public class RoleContainsItselfException
extends RoleException {
    public RoleContainsItselfException(String roleId, Throwable cause) {
        super("Role %s cannot reference itself.", roleId, cause);
    }

    public RoleContainsItselfException(String role) {
        this(role, (Throwable)null);
    }
}

