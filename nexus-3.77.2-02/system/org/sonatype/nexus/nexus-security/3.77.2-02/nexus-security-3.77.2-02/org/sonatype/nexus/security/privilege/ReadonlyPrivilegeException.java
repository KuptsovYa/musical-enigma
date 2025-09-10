/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.privilege;

import org.sonatype.nexus.security.privilege.PrivilegeException;

public class ReadonlyPrivilegeException
extends PrivilegeException {
    private static final String ERROR_TEXT = "Privilege %s is read only.";

    public ReadonlyPrivilegeException(String privilegeId) {
        super(String.format(ERROR_TEXT, privilegeId));
    }

    public ReadonlyPrivilegeException(String privilegeId, Exception cause) {
        super(String.format(ERROR_TEXT, privilegeId), cause);
    }
}

