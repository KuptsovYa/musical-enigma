/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.licensing.feature.Feature
 *  org.sonatype.licensing.feature.LicenseFeatureVerifier
 *  org.sonatype.nexus.capability.Condition
 *  org.sonatype.nexus.common.event.EventManager
 */
package com.sonatype.nexus.licensing.ext.internal;

import com.google.common.base.Preconditions;
import com.sonatype.nexus.licensing.ext.capability.LicensingConditions;
import com.sonatype.nexus.licensing.ext.internal.LicenseIsValidCondition;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.licensing.feature.Feature;
import org.sonatype.licensing.feature.LicenseFeatureVerifier;
import org.sonatype.nexus.capability.Condition;
import org.sonatype.nexus.common.event.EventManager;

@Named
@Singleton
public class LicensingConditionsImpl
implements LicensingConditions {
    private final EventManager eventManager;
    private final LicenseFeatureVerifier licenseFeatureVerifier;

    @Inject
    public LicensingConditionsImpl(EventManager eventManager, LicenseFeatureVerifier licenseFeatureVerifier) {
        this.eventManager = (EventManager)Preconditions.checkNotNull((Object)eventManager);
        this.licenseFeatureVerifier = (LicenseFeatureVerifier)Preconditions.checkNotNull((Object)licenseFeatureVerifier);
    }

    @Override
    public Condition licenseIsValid(Feature feature) {
        return new LicenseIsValidCondition(this.eventManager, this.licenseFeatureVerifier, feature);
    }
}

