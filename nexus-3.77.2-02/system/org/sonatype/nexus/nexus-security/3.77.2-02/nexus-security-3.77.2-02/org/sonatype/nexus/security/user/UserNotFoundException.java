/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.user;

public class UserNotFoundException
extends Exception {
    private static final long serialVersionUID = -177760017345640029L;

    public UserNotFoundException(String userId, String message, Throwable cause) {
        super("User not found: " + userId + "; " + message, cause);
    }

    public UserNotFoundException(String userId) {
        super("User not found: " + userId);
    }
}

