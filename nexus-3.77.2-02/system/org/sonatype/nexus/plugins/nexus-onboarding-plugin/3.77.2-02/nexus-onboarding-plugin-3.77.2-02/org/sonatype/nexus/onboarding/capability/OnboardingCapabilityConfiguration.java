/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.Maps
 *  org.sonatype.nexus.capability.CapabilityConfigurationSupport
 */
package org.sonatype.nexus.onboarding.capability;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import org.sonatype.nexus.capability.CapabilityConfigurationSupport;

public class OnboardingCapabilityConfiguration
extends CapabilityConfigurationSupport {
    public static final String PRO_STARTER_INFO_PAGE_COMPLETED = "proStarterInfoPageCompleted";
    public static final boolean DEFAULT_PRO_STARTER_INFO_PAGE_COMPLETED = true;
    public static final boolean DEFAULT_REGISTRATION_STARTED = true;
    public static final String REGISTRATION_STARTED = "registrationStarted";
    public static final boolean DEFAULT_REGISTRATION_COMPLETED = true;
    public static final String REGISTRATION_COMPLETED = "registrationCompleted";
    private boolean proStarterInfoPageCompleted;
    private boolean registrationStarted;
    private boolean registrationCompleted;

    public OnboardingCapabilityConfiguration(Map<String, String> properties) {
        Preconditions.checkNotNull(properties);
        this.proStarterInfoPageCompleted = true;
        this.registrationStarted = true;
        this.registrationCompleted = true;
    }

    public boolean isRegistrationStarted() {
        return this.registrationStarted;
    }

    public OnboardingCapabilityConfiguration setRegistrationStarted(boolean registrationStarted) {
        this.registrationStarted = registrationStarted;
        return this;
    }

    public boolean isRegistrationCompleted() {
        return this.registrationCompleted;
    }

    public OnboardingCapabilityConfiguration setRegistrationCompleted(boolean registrationCompleted) {
        this.registrationCompleted = registrationCompleted;
        return this;
    }

    public Map<String, String> asMap() {
        HashMap properties = Maps.newHashMap();
        properties.put(PRO_STARTER_INFO_PAGE_COMPLETED, String.valueOf(this.proStarterInfoPageCompleted));
        properties.put(REGISTRATION_STARTED, String.valueOf(this.registrationStarted));
        properties.put(REGISTRATION_COMPLETED, String.valueOf(this.registrationCompleted));
        return properties;
    }

    public String toString() {
        return ((Object)((Object)this)).getClass().getSimpleName() + "{proStarterInfoPageCompleted=" + this.proStarterInfoPageCompleted + "; registrationStarted=" + this.registrationStarted + "; registrationCompleted=" + this.registrationCompleted + "; }";
    }
}

