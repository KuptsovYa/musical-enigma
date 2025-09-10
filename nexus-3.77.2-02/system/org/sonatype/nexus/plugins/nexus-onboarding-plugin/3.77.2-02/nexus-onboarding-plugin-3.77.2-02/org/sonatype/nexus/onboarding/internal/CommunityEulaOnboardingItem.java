/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.common.app.ApplicationVersion
 *  org.sonatype.nexus.kv.GlobalKeyValueStore
 */
package org.sonatype.nexus.onboarding.internal;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.common.app.ApplicationVersion;
import org.sonatype.nexus.kv.GlobalKeyValueStore;
import org.sonatype.nexus.onboarding.OnboardingItemPriority;
import org.sonatype.nexus.onboarding.internal.CommunityOnboardingItem;

@Named
@Singleton
public class CommunityEulaOnboardingItem
extends CommunityOnboardingItem {
    @Inject
    public CommunityEulaOnboardingItem(ApplicationVersion applicationVersion, GlobalKeyValueStore globalKeyValueStore) {
        super(applicationVersion, globalKeyValueStore);
    }

    @Override
    public String getType() {
        return "CommunityEula";
    }

    @Override
    public int getPriority() {
        return OnboardingItemPriority.COMMUNITY_EULA_ONBOARDING;
    }
}

