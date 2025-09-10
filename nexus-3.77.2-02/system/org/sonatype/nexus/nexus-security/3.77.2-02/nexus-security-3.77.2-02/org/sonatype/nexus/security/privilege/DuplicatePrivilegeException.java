/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.privilege;

import org.sonatype.nexus.security.privilege.PrivilegeException;

public class DuplicatePrivilegeException
extends PrivilegeException {
    private static final String ERROR_TEXT = "Privilege %s already exists.";

    public DuplicatePrivilegeException(String privilegeId) {
        super(String.format(ERROR_TEXT, privilegeId));
    }

    public DuplicatePrivilegeException(String privilegeId, Exception cause) {
        super(String.format(ERROR_TEXT, privilegeId), cause);
    }
}

