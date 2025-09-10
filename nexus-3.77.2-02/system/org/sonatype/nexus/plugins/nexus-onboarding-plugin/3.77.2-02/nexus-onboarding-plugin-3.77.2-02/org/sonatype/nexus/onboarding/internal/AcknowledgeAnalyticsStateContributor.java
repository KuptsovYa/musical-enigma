/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.capability.CapabilityReferenceFilterBuilder$CapabilityReferenceFilter
 *  org.sonatype.nexus.capability.CapabilityRegistry
 *  org.sonatype.nexus.capability.CapabilityType
 *  org.sonatype.nexus.common.app.ApplicationVersion
 *  org.sonatype.nexus.rapture.StateContributor
 */
package org.sonatype.nexus.onboarding.internal;

import com.google.common.base.Predicate;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.capability.CapabilityReferenceFilterBuilder;
import org.sonatype.nexus.capability.CapabilityRegistry;
import org.sonatype.nexus.capability.CapabilityType;
import org.sonatype.nexus.common.app.ApplicationVersion;
import org.sonatype.nexus.rapture.StateContributor;

@Named
@Singleton
public class AcknowledgeAnalyticsStateContributor
implements StateContributor {
    protected static final String OSS = "OSS";
    private static final String ANALYTICS_CONFIGURATION = "analytics-configuration";
    private final ApplicationVersion applicationVersion;
    private final CapabilityRegistry capabilityRegistry;

    @Inject
    public AcknowledgeAnalyticsStateContributor(ApplicationVersion applicationVersion, CapabilityRegistry capabilityRegistry) {
        this.applicationVersion = applicationVersion;
        this.capabilityRegistry = capabilityRegistry;
    }

    @Nullable
    public Map<String, Object> getState() {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("acknowledgeAnalytics.required", this.applies());
        return properties;
    }

    private boolean applies() {
        return OSS.equals(this.applicationVersion.getEdition()) && this.analyticsCapabilityAbsent();
    }

    private boolean analyticsCapabilityAbsent() {
        CapabilityType capabilityType = CapabilityType.capabilityType((String)ANALYTICS_CONFIGURATION);
        return this.capabilityRegistry.get((Predicate)new CapabilityReferenceFilterBuilder.CapabilityReferenceFilter().withType(capabilityType).includeNotExposed()).isEmpty();
    }
}

