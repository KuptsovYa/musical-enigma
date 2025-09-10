/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.jwt;

import java.util.Optional;

public interface SecretStore {
    public Optional<String> getSecret();

    public void setSecret(String var1);

    public void generateNewSecret();
}

