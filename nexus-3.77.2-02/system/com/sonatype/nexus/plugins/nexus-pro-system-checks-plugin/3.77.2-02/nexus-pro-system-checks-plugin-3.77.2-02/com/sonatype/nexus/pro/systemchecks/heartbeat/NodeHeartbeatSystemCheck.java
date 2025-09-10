/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.health.HealthCheck
 *  com.codahale.metrics.health.HealthCheck$Result
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Provider
 *  javax.inject.Singleton
 *  org.sonatype.nexus.node.datastore.NodeHeartbeatManager
 */
package com.sonatype.nexus.pro.systemchecks.heartbeat;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.Preconditions;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import org.sonatype.nexus.node.datastore.NodeHeartbeatManager;

@Named(value="Node Health Check")
@Singleton
public class NodeHeartbeatSystemCheck
extends HealthCheck {
    private final Provider<NodeHeartbeatManager> manager;

    @Inject
    public NodeHeartbeatSystemCheck(Provider<NodeHeartbeatManager> manager) {
        this.manager = (Provider)Preconditions.checkNotNull(manager);
    }

    protected HealthCheck.Result check() {
        if (((NodeHeartbeatManager)this.manager.get()).isValidNodeDeployment()) {
            return ((NodeHeartbeatManager)this.manager.get()).isCurrentNodeClustered() ? HealthCheck.Result.healthy((String)"Only clustered nodes are connected to your database.") : HealthCheck.Result.healthy((String)"Only a single non-clustered node is connected to your database.");
        }
        return HealthCheck.Result.unhealthy((String)"There are multiple non-clustered or a mixture of clustered and non-clustered nodes connected to a single database. This is unsupported and will lead to errors.");
    }
}

