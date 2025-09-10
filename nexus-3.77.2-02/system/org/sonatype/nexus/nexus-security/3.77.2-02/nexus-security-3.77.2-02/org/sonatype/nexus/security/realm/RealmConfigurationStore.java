/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package org.sonatype.nexus.security.realm;

import javax.annotation.Nullable;
import org.sonatype.nexus.security.realm.RealmConfiguration;

public interface RealmConfigurationStore {
    public RealmConfiguration newEntity();

    @Nullable
    public RealmConfiguration load();

    public void save(RealmConfiguration var1);
}

