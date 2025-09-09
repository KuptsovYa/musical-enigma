/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing.product;

import org.sonatype.licensing.product.ProductLicenseKey;

public interface LicenseChangeListener {
    public void licenseChanged(ProductLicenseKey var1, boolean var2);

    public static class itm
    implements LicenseChangeListener {
        @Override
        public void licenseChanged(ProductLicenseKey productLicenseKey, boolean bl) {
        }
    }
}

