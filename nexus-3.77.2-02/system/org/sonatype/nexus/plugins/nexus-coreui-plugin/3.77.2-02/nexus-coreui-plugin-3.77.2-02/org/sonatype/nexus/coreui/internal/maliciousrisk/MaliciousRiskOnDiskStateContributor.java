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
package org.sonatype.nexus.coreui.internal.maliciousrisk;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.rapture.StateContributor;

@Named
@Singleton
public class MaliciousRiskOnDiskStateContributor
implements StateContributor {
    private final Map<String, Object> state;

    @Inject
    public MaliciousRiskOnDiskStateContributor(@Named(value="${nexus.malware.risk.on.disk.enabled:-true}") boolean maliciousRiskOnDiskEnabled, @Named(value="${nexus.malware.risk.on.disk.nonadmin.override.enabled:-false}") boolean maliciousRiskOnDiskNoneAdminOverrideEnabled) {
        this.state = ImmutableMap.of((Object)"nexus.malware.risk.on.disk.enabled", (Object)maliciousRiskOnDiskEnabled, (Object)"nexus.malware.risk.on.disk.nonadmin.override.enabled", (Object)maliciousRiskOnDiskNoneAdminOverrideEnabled);
    }

    public Map<String, Object> getState() {
        return this.state;
    }
}

