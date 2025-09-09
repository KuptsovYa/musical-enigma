/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.sonatype.licensing.product.ProductLicenseKey
 */
package com.sonatype.nexus.licensing.ext;

import java.util.Date;
import org.sonatype.licensing.product.ProductLicenseKey;

public class LicenseExpirationEvent {
    private final ProductLicenseKey licenseKey;
    private final Date expirationDate;
    private final int daysUntilExpiration;
    private final boolean expired;

    public LicenseExpirationEvent(ProductLicenseKey licenseKey, Date expirationDate, int daysUntilExpiration, boolean expired) {
        this.licenseKey = licenseKey;
        this.expirationDate = expirationDate;
        this.daysUntilExpiration = daysUntilExpiration;
        this.expired = expired;
    }

    public ProductLicenseKey getLicenseKey() {
        return this.licenseKey;
    }

    public Date getExpirationDate() {
        return this.expirationDate;
    }

    public int getDaysUntilExpiration() {
        return this.daysUntilExpiration;
    }

    public boolean isExpired() {
        return this.expired;
    }

    public String toString() {
        return String.valueOf(this.getClass().getSimpleName()) + "{" + "licenseKey=" + this.licenseKey + ", expirationDate=" + this.expirationDate + ", daysUntilExpiration=" + this.daysUntilExpiration + ", expired=" + this.expired + '}';
    }
}

