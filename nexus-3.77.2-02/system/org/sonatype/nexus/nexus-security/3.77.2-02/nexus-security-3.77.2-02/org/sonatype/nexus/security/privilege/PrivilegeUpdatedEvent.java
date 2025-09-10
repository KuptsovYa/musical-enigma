/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.privilege;

import org.sonatype.nexus.security.privilege.Privilege;
import org.sonatype.nexus.security.privilege.PrivilegeEvent;

public class PrivilegeUpdatedEvent
extends PrivilegeEvent {
    public PrivilegeUpdatedEvent(Privilege privilege) {
        super(privilege);
    }
}

