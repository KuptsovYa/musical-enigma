/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.common.db.DatabaseCheck
 *  org.sonatype.nexus.rapture.StateContributor
 */
package org.sonatype.nexus.coreui.internal;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.common.db.DatabaseCheck;
import org.sonatype.nexus.rapture.StateContributor;

@Singleton
@Named
public class DatastoreStateContributor
implements StateContributor {
    private boolean datastoreEnabled;
    private boolean datastoreDeveloper;
    private boolean isPostgresql;

    @Inject
    public DatastoreStateContributor(@Named(value="${nexus.datastore.enabled:-true}") boolean datastoreEnabled, @Named(value="${nexus.datastore.developer:-false}") boolean datastoreDeveloper, DatabaseCheck dbCheck) {
        this.datastoreEnabled = datastoreEnabled;
        this.datastoreDeveloper = datastoreDeveloper;
        this.isPostgresql = dbCheck.isPostgresql();
    }

    public Map<String, Object> getState() {
        return ImmutableMap.of((Object)"nexus.datastore.enabled", (Object)this.datastoreEnabled, (Object)"nexus.datastore.developer", (Object)this.datastoreDeveloper, (Object)"datastore.isPostgresql", (Object)this.isPostgresql);
    }
}

