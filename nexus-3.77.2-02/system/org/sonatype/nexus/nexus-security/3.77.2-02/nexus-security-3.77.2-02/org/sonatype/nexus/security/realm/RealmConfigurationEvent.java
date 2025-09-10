/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.realm;

import org.sonatype.nexus.security.realm.RealmConfiguration;

public interface RealmConfigurationEvent {
    public boolean isLocal();

    public RealmConfiguration getConfiguration();
}

