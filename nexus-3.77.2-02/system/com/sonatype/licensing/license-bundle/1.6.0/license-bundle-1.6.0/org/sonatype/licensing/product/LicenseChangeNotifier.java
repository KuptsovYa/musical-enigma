/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing.product;

import org.sonatype.licensing.product.ProductLicenseKey;

public interface LicenseChangeNotifier {
    public void notifyListeners(ProductLicenseKey var1, boolean var2, Exception var3);
}

