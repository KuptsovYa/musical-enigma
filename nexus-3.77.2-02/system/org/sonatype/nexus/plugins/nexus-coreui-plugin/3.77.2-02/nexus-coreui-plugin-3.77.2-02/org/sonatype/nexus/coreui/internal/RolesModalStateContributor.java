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
public class RolesModalStateContributor
implements StateContributor {
    private final boolean isRolesModalEnabled;

    @Inject
    public RolesModalStateContributor(@Named(value="${nexus.react.roles.modal.enabled:-true}") boolean isRolesModalEnabled) {
        this.isRolesModalEnabled = isRolesModalEnabled;
    }

    public Map<String, Object> getState() {
        return ImmutableMap.of((Object)"nexus.react.roles.modal.enabled", (Object)this.isRolesModalEnabled);
    }
}

