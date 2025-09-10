/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.user;

public class DuplicateUserException
extends RuntimeException {
    private static final String ERROR_TEXT = "User %s already exists.";

    public DuplicateUserException(String userId, Throwable cause) {
        super(String.format(ERROR_TEXT, userId), cause);
    }

    public DuplicateUserException(String userId) {
        this(userId, null);
    }
}

