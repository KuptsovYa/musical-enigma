/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.rapture.StateContributor
 */
package org.sonatype.nexus.coreui;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.rapture.StateContributor;

@Named
@Singleton
public class ApiDocsStateContributor
implements StateContributor {
    private final boolean enabled;

    @Inject
    public ApiDocsStateContributor(@Named(value="${nexus.admin.system.apidocs.enabled:-true}") boolean enabled) {
        this.enabled = enabled;
    }

    @Nullable
    public Map<String, Object> getState() {
        return ImmutableMap.of((Object)"api", (Object)this.enabled);
    }
}

