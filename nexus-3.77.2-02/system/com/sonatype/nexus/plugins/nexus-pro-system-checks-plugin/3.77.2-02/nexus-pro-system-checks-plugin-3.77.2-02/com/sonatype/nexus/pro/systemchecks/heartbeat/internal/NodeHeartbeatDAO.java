/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.apache.ibatis.annotations.Param
 *  org.sonatype.nexus.common.entity.Continuation
 *  org.sonatype.nexus.datastore.api.FreezeImmuneDAO
 *  org.sonatype.nexus.datastore.api.SingletonDataAccess
 *  org.sonatype.nexus.node.datastore.NodeHeartbeat
 */
package com.sonatype.nexus.pro.systemchecks.heartbeat.internal;

import com.sonatype.nexus.pro.systemchecks.heartbeat.NodeHeartbeatData;
import java.time.OffsetDateTime;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.ibatis.annotations.Param;
import org.sonatype.nexus.common.entity.Continuation;
import org.sonatype.nexus.datastore.api.FreezeImmuneDAO;
import org.sonatype.nexus.datastore.api.SingletonDataAccess;
import org.sonatype.nexus.node.datastore.NodeHeartbeat;

public interface NodeHeartbeatDAO
extends SingletonDataAccess<NodeHeartbeatData>,
FreezeImmuneDAO {
    public void save(NodeHeartbeat var1);

    public Continuation<NodeHeartbeat> browse(@Nullable String var1);

    public List<NodeHeartbeat> getLastTwoHeartbeats();

    public boolean removeStale(@Param(value="cutoff") OffsetDateTime var1, @Param(value="limit") int var2);

    public List<NodeHeartbeat> getAllActiveNodes(@Param(value="secondsSince") int var1);

    public boolean removeLastHeartbeat(@Param(value="heartbeatId") String var1);
}

