/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.realm;

import org.sonatype.nexus.security.realm.RealmConfiguration;

public class RealmConfigurationChangedEvent {
    private final RealmConfiguration configuration;

    public RealmConfigurationChangedEvent(RealmConfiguration configuration) {
        this.configuration = configuration;
    }

    public RealmConfiguration getConfiguration() {
        return this.configuration;
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{configuration=" + this.configuration + "}";
    }
}

