/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.capability.CapabilityBooterSupport
 *  org.sonatype.nexus.capability.CapabilityRegistry
 */
package org.sonatype.nexus.onboarding.capability;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.capability.CapabilityBooterSupport;
import org.sonatype.nexus.capability.CapabilityRegistry;
import org.sonatype.nexus.onboarding.capability.OnboardingCapability;

@Named
@Singleton
public class OnboardingCapabilityBooter
extends CapabilityBooterSupport {
    protected void boot(CapabilityRegistry registry) throws Exception {
        this.maybeAddCapability(registry, OnboardingCapability.TYPE, true, null, (Map)ImmutableMap.of((Object)"proStarterInfoPageCompleted", (Object)String.valueOf(false), (Object)"registrationStarted", (Object)String.valueOf(false), (Object)"registrationCompleted", (Object)String.valueOf(false)));
    }
}

