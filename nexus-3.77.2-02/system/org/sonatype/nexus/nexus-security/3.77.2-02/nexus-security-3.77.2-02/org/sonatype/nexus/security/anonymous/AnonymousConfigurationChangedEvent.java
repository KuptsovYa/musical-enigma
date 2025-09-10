/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.anonymous;

import org.sonatype.nexus.security.anonymous.AnonymousConfiguration;

public class AnonymousConfigurationChangedEvent {
    private final AnonymousConfiguration configuration;

    public AnonymousConfigurationChangedEvent(AnonymousConfiguration configuration) {
        this.configuration = configuration;
    }

    public AnonymousConfiguration getConfiguration() {
        return this.configuration;
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{configuration=" + this.configuration + "}";
    }
}

