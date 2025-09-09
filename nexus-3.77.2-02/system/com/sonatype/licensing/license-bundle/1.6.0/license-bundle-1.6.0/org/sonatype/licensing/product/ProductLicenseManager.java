/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing.product;

import java.io.IOException;
import java.io.InputStream;
import org.sonatype.licensing.LicensingException;
import org.sonatype.licensing.feature.Feature;
import org.sonatype.licensing.product.ProductLicenseKey;

public interface ProductLicenseManager {
    public void installLicense(InputStream var1) throws IOException, LicensingException;

    public void uninstallLicense() throws LicensingException;

    public ProductLicenseKey getLicenseDetails() throws LicensingException;

    public ProductLicenseKey getLicenseDetails(InputStream var1) throws IOException, LicensingException;

    public void verifyLicenseAndFeature(Feature var1) throws LicensingException;

    public void verifyFeature(ProductLicenseKey var1, Feature var2) throws LicensingException;
}

