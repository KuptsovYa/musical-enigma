/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.annotation.Nullable
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.crypto.secrets.Secret
 *  org.sonatype.nexus.crypto.secrets.SecretsService
 */
package org.sonatype.nexus.security.secrets;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.crypto.secrets.Secret;
import org.sonatype.nexus.crypto.secrets.SecretsService;
import org.sonatype.nexus.security.UserIdHelper;
import org.sonatype.nexus.security.secrets.SecretMigrationException;
import org.sonatype.nexus.security.secrets.SecretsMigrator;

public abstract class SecretsMigratorSupport
extends ComponentSupport
implements SecretsMigrator {
    protected final SecretsService secretsService;

    protected SecretsMigratorSupport(SecretsService secretsService) {
        this.secretsService = (SecretsService)Preconditions.checkNotNull((Object)secretsService);
    }

    protected Secret createSecret(String purpose, Secret oldSecret, @Nullable String context) {
        Secret newSecret;
        char[] pw = oldSecret.decrypt();
        if (Arrays.equals(pw, (newSecret = this.secretsService.encrypt(purpose, pw, UserIdHelper.get())).decrypt())) {
            return newSecret;
        }
        if (context != null) {
            throw new SecretMigrationException("Re-encrypted secret does not match for " + context);
        }
        throw new SecretMigrationException("Re-encrypted secret does not match");
    }

    protected void quietlyRemove(List<Secret> secrets) {
        for (Secret secret : secrets) {
            try {
                this.secretsService.remove(secret);
            }
            catch (Exception e) {
                if (this.isLegacyEncryptedString(secret)) {
                    this.log.error("Failed to cleanup cause {}", (Object)e.getMessage(), (Object)(this.log.isDebugEnabled() ? e : null));
                    continue;
                }
                this.log.error("Failed to cleanup secret {} cause {}", new Object[]{secret.getId(), e.getMessage(), this.log.isDebugEnabled() ? e : null});
            }
        }
    }
}

