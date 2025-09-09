/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.inject.AbstractModule
 *  com.google.inject.Injector
 *  com.google.inject.matcher.Matchers
 *  javax.inject.Provider
 *  org.aopalliance.intercept.MethodInterceptor
 */
package org.sonatype.licensing;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import javax.inject.Provider;
import org.aopalliance.intercept.MethodInterceptor;
import org.sonatype.licensing.RequiresLicense;
import org.sonatype.licensing.feature.LicenseFeatureInterceptor;
import org.sonatype.licensing.feature.LicenseFeatureVerifier;

public class LicensingModule
extends AbstractModule {
    protected void configure() {
        this.bindInterceptor(Matchers.any(), Matchers.annotatedWith(RequiresLicense.class), new MethodInterceptor[]{new LicenseFeatureInterceptor((Provider<Injector>)this.getProvider(Injector.class), (Provider<LicenseFeatureVerifier>)this.getProvider(LicenseFeatureVerifier.class))});
    }
}

