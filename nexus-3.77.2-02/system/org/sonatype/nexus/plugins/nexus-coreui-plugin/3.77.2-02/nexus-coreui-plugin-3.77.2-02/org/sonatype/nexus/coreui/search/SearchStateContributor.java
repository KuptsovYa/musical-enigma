/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.rapture.StateContributor
 */
package org.sonatype.nexus.coreui.search;

import java.util.Collections;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.rapture.StateContributor;

@Named
@Singleton
public class SearchStateContributor
implements StateContributor {
    private final boolean sqlSearch;

    @Inject
    public SearchStateContributor(@Named(value="${nexus.datastore.table.search.enabled:-false}") boolean sqlSearch) {
        this.sqlSearch = sqlSearch;
    }

    public Map<String, Object> getState() {
        return Collections.singletonMap("sqlSearchEnabled", this.sqlSearch);
    }
}

