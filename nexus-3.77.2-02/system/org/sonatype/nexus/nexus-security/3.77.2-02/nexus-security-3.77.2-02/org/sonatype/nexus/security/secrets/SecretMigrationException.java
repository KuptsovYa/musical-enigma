/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.secrets;

public class SecretMigrationException
extends RuntimeException {
    public SecretMigrationException(String message) {
        super(message);
    }

    public SecretMigrationException(String message, Throwable cause) {
        super(message, cause);
    }
}

