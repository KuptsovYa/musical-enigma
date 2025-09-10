/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 */
package org.sonatype.nexus.onboarding;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class OnboardingConfiguration {
    private boolean enabled;

    @Inject
    public OnboardingConfiguration(@Named(value="${nexus.onboarding.enabled:-true}") boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return this.enabled;
    }
}

