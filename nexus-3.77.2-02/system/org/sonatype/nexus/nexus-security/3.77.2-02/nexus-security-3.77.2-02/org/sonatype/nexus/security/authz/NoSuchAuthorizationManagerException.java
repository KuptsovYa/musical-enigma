/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.authz;

public class NoSuchAuthorizationManagerException
extends Exception {
    private static final long serialVersionUID = -9130834235862218360L;

    public NoSuchAuthorizationManagerException(String source) {
        super("Authorization-manager with source '" + source + "' could not be found");
    }
}

