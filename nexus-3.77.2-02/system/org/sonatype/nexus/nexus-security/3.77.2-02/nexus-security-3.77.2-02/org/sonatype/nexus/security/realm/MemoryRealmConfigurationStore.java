/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.Lists
 *  javax.annotation.Nullable
 *  javax.annotation.Priority
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.goodies.common.ComponentSupport
 */
package org.sonatype.nexus.security.realm;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.Priority;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.security.realm.RealmConfiguration;
import org.sonatype.nexus.security.realm.RealmConfigurationStore;

@Named(value="memory")
@Singleton
@Priority(value=-2147483648)
@VisibleForTesting
public class MemoryRealmConfigurationStore
extends ComponentSupport
implements RealmConfigurationStore {
    private RealmConfiguration model;

    @Override
    public RealmConfiguration newEntity() {
        return new MemoryRealmConfiguration();
    }

    @Override
    @Nullable
    public synchronized RealmConfiguration load() {
        return this.model;
    }

    @Override
    public synchronized void save(RealmConfiguration configuration) {
        this.model = (RealmConfiguration)Preconditions.checkNotNull((Object)configuration);
    }

    private static class MemoryRealmConfiguration
    implements RealmConfiguration,
    Cloneable {
        private List<String> realmNames;

        MemoryRealmConfiguration() {
        }

        @Override
        public List<String> getRealmNames() {
            return this.realmNames;
        }

        @Override
        public void setRealmNames(@Nullable List<String> realmNames) {
            this.realmNames = realmNames;
        }

        @Override
        public MemoryRealmConfiguration copy() {
            try {
                MemoryRealmConfiguration copy = (MemoryRealmConfiguration)this.clone();
                if (this.realmNames != null) {
                    copy.realmNames = Lists.newArrayList(this.realmNames);
                }
                return copy;
            }
            catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

