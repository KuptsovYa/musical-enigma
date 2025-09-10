/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.inject.Named
 *  org.sonatype.nexus.capability.CapabilitySupport
 *  org.sonatype.nexus.capability.CapabilityType
 *  org.sonatype.nexus.capability.Condition
 */
package org.sonatype.nexus.onboarding.capability;

import java.util.Map;
import javax.annotation.Nullable;
import javax.inject.Named;
import org.sonatype.nexus.capability.CapabilitySupport;
import org.sonatype.nexus.capability.CapabilityType;
import org.sonatype.nexus.capability.Condition;
import org.sonatype.nexus.onboarding.capability.OnboardingCapabilityConfiguration;
import org.sonatype.nexus.onboarding.capability.OnboardingCapabilityDescriptor;

@Named(value="onboarding-wizard")
public class OnboardingCapability
extends CapabilitySupport<OnboardingCapabilityConfiguration> {
    public static final String TYPE_ID = "onboarding-wizard";
    public static final CapabilityType TYPE = CapabilityType.capabilityType((String)"onboarding-wizard");

    protected OnboardingCapabilityConfiguration createConfig(Map<String, String> properties) {
        return new OnboardingCapabilityConfiguration(properties);
    }

    @Nullable
    protected String renderDescription() {
        return this.context().isActive() ? OnboardingCapabilityDescriptor.messages.enabled() : OnboardingCapabilityDescriptor.messages.disabled();
    }

    public Condition activationCondition() {
        return this.conditions().logical().and(new Condition[]{this.conditions().nexus().active(), this.conditions().capabilities().capabilityHasNoDuplicates(), this.conditions().capabilities().passivateCapabilityDuringUpdate()});
    }

    public boolean isRegistrationStarted() {
        return ((OnboardingCapabilityConfiguration)((Object)this.getConfig())).isRegistrationStarted();
    }

    public void setRegistrationStarted(boolean registrationStarted) {
        ((OnboardingCapabilityConfiguration)((Object)this.getConfig())).setRegistrationStarted(registrationStarted);
    }

    public boolean isRegistrationCompleted() {
        return ((OnboardingCapabilityConfiguration)((Object)this.getConfig())).isRegistrationCompleted();
    }

    public void setRegistrationCompleted(boolean registrationCompleted) {
        ((OnboardingCapabilityConfiguration)((Object)this.getConfig())).setRegistrationCompleted(registrationCompleted);
    }
}

