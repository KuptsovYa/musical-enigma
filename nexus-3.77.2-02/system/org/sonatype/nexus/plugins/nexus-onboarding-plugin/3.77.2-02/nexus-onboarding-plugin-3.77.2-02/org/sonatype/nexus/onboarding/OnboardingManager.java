/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.onboarding;

import java.util.List;
import org.sonatype.nexus.onboarding.OnboardingItem;

public interface OnboardingManager {
    public boolean needsOnboarding();

    public List<OnboardingItem> getOnboardingItems();
}

