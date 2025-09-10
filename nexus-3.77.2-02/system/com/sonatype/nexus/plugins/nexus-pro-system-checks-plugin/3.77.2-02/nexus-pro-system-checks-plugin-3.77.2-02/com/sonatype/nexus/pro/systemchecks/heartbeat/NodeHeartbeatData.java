/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.google.common.base.Preconditions
 *  org.sonatype.nexus.node.datastore.NodeHeartbeat
 */
package com.sonatype.nexus.pro.systemchecks.heartbeat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import org.sonatype.nexus.node.datastore.NodeHeartbeat;

public class NodeHeartbeatData
implements NodeHeartbeat,
Serializable {
    private static final long serialVersionUID = 1225174334027832674L;
    @JsonIgnore
    Integer id;
    @JsonProperty
    private String heartbeatId;
    @JsonProperty
    private String hostname;
    @JsonProperty
    private OffsetDateTime heartbeatTime;
    @JsonProperty
    private Map<String, Object> nodeInfo = new HashMap<String, Object>();
    @JsonProperty
    private Map<String, Object> systemInfo = new HashMap<String, Object>();

    private NodeHeartbeatData() {
    }

    public NodeHeartbeatData(String heartbeatId, String hostname) {
        this.heartbeatId = (String)Preconditions.checkNotNull((Object)heartbeatId);
        this.hostname = (String)Preconditions.checkNotNull((Object)hostname);
    }

    public NodeHeartbeatData(String heartbeatId, String hostname, Map<String, Object> nodeInfo, Map<String, Object> systemInfo) {
        this.heartbeatId = (String)Preconditions.checkNotNull((Object)heartbeatId);
        this.hostname = (String)Preconditions.checkNotNull((Object)hostname);
        this.nodeInfo = (Map)Preconditions.checkNotNull(nodeInfo);
        this.systemInfo = (Map)Preconditions.checkNotNull(systemInfo);
    }

    public OffsetDateTime heartbeatTime() {
        return this.heartbeatTime;
    }

    public String hostname() {
        return this.hostname;
    }

    public Integer id() {
        return this.id;
    }

    public Map<String, Object> nodeInfo() {
        return this.nodeInfo;
    }

    public Map<String, Object> systemInfo() {
        return this.systemInfo;
    }

    public String heartbeatId() {
        return this.heartbeatId;
    }

    public void setHeartbeatTime(OffsetDateTime heartbeatTime) {
        this.heartbeatTime = heartbeatTime;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setHeartbeatId(String heartbeatId) {
        this.heartbeatId = heartbeatId;
    }

    public void setNodeInfo(Map<String, Object> nodeInfo) {
        this.nodeInfo = nodeInfo;
    }

    public void setSystemInfo(Map<String, Object> systemInfo) {
        this.systemInfo = systemInfo;
    }

    public String nextContinuationToken() {
        return Integer.toString(this.id);
    }
}

