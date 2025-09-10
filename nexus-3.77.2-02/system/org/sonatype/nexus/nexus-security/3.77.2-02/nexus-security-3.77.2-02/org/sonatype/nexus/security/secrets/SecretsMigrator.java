/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.sonatype.nexus.crypto.secrets.Secret
 */
package org.sonatype.nexus.security.secrets;

import org.sonatype.nexus.crypto.secrets.Secret;
import org.sonatype.nexus.security.secrets.SecretMigrationException;

public interface SecretsMigrator {
    public void migrate() throws SecretMigrationException;

    default public boolean isLegacyEncryptedString(Secret secret) {
        return !this.isPersistedSecret(secret);
    }

    default public boolean isPersistedSecret(Secret secret) {
        return secret.getId().startsWith("_");
    }
}

