/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.Lists
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Provider
 *  javax.inject.Singleton
 */
package org.sonatype.nexus.security.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import org.sonatype.nexus.security.realm.RealmConfiguration;
import org.sonatype.nexus.security.realm.RealmConfigurationStore;

@Named(value="initial")
@Singleton
public class InitialRealmConfigurationProvider
implements Provider<RealmConfiguration> {
    private final RealmConfigurationStore store;

    @Inject
    public InitialRealmConfigurationProvider(RealmConfigurationStore store) {
        this.store = (RealmConfigurationStore)Preconditions.checkNotNull((Object)store);
    }

    public RealmConfiguration get() {
        RealmConfiguration configuration = this.store.newEntity();
        configuration.setRealmNames(Lists.newArrayList((Object[])new String[]{"NexusAuthenticatingRealm", "NexusAuthorizingRealm"}));
        return configuration;
    }
}

