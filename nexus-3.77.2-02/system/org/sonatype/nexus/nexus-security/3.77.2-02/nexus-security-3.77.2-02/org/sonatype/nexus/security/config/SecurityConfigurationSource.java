/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.config;

import org.sonatype.nexus.security.config.SecurityConfiguration;

public interface SecurityConfigurationSource {
    public SecurityConfiguration getConfiguration();

    public SecurityConfiguration loadConfiguration();
}

