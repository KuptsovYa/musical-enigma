/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.annotation.ExceptionMetered
 *  com.codahale.metrics.annotation.Timed
 *  com.google.common.collect.ImmutableMap
 *  com.softwarementors.extjs.djn.config.annotations.DirectAction
 *  com.softwarementors.extjs.djn.config.annotations.DirectMethod
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.datastore.api.DataStore
 *  org.sonatype.nexus.datastore.api.DataStoreManager
 *  org.sonatype.nexus.extdirect.DirectComponentSupport
 *  org.sonatype.nexus.rapture.StateContributor
 *  org.sonatype.nexus.repository.manager.RepositoryManager
 *  org.sonatype.nexus.repository.security.RepositoryPermissionChecker
 *  org.sonatype.nexus.security.privilege.ApplicationPermission
 */
package org.sonatype.nexus.coreui.internal.datastore;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.ImmutableMap;
import com.softwarementors.extjs.djn.config.annotations.DirectAction;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.coreui.internal.datastore.DataStoreXO;
import org.sonatype.nexus.datastore.api.DataStore;
import org.sonatype.nexus.datastore.api.DataStoreManager;
import org.sonatype.nexus.extdirect.DirectComponentSupport;
import org.sonatype.nexus.rapture.StateContributor;
import org.sonatype.nexus.repository.manager.RepositoryManager;
import org.sonatype.nexus.repository.security.RepositoryPermissionChecker;
import org.sonatype.nexus.security.privilege.ApplicationPermission;

@Named
@Singleton
@DirectAction(action={"coreui_Datastore"})
public class DataStoreComponent
extends DirectComponentSupport
implements StateContributor {
    private static final String DATASTORES_FIELD = "datastores";
    private static final String JDBCURL_FIELD = "jdbcUrl";
    private static final String DATASTORES_PERMISSION = "datastores";
    private final DataStoreManager dataStoreManager;
    private final RepositoryManager repositoryManager;
    private final RepositoryPermissionChecker repositoryPermissionChecker;
    private final boolean enabled;

    @Inject
    public DataStoreComponent(DataStoreManager dataStoreManager, RepositoryManager repositoryManager, RepositoryPermissionChecker repositoryPermissionChecker, @Named(value="${nexus.datastore.enabled:-true}") boolean enabled) {
        this.dataStoreManager = dataStoreManager;
        this.repositoryManager = repositoryManager;
        this.repositoryPermissionChecker = repositoryPermissionChecker;
        this.enabled = enabled;
    }

    @Nullable
    public Map<String, Object> getState() {
        return ImmutableMap.of((Object)"datastores", (Object)this.enabled);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    public List<DataStoreXO> read() {
        this.repositoryPermissionChecker.ensureUserHasAnyPermissionOrAdminAccess(Collections.singletonList(new ApplicationPermission("datastores", new String[]{"read"})), "read", this.repositoryManager.browse());
        return StreamSupport.stream(this.dataStoreManager.browse().spliterator(), false).map(this::asDataStoreXO).collect(Collectors.toList());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    public List<DataStoreXO> readH2() {
        this.repositoryPermissionChecker.ensureUserHasAnyPermissionOrAdminAccess(Collections.singletonList(new ApplicationPermission("datastores", new String[]{"read"})), "read", this.repositoryManager.browse());
        return StreamSupport.stream(this.dataStoreManager.browse().spliterator(), false).filter(dataStore -> dataStore.getConfiguration().getAttributes().getOrDefault(JDBCURL_FIELD, "").startsWith("jdbc:h2:")).map(this::asDataStoreXO).collect(Collectors.toList());
    }

    private DataStoreXO asDataStoreXO(DataStore<?> dataStore) {
        DataStoreXO dataStoreXO = new DataStoreXO();
        dataStoreXO.setName(dataStore.getConfiguration().getName());
        return dataStoreXO;
    }
}

