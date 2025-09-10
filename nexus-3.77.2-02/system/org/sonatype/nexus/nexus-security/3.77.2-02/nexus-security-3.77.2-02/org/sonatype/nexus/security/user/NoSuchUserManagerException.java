/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.user;

public class NoSuchUserManagerException
extends Exception {
    private static final long serialVersionUID = -2561129270233203244L;

    public NoSuchUserManagerException(String source) {
        this("User-manager not found", source);
    }

    public NoSuchUserManagerException(String message, String source) {
        super(message + ": " + source);
    }
}

