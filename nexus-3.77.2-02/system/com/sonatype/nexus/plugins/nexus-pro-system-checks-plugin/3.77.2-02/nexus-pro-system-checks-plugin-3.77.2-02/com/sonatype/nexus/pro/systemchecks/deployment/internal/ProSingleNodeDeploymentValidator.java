/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.node.NodeAccess
 *  org.sonatype.nexus.node.datastore.NodeHeartbeat
 *  org.sonatype.nexus.upgrade.datastore.DeploymentValidator
 *  org.sonatype.nexus.upgrade.datastore.UpgradeManager
 */
package com.sonatype.nexus.pro.systemchecks.deployment.internal;

import com.google.common.base.Preconditions;
import com.sonatype.nexus.pro.systemchecks.heartbeat.NodeHeartbeatStore;
import com.sonatype.nexus.pro.systemchecks.heartbeat.upgrade.NodeHeartbeatDatabaseMigrationStep;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.node.NodeAccess;
import org.sonatype.nexus.node.datastore.NodeHeartbeat;
import org.sonatype.nexus.upgrade.datastore.DeploymentValidator;
import org.sonatype.nexus.upgrade.datastore.UpgradeManager;

@Named
public class ProSingleNodeDeploymentValidator
extends ComponentSupport
implements DeploymentValidator {
    private final NodeAccess nodeAccess;
    private final NodeHeartbeatStore nodeHeartbeatStore;
    private final UpgradeManager upgradeManager;

    @Inject
    public ProSingleNodeDeploymentValidator(NodeAccess nodeAccess, NodeHeartbeatStore nodeHeartbeatStore, UpgradeManager upgradeManager) {
        this.nodeAccess = (NodeAccess)Preconditions.checkNotNull((Object)nodeAccess);
        this.nodeHeartbeatStore = (NodeHeartbeatStore)Preconditions.checkNotNull((Object)nodeHeartbeatStore);
        this.upgradeManager = (UpgradeManager)Preconditions.checkNotNull((Object)upgradeManager);
    }

    public void validate() {
        if (this.requiredMigrationApplied()) {
            List otherNodeHeartbeats = this.getValidRecentHeartBeats().stream().filter(hb -> !hb.nodeInfo().get("nodeId").equals(this.nodeAccess.getId())).collect(Collectors.toList());
            Preconditions.checkState((boolean)otherNodeHeartbeats.isEmpty(), (Object)"unable to perform standalone deployment, recent heartbeats found from different nodes");
        }
    }

    private List<NodeHeartbeat> getValidRecentHeartBeats() {
        return this.getRecentHeartbeats().stream().filter(heartbeat -> !heartbeat.nodeInfo().isEmpty() && heartbeat.nodeInfo().containsKey("nodeId")).collect(Collectors.toList());
    }

    private List<NodeHeartbeat> getRecentHeartbeats() {
        return this.nodeHeartbeatStore.getActiveNodes();
    }

    private boolean requiredMigrationApplied() {
        return this.upgradeManager.isMigrationApplied(NodeHeartbeatDatabaseMigrationStep.class);
    }
}

