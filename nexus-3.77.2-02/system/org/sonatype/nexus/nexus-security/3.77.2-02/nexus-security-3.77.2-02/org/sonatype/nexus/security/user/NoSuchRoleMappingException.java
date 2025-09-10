/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.user;

public class NoSuchRoleMappingException
extends Exception {
    private static final long serialVersionUID = -8368148376838186349L;

    public NoSuchRoleMappingException(String userId) {
        super("No user-role mapping for user: " + userId);
    }
}

