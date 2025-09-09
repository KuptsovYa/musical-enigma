/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.inject.Injector
 *  com.google.inject.matcher.Matchers
 *  javax.inject.Named
 *  javax.inject.Provider
 *  javax.inject.Singleton
 *  org.aopalliance.intercept.MethodInterceptor
 *  org.sonatype.licensing.RequiresLicense
 *  org.sonatype.licensing.feature.LicenseFeatureInterceptor
 *  org.sonatype.licensing.feature.LicenseFeatureVerifier
 *  org.sonatype.nexus.common.app.ManagedLifecycleManager
 *  org.sonatype.nexus.common.guice.AbstractInterceptorModule
 */
package com.sonatype.nexus.licensing.ext.internal;

import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.sonatype.nexus.licensing.ext.RequiresLicenseCheck;
import com.sonatype.nexus.licensing.ext.internal.RequiresLicenseCheckInterceptor;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import org.aopalliance.intercept.MethodInterceptor;
import org.sonatype.licensing.RequiresLicense;
import org.sonatype.licensing.feature.LicenseFeatureInterceptor;
import org.sonatype.licensing.feature.LicenseFeatureVerifier;
import org.sonatype.nexus.common.app.ManagedLifecycleManager;
import org.sonatype.nexus.common.guice.AbstractInterceptorModule;

@Named
@Singleton
public class LicensingModule
extends AbstractInterceptorModule {
    protected void configure() {
        this.bindInterceptor(Matchers.any(), Matchers.annotatedWith(RequiresLicense.class), new MethodInterceptor[]{new LicenseFeatureInterceptor((Provider)this.getProvider(Injector.class), (Provider)this.getProvider(LicenseFeatureVerifier.class))});
        this.bindInterceptor(Matchers.any(), Matchers.annotatedWith(RequiresLicenseCheck.class), new MethodInterceptor[]{new RequiresLicenseCheckInterceptor((Provider<ManagedLifecycleManager>)this.getProvider(ManagedLifecycleManager.class), (Provider<Injector>)this.getProvider(Injector.class), (Provider<LicenseFeatureVerifier>)this.getProvider(LicenseFeatureVerifier.class))});
    }
}

