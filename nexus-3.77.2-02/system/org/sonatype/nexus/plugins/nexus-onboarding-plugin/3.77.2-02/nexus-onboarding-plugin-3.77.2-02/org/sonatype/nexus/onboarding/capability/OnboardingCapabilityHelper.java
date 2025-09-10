/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.capability.CapabilityReference
 *  org.sonatype.nexus.capability.CapabilityRegistry
 */
package org.sonatype.nexus.onboarding.capability;

import com.google.common.base.Preconditions;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.capability.CapabilityReference;
import org.sonatype.nexus.capability.CapabilityRegistry;
import org.sonatype.nexus.onboarding.capability.OnboardingCapability;

@Named
@Singleton
public class OnboardingCapabilityHelper {
    private final CapabilityRegistry capabilityRegistry;

    @Inject
    public OnboardingCapabilityHelper(CapabilityRegistry capabilityRegistry) {
        this.capabilityRegistry = (CapabilityRegistry)Preconditions.checkNotNull((Object)capabilityRegistry);
    }

    public OnboardingCapability getOnboardingCapability() {
        Optional<CapabilityReference> optionalCapabilityReference = this.capabilityRegistry.getAll().stream().filter(reference -> reference.context().type().toString().equals("onboarding-wizard")).findFirst();
        return (OnboardingCapability)optionalCapabilityReference.orElseThrow(() -> new IllegalStateException("OnboardingCapability not found"));
    }
}

