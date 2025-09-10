/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.role;

import org.sonatype.nexus.security.role.Role;
import org.sonatype.nexus.security.role.RoleEvent;

public class RoleDeletedEvent
extends RoleEvent {
    public RoleDeletedEvent(Role role) {
        super(role);
    }
}

