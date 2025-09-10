/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.goodies.common.ComponentSupport
 */
package org.sonatype.nexus.onboarding.internal;

import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.onboarding.OnboardingConfiguration;
import org.sonatype.nexus.onboarding.OnboardingItem;
import org.sonatype.nexus.onboarding.OnboardingManager;

@Named
@Singleton
public class OnboardingManagerImpl
extends ComponentSupport
implements OnboardingManager {
    private final OnboardingConfiguration onboardingConfiguration;
    private final Set<OnboardingItem> onboardingItems;

    @Inject
    public OnboardingManagerImpl(Set<OnboardingItem> onboardingItems, OnboardingConfiguration onboardingConfiguration) {
        this.onboardingItems = (Set)Preconditions.checkNotNull(onboardingItems);
        this.onboardingConfiguration = (OnboardingConfiguration)Preconditions.checkNotNull((Object)onboardingConfiguration);
    }

    @Override
    public boolean needsOnboarding() {
        return !this.getOnboardingItems().isEmpty();
    }

    @Override
    public List<OnboardingItem> getOnboardingItems() {
        if (this.onboardingConfiguration.isEnabled()) {
            return this.onboardingItems.stream().filter(OnboardingItem::applies).sorted(Comparator.comparingInt(OnboardingItem::getPriority)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}

