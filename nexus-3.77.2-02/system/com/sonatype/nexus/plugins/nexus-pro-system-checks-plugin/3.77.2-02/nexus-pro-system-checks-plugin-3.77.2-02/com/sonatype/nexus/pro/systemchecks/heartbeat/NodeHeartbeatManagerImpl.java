/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.health.HealthCheck$Result
 *  com.codahale.metrics.health.HealthCheckRegistry
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.common.base.Preconditions
 *  com.google.common.cache.CacheBuilder
 *  com.google.common.cache.CacheLoader
 *  com.google.common.cache.LoadingCache
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.Iterables
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.goodies.lifecycle.LifecycleSupport
 *  org.sonatype.nexus.common.app.ApplicationVersion
 *  org.sonatype.nexus.common.app.ManagedLifecycle
 *  org.sonatype.nexus.common.app.ManagedLifecycle$Phase
 *  org.sonatype.nexus.common.atlas.SystemInformationManager
 *  org.sonatype.nexus.common.event.EventAware
 *  org.sonatype.nexus.common.node.NodeAccess
 *  org.sonatype.nexus.common.scheduling.PeriodicJobService
 *  org.sonatype.nexus.common.scheduling.PeriodicJobService$PeriodicJob
 *  org.sonatype.nexus.node.datastore.NodeHeartbeat
 *  org.sonatype.nexus.node.datastore.NodeHeartbeatManager
 *  org.sonatype.nexus.systemchecks.NodeSystemCheckResult
 */
package com.sonatype.nexus.pro.systemchecks.heartbeat;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.sonatype.nexus.pro.systemchecks.heartbeat.NodeHeartbeatStore;
import com.sonatype.nexus.pro.systemchecks.heartbeat.ResultUtil;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.lifecycle.LifecycleSupport;
import org.sonatype.nexus.common.app.ApplicationVersion;
import org.sonatype.nexus.common.app.ManagedLifecycle;
import org.sonatype.nexus.common.atlas.SystemInformationManager;
import org.sonatype.nexus.common.event.EventAware;
import org.sonatype.nexus.common.node.NodeAccess;
import org.sonatype.nexus.common.scheduling.PeriodicJobService;
import org.sonatype.nexus.node.datastore.NodeHeartbeat;
import org.sonatype.nexus.node.datastore.NodeHeartbeatManager;
import org.sonatype.nexus.systemchecks.NodeSystemCheckResult;

@ManagedLifecycle(phase=ManagedLifecycle.Phase.SERVICES)
@Named
@Singleton
public class NodeHeartbeatManagerImpl
extends LifecycleSupport
implements NodeHeartbeatManager,
EventAware {
    private static final String HEALTH_CHECK = "healthCheck";
    private final PeriodicJobService periodicJobService;
    private final NodeHeartbeatStore store;
    private final boolean enabled;
    private final int updateIntervalSec;
    private final int clearIntervalDays;
    private final int initialRecordDelaySec;
    private final Map<String, Object> nodeInfo;
    private final NodeAccess nodeAccess;
    private String heartbeatId;
    private String hostname;
    private PeriodicJobService.PeriodicJob eventsTask;
    private PeriodicJobService.PeriodicJob clearTask;
    private final boolean localIsClustered;
    private final HealthCheckRegistry registry;
    private final SystemInformationManager systemInformationManager;
    private final LoadingCache<String, Collection<NodeHeartbeat>> cache;
    private boolean skipLifeCyclePhaseHealthCheck = true;

    @Inject
    public NodeHeartbeatManagerImpl(NodeHeartbeatStore store, PeriodicJobService periodicJobService, ApplicationVersion applicationVersion, NodeAccess nodeAccess, HealthCheckRegistry registry, SystemInformationManager systemInformationManager, @Named(value="${nexus.heartbeat.enabled:-true}") @Named(value="${nexus.heartbeat.enabled:-true}") boolean enabled, @Named(value="${nexus.heartbeat.interval:-600}") @Named(value="${nexus.heartbeat.interval:-600}") int updateIntervalSec, @Named(value="${nexus.heartbeat.history:-30}") @Named(value="${nexus.heartbeat.history:-30}") int clearIntervalDays, @Named(value="${nexus.heartbeat.initial.delay:-5}") @Named(value="${nexus.heartbeat.initial.delay:-5}") int initialRecordDelaySec, @Named(value="${nexus.healthcheck.refreshInterval:-15}") @Named(value="${nexus.healthcheck.refreshInterval:-15}") int healthCheckRefreshInterval) {
        this.store = (NodeHeartbeatStore)Preconditions.checkNotNull((Object)store);
        this.periodicJobService = (PeriodicJobService)Preconditions.checkNotNull((Object)periodicJobService);
        this.nodeAccess = (NodeAccess)Preconditions.checkNotNull((Object)nodeAccess);
        this.localIsClustered = nodeAccess.isClustered();
        this.enabled = enabled;
        this.registry = (HealthCheckRegistry)Preconditions.checkNotNull((Object)registry);
        this.systemInformationManager = (SystemInformationManager)Preconditions.checkNotNull((Object)systemInformationManager);
        Preconditions.checkState((updateIntervalSec >= 0 ? 1 : 0) != 0, (Object)"Update interval should be positive");
        Preconditions.checkState((clearIntervalDays >= 0 ? 1 : 0) != 0, (Object)"Clear time interval should be positive");
        Preconditions.checkState((initialRecordDelaySec >= 0 ? 1 : 0) != 0, (Object)"Initial record delay should be positive");
        this.updateIntervalSec = updateIntervalSec;
        this.clearIntervalDays = clearIntervalDays;
        this.initialRecordDelaySec = initialRecordDelaySec;
        this.nodeInfo = ImmutableMap.of((Object)"version", (Object)applicationVersion.getVersion(), (Object)"clustered", (Object)nodeAccess.isClustered(), (Object)"nodeId", (Object)nodeAccess.getId());
        this.cache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofSeconds(healthCheckRefreshInterval)).build(this.cacheLoader());
    }

    protected void doStart() throws Exception {
        if (!this.enabled) {
            return;
        }
        this.heartbeatId = UUID.randomUUID().toString();
        this.nodeAccess.getHostName().thenAccept(val -> {
            String string = this.hostname = val;
        }).exceptionally(ex -> {
            this.hostname = this.nodeAccess.getId();
            return null;
        });
        this.log.debug("Starting node events logging with time interval {} sec and cleanup out of {} days records", (Object)this.updateIntervalSec, (Object)this.clearIntervalDays);
        this.periodicJobService.startUsing();
        this.eventsTask = this.periodicJobService.schedule(this::doHeartbeat, Duration.ofSeconds(this.initialRecordDelaySec), Duration.ofSeconds(this.updateIntervalSec));
        this.clearTask = this.periodicJobService.schedule(this::clearStale, 43200);
    }

    protected void doStop() throws Exception {
        if (!this.enabled) {
            return;
        }
        this.eventsTask.cancel();
        this.clearTask.cancel();
        this.periodicJobService.stopUsing();
        this.store.removeLastHeartbeat(this.heartbeatId);
    }

    public boolean isValidNodeDeployment() {
        boolean hasInvalidActiveNodes = this.activeNodes().stream().anyMatch(heartbeat -> {
            boolean isDifferentNode = !this.nodeInfo.get("nodeId").equals(heartbeat.nodeInfo().get("nodeId"));
            boolean isClustered = Boolean.TRUE.equals(heartbeat.nodeInfo().get("clustered"));
            if (!this.localIsClustered && isDifferentNode) {
                return true;
            }
            return this.localIsClustered ^ isClustered;
        });
        if (hasInvalidActiveNodes) {
            this.log.warn("found invalid active nodes on node deployment , isClustered : {}", (Object)this.localIsClustered);
        }
        return !hasInvalidActiveNodes;
    }

    public Stream<NodeSystemCheckResult> getSystemChecks() {
        return this.activeNodes().stream().map(node -> {
            Map<String, HealthCheck.Result> results = NodeHeartbeatManagerImpl.deserializeHealthCheck(node);
            if (!results.isEmpty()) {
                String nodeId = Optional.ofNullable(node.nodeInfo()).map(nodeInfo -> nodeInfo.get("nodeId")).filter(String.class::isInstance).map(String.class::cast).orElse(null);
                return new NodeSystemCheckResult(nodeId, node.hostname(), results);
            }
            return null;
        }).filter(Objects::nonNull);
    }

    public boolean isCurrentNodeClustered() {
        return this.localIsClustered;
    }

    private void doHeartbeat() {
        this.writeHeartbeat();
    }

    private void clearStale() {
        OffsetDateTime cutoff = OffsetDateTime.now().minusDays(this.clearIntervalDays);
        this.log.debug("Removing stale heartbeats before {}", (Object)cutoff);
        this.store.removeStale(cutoff);
    }

    public void writeHeartbeat() {
        this.log.debug("Write heartbeat id: {} to the DB", (Object)this.heartbeatId);
        HashMap<String, Object> nodeInfoWithHealthCheck = new HashMap<String, Object>(this.nodeInfo);
        nodeInfoWithHealthCheck.put(HEALTH_CHECK, this.runHealthChecks());
        this.store.create(this.heartbeatId, this.hostname, nodeInfoWithHealthCheck, this.systemInformationManager.getSystemInfo());
    }

    @VisibleForTesting
    protected SortedMap<String, HealthCheck.Result> runHealthChecks() {
        if (this.skipLifeCyclePhaseHealthCheck) {
            this.skipLifeCyclePhaseHealthCheck = false;
            return this.registry.runHealthChecks((name, healthCheck) -> !"Lifecycle Phase".equals(name));
        }
        return this.registry.runHealthChecks();
    }

    public Map<String, Map<String, Object>> getSystemInformationForNodes() {
        Collection<NodeHeartbeat> activeNodes = this.activeNodes();
        Set categories = ((NodeHeartbeat)Iterables.getFirst(activeNodes, null)).systemInfo().keySet();
        return categories.stream().collect(Collectors.toMap(Function.identity(), category -> activeNodes.stream().collect(Collectors.toMap(node -> String.valueOf(node.nodeInfo().get("nodeId")), node -> node.systemInfo().get(category)))));
    }

    private Collection<NodeHeartbeat> activeNodes() {
        return (Collection)this.cache.getUnchecked((Object)"nodes");
    }

    private CacheLoader<String, Collection<NodeHeartbeat>> cacheLoader() {
        return new CacheLoader<String, Collection<NodeHeartbeat>>(){

            public Collection<NodeHeartbeat> load(String key) throws Exception {
                return NodeHeartbeatManagerImpl.this.getNodeHeartbeatData();
            }
        };
    }

    public Collection<NodeHeartbeat> getActiveNodeHeartbeatData() {
        return this.getNodeHeartbeatData();
    }

    private Collection<NodeHeartbeat> getNodeHeartbeatData() {
        HashMap data = new HashMap();
        this.store.getActiveNodes().stream().sorted(Comparator.comparing(NodeHeartbeat::heartbeatTime).reversed()).filter(heartbeat -> !data.containsKey(heartbeat.heartbeatId())).forEach(heartbeat -> {
            NodeHeartbeat nodeHeartbeat = data.put(heartbeat.heartbeatId(), heartbeat);
        });
        return data.values();
    }

    private static Map<String, HealthCheck.Result> deserializeHealthCheck(NodeHeartbeat node) {
        return Optional.ofNullable(node.nodeInfo()).map(nodeInfo -> nodeInfo.get(HEALTH_CHECK)).filter(Map.class::isInstance).map(check -> (Map)check).orElseGet(Collections::emptyMap).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> ResultUtil.deserialize((Map)entry.getValue())));
    }
}

