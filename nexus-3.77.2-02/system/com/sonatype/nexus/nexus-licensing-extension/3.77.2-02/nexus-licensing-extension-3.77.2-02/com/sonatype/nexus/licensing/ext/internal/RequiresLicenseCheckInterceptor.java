/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.Lists
 *  com.google.inject.Injector
 *  javax.inject.Provider
 *  org.aopalliance.intercept.MethodInterceptor
 *  org.aopalliance.intercept.MethodInvocation
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.licensing.feature.Feature
 *  org.sonatype.licensing.feature.LicenseFeatureVerifier
 *  org.sonatype.nexus.common.app.ManagedLifecycle$Phase
 *  org.sonatype.nexus.common.app.ManagedLifecycleManager
 */
package com.sonatype.nexus.licensing.ext.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Injector;
import com.sonatype.nexus.licensing.ext.RequiresLicenseCheck;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import javax.inject.Provider;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.licensing.feature.Feature;
import org.sonatype.licensing.feature.LicenseFeatureVerifier;
import org.sonatype.nexus.common.app.ManagedLifecycle;
import org.sonatype.nexus.common.app.ManagedLifecycleManager;

public class RequiresLicenseCheckInterceptor
extends ComponentSupport
implements MethodInterceptor {
    private static final ManagedLifecycle.Phase[] LIFECYCLE_PHASES = ManagedLifecycle.Phase.values();
    private final Provider<ManagedLifecycleManager> lifecycleManagerProvider;
    private final Provider<Injector> injectorProvider;
    private final Provider<LicenseFeatureVerifier> verifierProvider;
    private final ConcurrentHashMap<Integer, LongAdder> methodCallsCounterMap;

    public RequiresLicenseCheckInterceptor(Provider<ManagedLifecycleManager> lifecycleManagerProvider, Provider<Injector> injectorProvider, Provider<LicenseFeatureVerifier> verifierProvider) {
        this.lifecycleManagerProvider = (Provider)Preconditions.checkNotNull(lifecycleManagerProvider);
        this.injectorProvider = (Provider)Preconditions.checkNotNull(injectorProvider);
        this.verifierProvider = (Provider)Preconditions.checkNotNull(verifierProvider);
        this.methodCallsCounterMap = new ConcurrentHashMap();
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        RequiresLicenseCheck annotation = this.getAnnotation(methodInvocation);
        RequiresLicenseCheck.Frequency frequency = annotation.frequency();
        if (!this.containsCurrentPhase(annotation.phases())) {
            return methodInvocation.proceed();
        }
        switch (frequency) {
            case ALWAYS: {
                return this.verifyLicense(methodInvocation);
            }
            case EVERY_N_CALLS: {
                int numberOfCalls = annotation.numberOfCalls();
                return this.verifyLicense(methodInvocation, numberOfCalls);
            }
        }
        return methodInvocation.proceed();
    }

    private Object verifyLicense(MethodInvocation methodInvocation, int numberOfCalls) throws Throwable {
        int key = methodInvocation.getMethod().hashCode();
        LongAdder methodCallsCounter = this.methodCallsCounterMap.computeIfAbsent(key, k -> new LongAdder());
        if (methodCallsCounter.sum() < (long)numberOfCalls) {
            methodCallsCounter.increment();
            return methodInvocation.proceed();
        }
        this.log.debug("Verifying license after {} calls to {}...", (Object)numberOfCalls, (Object)methodInvocation.getMethod());
        Object result = this.verifyLicense(methodInvocation);
        methodCallsCounter.reset();
        return result;
    }

    private Object verifyLicense(MethodInvocation methodInvocation) throws Throwable {
        LicenseFeatureVerifier verifier = (LicenseFeatureVerifier)this.verifierProvider.get();
        Preconditions.checkState((verifier != null ? 1 : 0) != 0, (Object)("Could not get a " + LicenseFeatureVerifier.class.getName()));
        List<Feature> features = this.getFeatures(methodInvocation);
        this.log.debug("Verifying license and features {} upon calling {}", features, (Object)methodInvocation.getMethod());
        for (Feature feature : features) {
            verifier.verifyLicenseAndFeature(feature);
        }
        return methodInvocation.proceed();
    }

    private List<Feature> getFeatures(MethodInvocation methodInvocation) {
        RequiresLicenseCheck annotation = this.getAnnotation(methodInvocation);
        Class<? extends Feature>[] featureClasses = annotation.features();
        ArrayList features = Lists.newArrayList();
        Injector injector = (Injector)this.injectorProvider.get();
        Preconditions.checkState((injector != null ? 1 : 0) != 0, (Object)("Could not get an " + Injector.class.getName()));
        Class<? extends Feature>[] classArray = featureClasses;
        int n = featureClasses.length;
        int n2 = 0;
        while (n2 < n) {
            Class<? extends Feature> featureClass = classArray[n2];
            features.add((Feature)injector.getInstance(featureClass));
            ++n2;
        }
        return features;
    }

    private boolean containsCurrentPhase(ManagedLifecycle.Phase[] phasesToCheck) {
        if (phasesToCheck.length != 0) {
            ManagedLifecycleManager lifecycleManager = (ManagedLifecycleManager)this.lifecycleManagerProvider.get();
            Preconditions.checkState((lifecycleManager != null ? 1 : 0) != 0, (Object)("Could not get a " + LicenseFeatureVerifier.class.getName()));
            int currentPhaseIndex = lifecycleManager.getCurrentPhase().ordinal() + 1;
            currentPhaseIndex = Math.min(currentPhaseIndex, LIFECYCLE_PHASES.length - 1);
            ManagedLifecycle.Phase currentLifecyclePhase = LIFECYCLE_PHASES[currentPhaseIndex];
            ManagedLifecycle.Phase[] phaseArray = phasesToCheck;
            int n = phasesToCheck.length;
            int n2 = 0;
            while (n2 < n) {
                ManagedLifecycle.Phase phaseToCheck = phaseArray[n2];
                if (phaseToCheck == currentLifecyclePhase) {
                    return true;
                }
                ++n2;
            }
            return false;
        }
        return true;
    }

    private RequiresLicenseCheck getAnnotation(MethodInvocation methodInvocation) {
        return methodInvocation.getMethod().getAnnotation(RequiresLicenseCheck.class);
    }
}

