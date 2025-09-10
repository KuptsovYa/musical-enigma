/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.common.entity.Continuation
 *  org.sonatype.nexus.node.datastore.NodeHeartbeat
 *  org.sonatype.nexus.supportzip.ExportConfigData
 *  org.sonatype.nexus.supportzip.datastore.JsonExporter
 */
package com.sonatype.nexus.pro.systemchecks.heartbeat.internal;

import com.google.common.base.Preconditions;
import com.sonatype.nexus.pro.systemchecks.heartbeat.NodeHeartbeatStore;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.common.entity.Continuation;
import org.sonatype.nexus.node.datastore.NodeHeartbeat;
import org.sonatype.nexus.supportzip.ExportConfigData;
import org.sonatype.nexus.supportzip.datastore.JsonExporter;

@Named(value="nodeHeartbeatExport")
@Singleton
public class NodeHeartbeatExport
extends JsonExporter
implements ExportConfigData {
    private final NodeHeartbeatStore store;

    @Inject
    public NodeHeartbeatExport(NodeHeartbeatStore store) {
        this.store = (NodeHeartbeatStore)Preconditions.checkNotNull((Object)store);
    }

    public void export(File file) throws IOException {
        this.log.debug("Export node heartbeats data to {}", (Object)file);
        Continuation<NodeHeartbeat> page = this.store.browse(null);
        ArrayList<NodeHeartbeat> nodeHeartbeats = new ArrayList<NodeHeartbeat>();
        while (!page.isEmpty()) {
            nodeHeartbeats.addAll((Collection<NodeHeartbeat>)page);
            page = this.store.browse(page.nextContinuationToken());
        }
        this.exportToJson(nodeHeartbeats, file);
    }
}

