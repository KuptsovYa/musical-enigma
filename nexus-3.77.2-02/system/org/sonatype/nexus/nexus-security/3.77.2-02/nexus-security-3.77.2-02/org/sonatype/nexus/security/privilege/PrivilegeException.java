/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.privilege;

public class PrivilegeException
extends RuntimeException {
    public PrivilegeException(String message) {
        super(message);
    }

    public PrivilegeException(String message, Exception cause) {
        super(message, cause);
    }
}

