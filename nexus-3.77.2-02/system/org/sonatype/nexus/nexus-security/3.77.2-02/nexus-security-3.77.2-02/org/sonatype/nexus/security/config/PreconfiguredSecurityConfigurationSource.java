/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.config;

import org.sonatype.nexus.security.config.SecurityConfiguration;
import org.sonatype.nexus.security.config.SecurityConfigurationSource;

public class PreconfiguredSecurityConfigurationSource
implements SecurityConfigurationSource {
    private final SecurityConfiguration configuration;

    public PreconfiguredSecurityConfigurationSource(SecurityConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SecurityConfiguration getConfiguration() {
        return this.configuration;
    }

    @Override
    public SecurityConfiguration loadConfiguration() {
        return this.getConfiguration();
    }
}

