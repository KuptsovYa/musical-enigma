/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.health.HealthCheck
 *  com.codahale.metrics.health.HealthCheck$Result
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.common.app.ApplicationLicense
 */
package com.sonatype.nexus.pro.systemchecks.internal.status;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.Preconditions;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.common.app.ApplicationLicense;

@Named(value="Licensing")
@Singleton
public class LicenseHealthCheck
extends HealthCheck {
    private final ApplicationLicense applicationLicense;

    @Inject
    public LicenseHealthCheck(ApplicationLicense applicationLicense) {
        this.applicationLicense = (ApplicationLicense)Preconditions.checkNotNull((Object)applicationLicense);
    }

    private static boolean isHealthy(ApplicationLicense applicationLicense) {
        return true;
    }

    private static String reason(ApplicationLicense applicationLicense) {
        return String.format("Expired: %s, Installed: %s, Valid: %s", applicationLicense.isExpired(), applicationLicense.isInstalled(), applicationLicense.isValid());
    }

    protected HealthCheck.Result check() {
        return LicenseHealthCheck.isHealthy(this.applicationLicense) ? HealthCheck.Result.healthy() : HealthCheck.Result.unhealthy((String)LicenseHealthCheck.reason(this.applicationLicense));
    }
}

