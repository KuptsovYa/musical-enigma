/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing.product.util;

import org.sonatype.licensing.product.ProductLicenseKey;

public interface LicenseFingerprintStrategy {
    public String calculate(ProductLicenseKey var1);
}

