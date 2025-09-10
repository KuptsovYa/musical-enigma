/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.rapture.StateContributor
 *  org.sonatype.nexus.security.config.AdminPasswordFileManager
 */
package org.sonatype.nexus.onboarding.internal;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.onboarding.OnboardingConfiguration;
import org.sonatype.nexus.onboarding.OnboardingManager;
import org.sonatype.nexus.rapture.StateContributor;
import org.sonatype.nexus.security.config.AdminPasswordFileManager;

@Singleton
@Named
public class OnboardingStateContributor
implements StateContributor {
    private final OnboardingConfiguration onboardingConfiguration;
    private final OnboardingManager onboardingManager;
    private final AdminPasswordFileManager adminPasswordFileManager;
    private boolean needsOnboarding = true;

    @Inject
    public OnboardingStateContributor(OnboardingConfiguration onboardingConfiguration, OnboardingManager onboardingManager, AdminPasswordFileManager adminPasswordFileManager) {
        this.onboardingConfiguration = (OnboardingConfiguration)Preconditions.checkNotNull((Object)onboardingConfiguration);
        this.onboardingManager = (OnboardingManager)Preconditions.checkNotNull((Object)onboardingManager);
        this.adminPasswordFileManager = (AdminPasswordFileManager)Preconditions.checkNotNull((Object)adminPasswordFileManager);
    }

    @Nullable
    public Map<String, Object> getState() {
        this.needsOnboarding = this.needsOnboarding && this.onboardingConfiguration.isEnabled() && this.onboardingManager.needsOnboarding();
        HashMap<String, Object> properties = new HashMap<String, Object>();
        if (this.needsOnboarding) {
            properties.put("onboarding.required", false);
        }
        if (this.adminPasswordFileManager.exists()) {
            properties.put("admin.password.file", this.adminPasswordFileManager.getPath());
        }
        return properties.isEmpty() ? null : properties;
    }
}

