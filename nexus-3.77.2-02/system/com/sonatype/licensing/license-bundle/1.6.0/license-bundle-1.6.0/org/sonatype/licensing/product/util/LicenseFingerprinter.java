/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 */
package org.sonatype.licensing.product.util;

import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.licensing.product.ProductLicenseKey;
import org.sonatype.licensing.product.util.LicenseContent;
import org.sonatype.licensing.product.util.LicenseFingerprintStrategy;
import org.sonatype.licensing.product.util.LicenseFingerprintStrategyImpl;

@Named
@Singleton
public class LicenseFingerprinter {
    private final LicenseFingerprintStrategy kzs;
    private final LicenseContent oxb;

    public LicenseFingerprinter() {
        this.oxb = null;
        this.kzs = new LicenseFingerprintStrategyImpl();
    }

    @Inject
    public LicenseFingerprinter(LicenseFingerprintStrategy licenseFingerprintStrategy, @Nullable LicenseContent licenseContent) {
        this.kzs = (LicenseFingerprintStrategy)Preconditions.checkNotNull((Object)licenseFingerprintStrategy);
        this.oxb = licenseContent;
    }

    public String calculate() {
        if (this.oxb == null) {
            throw new IllegalStateException("No license content available.");
        }
        return this.calculate(this.oxb.key());
    }

    public String calculate(ProductLicenseKey productLicenseKey) {
        return this.kzs.calculate(productLicenseKey);
    }
}

