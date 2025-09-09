/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.licensing.product.PeriodicLicenseValidator
 *  org.sonatype.nexus.common.app.ManagedLifecycle
 *  org.sonatype.nexus.common.app.ManagedLifecycle$Phase
 *  org.sonatype.nexus.common.stateguard.StateGuardLifecycleSupport
 */
package com.sonatype.nexus.licensing.ext.internal;

import com.google.common.base.Preconditions;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.licensing.product.PeriodicLicenseValidator;
import org.sonatype.nexus.common.app.ManagedLifecycle;
import org.sonatype.nexus.common.stateguard.StateGuardLifecycleSupport;

@Named
@ManagedLifecycle(phase=ManagedLifecycle.Phase.SERVICES)
@Singleton
public class PeriodicLicenseValidatorEventHandler
extends StateGuardLifecycleSupport {
    private final PeriodicLicenseValidator licenseValidator;

    @Inject
    public PeriodicLicenseValidatorEventHandler(PeriodicLicenseValidator licenseValidator) {
        this.licenseValidator = (PeriodicLicenseValidator)Preconditions.checkNotNull((Object)licenseValidator);
    }

    protected void doStart() throws Exception {
        this.licenseValidator.start();
    }

    protected void doStop() throws Exception {
        this.licenseValidator.stop();
    }
}

