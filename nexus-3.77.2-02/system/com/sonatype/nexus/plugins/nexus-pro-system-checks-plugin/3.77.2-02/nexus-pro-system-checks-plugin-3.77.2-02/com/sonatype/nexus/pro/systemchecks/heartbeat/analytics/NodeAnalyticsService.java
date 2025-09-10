/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.inject.Singleton
 *  javax.inject.Inject
 *  javax.inject.Named
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.node.datastore.NodeHeartbeat
 */
package com.sonatype.nexus.pro.systemchecks.heartbeat.analytics;

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;
import com.sonatype.nexus.pro.systemchecks.heartbeat.NodeHeartbeatStore;
import javax.inject.Inject;
import javax.inject.Named;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.node.datastore.NodeHeartbeat;

@Named
@Singleton
public class NodeAnalyticsService
extends ComponentSupport {
    private final NodeHeartbeatStore nodeHeartbeatStore;

    @Inject
    public NodeAnalyticsService(NodeHeartbeatStore nodeHeartbeatStore) {
        this.nodeHeartbeatStore = (NodeHeartbeatStore)Preconditions.checkNotNull((Object)nodeHeartbeatStore);
    }

    public long getUniqueHostnamesCount() {
        return this.nodeHeartbeatStore.getActiveNodes().stream().map(NodeHeartbeat::hostname).distinct().count();
    }
}

