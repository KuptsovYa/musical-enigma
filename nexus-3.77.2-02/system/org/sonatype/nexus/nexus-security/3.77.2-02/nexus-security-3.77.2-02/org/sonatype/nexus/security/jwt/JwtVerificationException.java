/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.jwt;

public class JwtVerificationException
extends Exception {
    private static final long serialVersionUID = 8292613157279521051L;

    public JwtVerificationException(String reason) {
        super(reason);
    }
}

