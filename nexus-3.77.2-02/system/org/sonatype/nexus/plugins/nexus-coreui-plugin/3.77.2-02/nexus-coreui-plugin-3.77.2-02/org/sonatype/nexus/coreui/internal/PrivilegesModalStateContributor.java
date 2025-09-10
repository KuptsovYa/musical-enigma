/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.rapture.StateContributor
 */
package org.sonatype.nexus.coreui.internal;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.rapture.StateContributor;

@Singleton
@Named
public class PrivilegesModalStateContributor
implements StateContributor {
    private final boolean isPrivilegesModalEnabled;

    @Inject
    public PrivilegesModalStateContributor(@Named(value="${nexus.react.privileges.modal.enabled:-true}") boolean isPrivilegesModalEnabled) {
        this.isPrivilegesModalEnabled = isPrivilegesModalEnabled;
    }

    public Map<String, Object> getState() {
        return ImmutableMap.of((Object)"nexus.react.privileges.modal.enabled", (Object)this.isPrivilegesModalEnabled);
    }
}

