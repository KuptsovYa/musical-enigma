/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.user;

public class InvalidCredentialsException
extends Exception {
    private static final long serialVersionUID = 294536984704055394L;

    public InvalidCredentialsException() {
        super("Invalid credentials");
    }
}

