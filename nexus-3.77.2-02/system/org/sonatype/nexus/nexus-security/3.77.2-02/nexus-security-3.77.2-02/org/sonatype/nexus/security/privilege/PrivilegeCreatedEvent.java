/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.privilege;

import org.sonatype.nexus.security.privilege.Privilege;
import org.sonatype.nexus.security.privilege.PrivilegeEvent;

public class PrivilegeCreatedEvent
extends PrivilegeEvent {
    public PrivilegeCreatedEvent(Privilege privilege) {
        super(privilege);
    }
}

