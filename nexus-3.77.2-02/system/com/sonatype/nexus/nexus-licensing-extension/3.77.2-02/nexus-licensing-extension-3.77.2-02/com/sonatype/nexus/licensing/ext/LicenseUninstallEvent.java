/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.sonatype.licensing.product.ProductLicenseKey
 */
package com.sonatype.nexus.licensing.ext;

import com.sonatype.nexus.licensing.ext.LicenseSource;
import javax.annotation.Nullable;
import org.sonatype.licensing.product.ProductLicenseKey;

public class LicenseUninstallEvent {
    @Nullable
    private final ProductLicenseKey licenseKey;
    private final boolean valid;
    private final LicenseSource licenseSource;

    public LicenseUninstallEvent(@Nullable ProductLicenseKey licenseKey, boolean valid, LicenseSource licenseSource) {
        this.licenseKey = licenseKey;
        this.valid = valid;
        this.licenseSource = licenseSource;
    }

    @Nullable
    public ProductLicenseKey getLicenseKey() {
        return this.licenseKey;
    }

    public boolean isLicenseValid() {
        return this.valid;
    }

    public boolean isLicenseUninstalled() {
        return this.licenseKey == null;
    }

    public LicenseSource getLicenseSource() {
        return this.licenseSource;
    }

    public String toString() {
        return String.valueOf(this.getClass().getSimpleName()) + "{" + "licenseKey=" + this.licenseKey + ", valid=" + this.valid + ", licenseSource=" + this.licenseSource.getDescription() + '}';
    }
}

