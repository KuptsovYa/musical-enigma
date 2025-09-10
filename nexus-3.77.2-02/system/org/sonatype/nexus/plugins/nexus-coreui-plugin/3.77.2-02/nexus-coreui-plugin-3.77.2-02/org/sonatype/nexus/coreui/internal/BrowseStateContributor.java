/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.base.Suppliers
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.rapture.StateContributor
 *  org.sonatype.nexus.repository.browse.node.BrowseNodeConfiguration
 *  org.sonatype.nexus.scheduling.TaskInfo
 *  org.sonatype.nexus.scheduling.TaskScheduler
 *  org.sonatype.nexus.scheduling.TaskState
 */
package org.sonatype.nexus.coreui.internal;

import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.rapture.StateContributor;
import org.sonatype.nexus.repository.browse.node.BrowseNodeConfiguration;
import org.sonatype.nexus.scheduling.TaskInfo;
import org.sonatype.nexus.scheduling.TaskScheduler;
import org.sonatype.nexus.scheduling.TaskState;

@Singleton
@Named
public class BrowseStateContributor
implements StateContributor {
    private final BrowseNodeConfiguration browseNodeConfiguration;
    private final TaskScheduler taskScheduler;
    private final Supplier<Set<String>> rebuildingRepositoriesCache;
    private static final long DEFAULT_REBUILDING_REPOSITORIES_CACHE_TTL = 60L;

    @Inject
    public BrowseStateContributor(BrowseNodeConfiguration browseNodeConfiguration, TaskScheduler taskScheduler, @Named(value="${nexus.coreui.state.rebuildingRepositoryTasksCacheTTL:-60}") long rebuildingRepositoriesCacheTTL) {
        this.browseNodeConfiguration = (BrowseNodeConfiguration)Preconditions.checkNotNull((Object)browseNodeConfiguration);
        this.taskScheduler = (TaskScheduler)Preconditions.checkNotNull((Object)taskScheduler);
        if (rebuildingRepositoriesCacheTTL < 0L) {
            rebuildingRepositoriesCacheTTL = 60L;
        }
        this.rebuildingRepositoriesCache = Suppliers.memoizeWithExpiration(this::getRepositoryNamesForRunningTasks, (long)Math.abs(rebuildingRepositoriesCacheTTL), (TimeUnit)TimeUnit.SECONDS);
    }

    public Map<String, Object> getState() {
        HashMap<String, Object> state = new HashMap<String, Object>();
        state.put("rebuildingRepositories", this.rebuildingRepositoriesCache.get());
        state.put("browseTreeMaxNodes", this.browseNodeConfiguration.getMaxNodes());
        return state;
    }

    private Set<String> getRepositoryNamesForRunningTasks() {
        HashSet<String> repositoryNames = new HashSet<String>();
        for (TaskInfo taskInfo : this.taskScheduler.listsTasks()) {
            if (!"create.browse.nodes".equals(taskInfo.getTypeId()) || !TaskState.RUNNING.equals((Object)taskInfo.getCurrentState().getRunState())) continue;
            String repositoryName = taskInfo.getConfiguration().getString("repositoryName");
            if ("*".equals(repositoryName)) {
                return Collections.singleton("*");
            }
            repositoryNames.add(repositoryName);
        }
        return repositoryNames;
    }
}

