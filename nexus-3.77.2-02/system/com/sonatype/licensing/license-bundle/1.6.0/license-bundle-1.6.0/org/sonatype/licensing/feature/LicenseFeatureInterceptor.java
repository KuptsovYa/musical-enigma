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
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.sonatype.licensing.feature;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Injector;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Provider;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.licensing.RequiresLicense;
import org.sonatype.licensing.feature.Feature;
import org.sonatype.licensing.feature.LicenseFeatureVerifier;

public class LicenseFeatureInterceptor
implements MethodInterceptor {
    private static final Logger evv = LoggerFactory.getLogger(LicenseFeatureInterceptor.class);
    private final Provider<Injector> iui;
    private final Provider<LicenseFeatureVerifier> tjb;

    public LicenseFeatureInterceptor(Provider<Injector> provider, Provider<LicenseFeatureVerifier> provider2) {
        this.iui = (Provider)Preconditions.checkNotNull(provider);
        this.tjb = (Provider)Preconditions.checkNotNull(provider2);
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        LicenseFeatureVerifier licenseFeatureVerifier = (LicenseFeatureVerifier)this.tjb.get();
        Preconditions.checkState((licenseFeatureVerifier != null ? 1 : 0) != 0, (Object)("Could not get a " + LicenseFeatureVerifier.class.getName()));
        List<Feature> list = this.itm(methodInvocation);
        if (evv.isDebugEnabled()) {
            evv.debug("Verifying license and features {} upon calling {}", list, (Object)methodInvocation.getMethod());
        }
        for (Feature feature : list) {
            licenseFeatureVerifier.verifyLicenseAndFeature(feature);
        }
        return methodInvocation.proceed();
    }

    private List<Feature> itm(MethodInvocation methodInvocation) {
        RequiresLicense requiresLicense = this.omj(methodInvocation);
        Class<? extends Feature>[] classArray = requiresLicense.features();
        ArrayList arrayList = Lists.newArrayList();
        Injector injector = (Injector)this.iui.get();
        Preconditions.checkState((injector != null ? 1 : 0) != 0, (Object)("Could not get an " + Injector.class.getName()));
        for (Class<? extends Feature> clazz : classArray) {
            arrayList.add(injector.getInstance(clazz));
        }
        return arrayList;
    }

    private RequiresLicense omj(MethodInvocation methodInvocation) {
        return methodInvocation.getMethod().getAnnotation(RequiresLicense.class);
    }
}

