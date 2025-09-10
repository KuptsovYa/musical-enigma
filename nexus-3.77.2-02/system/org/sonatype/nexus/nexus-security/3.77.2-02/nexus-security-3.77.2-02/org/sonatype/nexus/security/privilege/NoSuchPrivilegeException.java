/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.privilege;

import org.sonatype.nexus.security.privilege.PrivilegeException;

public class NoSuchPrivilegeException
extends PrivilegeException {
    private static final long serialVersionUID = 820651866330926246L;
    private String privilegeId;

    public NoSuchPrivilegeException(String privilegeId) {
        super("Privilege not found: " + privilegeId);
        this.privilegeId = privilegeId;
    }

    public String getPrivilegeId() {
        return this.privilegeId;
    }
}

