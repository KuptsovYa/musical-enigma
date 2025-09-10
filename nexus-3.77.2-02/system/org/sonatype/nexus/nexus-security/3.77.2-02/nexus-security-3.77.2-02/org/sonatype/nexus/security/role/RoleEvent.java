/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.sonatype.nexus.security.role;

import com.google.common.base.Preconditions;
import org.sonatype.nexus.security.role.Role;

public abstract class RoleEvent {
    private final Role role;

    public RoleEvent(Role role) {
        this.role = (Role)Preconditions.checkNotNull((Object)role);
    }

    public Role getRole() {
        return this.role;
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{role=" + this.role + "}";
    }
}

