/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.sonatype.nexus.security.privilege;

import com.google.common.base.Preconditions;
import org.sonatype.nexus.security.privilege.Privilege;

public abstract class PrivilegeEvent {
    private final Privilege privilege;

    public PrivilegeEvent(Privilege privilege) {
        this.privilege = (Privilege)Preconditions.checkNotNull((Object)privilege);
    }

    public Privilege getPrivilege() {
        return this.privilege;
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{privilege=" + this.privilege + "}";
    }
}

