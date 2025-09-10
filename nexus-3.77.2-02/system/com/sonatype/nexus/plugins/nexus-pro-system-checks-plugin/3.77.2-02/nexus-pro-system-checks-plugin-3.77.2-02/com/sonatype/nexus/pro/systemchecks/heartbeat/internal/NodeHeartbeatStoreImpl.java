/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.common.entity.Continuation
 *  org.sonatype.nexus.common.property.SystemPropertiesHelper
 *  org.sonatype.nexus.datastore.ConfigStoreSupport
 *  org.sonatype.nexus.datastore.api.DataSessionSupplier
 *  org.sonatype.nexus.node.datastore.NodeHeartbeat
 *  org.sonatype.nexus.scheduling.CancelableHelper
 *  org.sonatype.nexus.transaction.Transaction
 *  org.sonatype.nexus.transaction.Transactional
 *  org.sonatype.nexus.transaction.UnitOfWork
 */
package com.sonatype.nexus.pro.systemchecks.heartbeat.internal;

import com.sonatype.nexus.pro.systemchecks.heartbeat.NodeHeartbeatData;
import com.sonatype.nexus.pro.systemchecks.heartbeat.NodeHeartbeatStore;
import com.sonatype.nexus.pro.systemchecks.heartbeat.internal.NodeHeartbeatDAO;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.common.entity.Continuation;
import org.sonatype.nexus.common.property.SystemPropertiesHelper;
import org.sonatype.nexus.datastore.ConfigStoreSupport;
import org.sonatype.nexus.datastore.api.DataSessionSupplier;
import org.sonatype.nexus.node.datastore.NodeHeartbeat;
import org.sonatype.nexus.scheduling.CancelableHelper;
import org.sonatype.nexus.transaction.Transaction;
import org.sonatype.nexus.transaction.Transactional;
import org.sonatype.nexus.transaction.UnitOfWork;

@Named(value="mybatis")
@Singleton
public class NodeHeartbeatStoreImpl
extends ConfigStoreSupport<NodeHeartbeatDAO>
implements NodeHeartbeatStore {
    private static final int DELETE_BATCH_SIZE_DEFAULT = SystemPropertiesHelper.getInteger((String)"nexus.content.deleteBatchSize", (int)1000);
    private final int heartbeatIntervalSeconds;
    private final int intervalDelaySeconds;

    @Inject
    public NodeHeartbeatStoreImpl(DataSessionSupplier sessionSupplier, @Named(value="${nexus.heartbeat.interval:-600}") @Named(value="${nexus.heartbeat.interval:-600}") int heartBeatIntervalSeconds, @Named(value="${nexus.heartbeat.interval.delay:-3}") @Named(value="${nexus.heartbeat.interval.delay:-3}") int intervalDelaySeconds) {
        super(sessionSupplier);
        this.heartbeatIntervalSeconds = heartBeatIntervalSeconds;
        this.intervalDelaySeconds = intervalDelaySeconds;
    }

    @Override
    @Transactional
    public void create(String nodeId, String hostname, Map<String, Object> nodeInfo, Map<String, Object> systemInfo) {
        NodeHeartbeatData nodeEventsData = new NodeHeartbeatData(nodeId, hostname, nodeInfo, systemInfo);
        ((NodeHeartbeatDAO)this.dao()).save(nodeEventsData);
    }

    @Override
    @Transactional
    public Continuation<NodeHeartbeat> browse(@Nullable String continuationToken) {
        return ((NodeHeartbeatDAO)this.dao()).browse(continuationToken);
    }

    @Override
    @Transactional
    public List<NodeHeartbeat> getLastTwoHeartbeats() {
        return ((NodeHeartbeatDAO)this.dao()).getLastTwoHeartbeats();
    }

    @Override
    @Transactional
    public boolean removeStale(OffsetDateTime cutoff) {
        boolean deleted = false;
        while (((NodeHeartbeatDAO)this.dao()).removeStale(cutoff, DELETE_BATCH_SIZE_DEFAULT)) {
            this.commitChangesSoFar();
            deleted = true;
        }
        return deleted;
    }

    @Override
    @Transactional
    public List<NodeHeartbeat> getActiveNodes() {
        int interval = this.heartbeatIntervalSeconds + this.intervalDelaySeconds;
        return ((NodeHeartbeatDAO)this.dao()).getAllActiveNodes(interval);
    }

    @Override
    @Transactional
    public boolean removeLastHeartbeat(String heartbeatId) {
        this.log.debug("Removing very last heartbeat by heartbeatId : {}", (Object)heartbeatId);
        return ((NodeHeartbeatDAO)this.dao()).removeLastHeartbeat(heartbeatId);
    }

    private void commitChangesSoFar() {
        Transaction tx = UnitOfWork.currentTx();
        tx.commit();
        tx.begin();
        CancelableHelper.checkCancellation();
    }
}

