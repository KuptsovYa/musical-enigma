/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.common.app.FeatureFlag
 */
package org.sonatype.nexus.onboarding.internal;

import com.google.common.base.Preconditions;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.common.app.FeatureFlag;
import org.sonatype.nexus.onboarding.OnboardingItem;
import org.sonatype.nexus.onboarding.OnboardingItemPriority;
import org.sonatype.nexus.onboarding.capability.OnboardingCapabilityHelper;
import org.sonatype.nexus.onboarding.internal.InstanceStatus;

@Named
@Singleton
@FeatureFlag(name="nexus.onboarding.license.enabled")
public class UploadLicensePageOnboardingItem
implements OnboardingItem {
    private final InstanceStatus instanceStatus;
    private final OnboardingCapabilityHelper onboardingCapabilityHelper;

    @Inject
    public UploadLicensePageOnboardingItem(InstanceStatus instanceStatus, OnboardingCapabilityHelper onboardingCapabilityHelper) {
        this.instanceStatus = (InstanceStatus)Preconditions.checkNotNull((Object)instanceStatus);
        this.onboardingCapabilityHelper = (OnboardingCapabilityHelper)Preconditions.checkNotNull((Object)onboardingCapabilityHelper);
    }

    @Override
    public String getType() {
        return "UploadLicensePage";
    }

    @Override
    public boolean applies() {
        return this.instanceStatus.isNew() && !this.onboardingCapabilityHelper.getOnboardingCapability().isRegistrationCompleted();
    }

    @Override
    public int getPriority() {
        return OnboardingItemPriority.UPLOAD_LICENSE;
    }
}

