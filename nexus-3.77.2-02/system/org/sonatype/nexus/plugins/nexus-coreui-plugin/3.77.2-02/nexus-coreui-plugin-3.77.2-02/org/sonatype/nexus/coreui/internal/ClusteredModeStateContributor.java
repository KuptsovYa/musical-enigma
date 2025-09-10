/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.rapture.StateContributor
 */
package org.sonatype.nexus.coreui.internal;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.rapture.StateContributor;

@Named
@Singleton
public class ClusteredModeStateContributor
implements StateContributor {
    private final boolean clusteredModeEnabled;
    private final boolean zeroDowntimeEnabled;

    @Inject
    public ClusteredModeStateContributor(@Named(value="${nexus.datastore.clustered.enabled:-false}") boolean clusteredModeEnabled, @Named(value="${nexus.zero.downtime.enabled:-false}") boolean zeroDowntimeEnabled) {
        this.clusteredModeEnabled = clusteredModeEnabled;
        this.zeroDowntimeEnabled = zeroDowntimeEnabled;
    }

    @Nullable
    public Map<String, Object> getState() {
        return ImmutableMap.of((Object)"nexus.datastore.clustered.enabled", (Object)this.clusteredModeEnabled, (Object)"nexus.zero.downtime.enabled", (Object)this.zeroDowntimeEnabled);
    }
}

