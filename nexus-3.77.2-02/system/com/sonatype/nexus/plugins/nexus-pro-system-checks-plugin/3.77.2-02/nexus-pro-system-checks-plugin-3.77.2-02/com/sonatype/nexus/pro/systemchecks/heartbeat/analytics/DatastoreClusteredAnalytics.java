/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.annotation.CachedGauge
 *  com.google.common.base.Preconditions
 *  com.sonatype.analytics.AnalyticsMarker
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.app.FeatureFlag
 */
package com.sonatype.nexus.pro.systemchecks.heartbeat.analytics;

import com.codahale.metrics.annotation.CachedGauge;
import com.google.common.base.Preconditions;
import com.sonatype.analytics.AnalyticsMarker;
import com.sonatype.nexus.pro.systemchecks.heartbeat.analytics.NodeAnalyticsService;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.app.FeatureFlag;

@Named
@Singleton
@FeatureFlag(name="nexus.analytics.enabled", enabledByDefault=true)
public class DatastoreClusteredAnalytics
extends ComponentSupport
implements AnalyticsMarker {
    private final NodeAnalyticsService nodeAnalyticsService;
    private final boolean clustered;

    @Inject
    public DatastoreClusteredAnalytics(NodeAnalyticsService nodeAnalyticsService, @Named(value="${nexus.datastore.clustered.enabled:-false}") @Named(value="${nexus.datastore.clustered.enabled:-false}") boolean clustered) {
        this.nodeAnalyticsService = (NodeAnalyticsService)((Object)Preconditions.checkNotNull((Object)((Object)nodeAnalyticsService)));
        this.clustered = clustered;
    }

    @CachedGauge(name="nexus.analytics.datastore_clustered_mode", absolute=true, timeout=6L, timeoutUnit=TimeUnit.HOURS)
    public boolean getClusteredMode() {
        return this.clustered;
    }

    @CachedGauge(name="nexus.analytics.unique_hostnames_count", absolute=true, timeout=6L, timeoutUnit=TimeUnit.HOURS)
    public long getUniqueHostnamesCount() {
        return this.nodeAnalyticsService.getUniqueHostnamesCount();
    }
}

