/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.security.anonymous.AnonymousManager
 */
package org.sonatype.nexus.onboarding.internal;

import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.onboarding.capability.OnboardingCapability;
import org.sonatype.nexus.onboarding.capability.OnboardingCapabilityHelper;
import org.sonatype.nexus.security.anonymous.AnonymousManager;

@Named
@Singleton
public class InstanceStatus {
    private final AnonymousManager anonymousManager;
    private final OnboardingCapabilityHelper onboardingCapabilityHelper;

    @Inject
    public InstanceStatus(AnonymousManager anonymousManager, OnboardingCapabilityHelper onboardingCapabilityHelper) {
        this.anonymousManager = Objects.requireNonNull(anonymousManager);
        this.onboardingCapabilityHelper = Objects.requireNonNull(onboardingCapabilityHelper);
    }

    public boolean isNew() {
        if (!this.anonymousManager.isConfigured()) {
            return true;
        }
        OnboardingCapability onboardingCapability = this.onboardingCapabilityHelper.getOnboardingCapability();
        return onboardingCapability.isRegistrationStarted() && !onboardingCapability.isRegistrationCompleted();
    }

    public boolean isUpgraded() {
        OnboardingCapability onboardingCapability = this.onboardingCapabilityHelper.getOnboardingCapability();
        return this.anonymousManager.isConfigured() && !onboardingCapability.isRegistrationStarted();
    }
}

