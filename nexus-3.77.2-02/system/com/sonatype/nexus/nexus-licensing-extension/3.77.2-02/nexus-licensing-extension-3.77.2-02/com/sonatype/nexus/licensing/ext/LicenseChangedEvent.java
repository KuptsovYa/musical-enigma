/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.sonatype.licensing.product.ProductLicenseKey
 */
package com.sonatype.nexus.licensing.ext;

import javax.annotation.Nullable;
import org.sonatype.licensing.product.ProductLicenseKey;

public class LicenseChangedEvent {
    @Nullable
    private final ProductLicenseKey licenseKey;
    private final boolean valid;

    public LicenseChangedEvent(@Nullable ProductLicenseKey licenseKey, boolean valid) {
        this.licenseKey = licenseKey;
        this.valid = valid;
    }

    @Nullable
    public ProductLicenseKey getLicenseKey() {
        return this.licenseKey;
    }

    public boolean isLicenseValid() {
        return this.valid;
    }

    public boolean isLicenseInstalled() {
        return this.licenseKey != null;
    }

    public String toString() {
        return String.valueOf(this.getClass().getSimpleName()) + "{" + "licenseKey=" + this.licenseKey + ", valid=" + this.valid + '}';
    }
}

