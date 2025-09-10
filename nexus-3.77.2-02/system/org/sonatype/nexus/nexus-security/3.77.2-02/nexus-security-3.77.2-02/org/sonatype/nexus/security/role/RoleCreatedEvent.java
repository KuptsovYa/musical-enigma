/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.role;

import org.sonatype.nexus.security.role.Role;
import org.sonatype.nexus.security.role.RoleEvent;

public class RoleCreatedEvent
extends RoleEvent {
    public RoleCreatedEvent(Role role) {
        super(role);
    }
}

