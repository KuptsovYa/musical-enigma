/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.rapture.StateContributor
 */
package org.sonatype.nexus.coreui.internal.log;

import java.util.Collections;
import java.util.Map;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.rapture.StateContributor;

@Named
@Singleton
public class LogViewerStateContributor
implements StateContributor {
    @Inject
    @Named(value="${nexus.datastore.clustered.enabled:-false}")
    private boolean enabled;

    @Nullable
    public Map<String, Object> getState() {
        return Collections.singletonMap("log.viewer.enabled", !this.enabled);
    }
}

