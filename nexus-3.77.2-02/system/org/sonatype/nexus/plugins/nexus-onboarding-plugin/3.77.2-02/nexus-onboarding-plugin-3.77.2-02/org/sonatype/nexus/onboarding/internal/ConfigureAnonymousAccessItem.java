/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.security.anonymous.AnonymousManager
 */
package org.sonatype.nexus.onboarding.internal;

import com.google.common.base.Preconditions;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.onboarding.OnboardingItem;
import org.sonatype.nexus.onboarding.OnboardingItemPriority;
import org.sonatype.nexus.security.anonymous.AnonymousManager;

@Named
@Singleton
public class ConfigureAnonymousAccessItem
extends ComponentSupport
implements OnboardingItem {
    private final AnonymousManager anonymousManager;

    @Inject
    public ConfigureAnonymousAccessItem(AnonymousManager anonymousManager) {
        this.anonymousManager = (AnonymousManager)Preconditions.checkNotNull((Object)anonymousManager);
    }

    @Override
    public String getType() {
        return "ConfigureAnonymousAccess";
    }

    @Override
    public int getPriority() {
        return OnboardingItemPriority.CONFIGURE_ANONYMOUS_ACCESS;
    }

    @Override
    public boolean applies() {
        return !this.anonymousManager.isConfigured();
    }
}

