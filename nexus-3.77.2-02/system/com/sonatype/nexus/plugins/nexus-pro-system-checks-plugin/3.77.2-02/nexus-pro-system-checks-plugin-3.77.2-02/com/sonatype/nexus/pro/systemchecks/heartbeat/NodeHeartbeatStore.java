/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.sonatype.nexus.common.entity.Continuation
 *  org.sonatype.nexus.node.datastore.NodeHeartbeat
 */
package com.sonatype.nexus.pro.systemchecks.heartbeat;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.sonatype.nexus.common.entity.Continuation;
import org.sonatype.nexus.node.datastore.NodeHeartbeat;

public interface NodeHeartbeatStore {
    public void create(String var1, String var2, Map<String, Object> var3, Map<String, Object> var4);

    public Continuation<NodeHeartbeat> browse(@Nullable String var1);

    public List<NodeHeartbeat> getLastTwoHeartbeats();

    public boolean removeStale(OffsetDateTime var1);

    public List<NodeHeartbeat> getActiveNodes();

    public boolean removeLastHeartbeat(String var1);
}

