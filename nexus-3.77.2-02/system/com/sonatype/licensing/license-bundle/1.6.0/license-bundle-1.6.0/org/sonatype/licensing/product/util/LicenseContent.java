/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.apache.commons.codec.binary.Base64
 */
package org.sonatype.licensing.product.util;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.commons.codec.binary.Base64;
import org.sonatype.licensing.LicenseKey;
import org.sonatype.licensing.LicensingException;
import org.sonatype.licensing.PreferencesFactory;
import org.sonatype.licensing.product.LicenseBuilder;
import org.sonatype.licensing.product.ProductLicenseKey;
import org.sonatype.licensing.product.ProductLicenseManager;

@Named
@Singleton
public class LicenseContent {
    private final ProductLicenseManager sua;
    private final String qbc;
    private final PreferencesFactory xpt;

    @Inject
    public LicenseContent(LicenseBuilder licenseBuilder, ProductLicenseManager productLicenseManager, PreferencesFactory preferencesFactory) {
        this.sua = productLicenseManager;
        this.qbc = licenseBuilder.getPreferenceNodePath();
        this.xpt = preferencesFactory;
    }

    public byte[] raw() {
        return Base64.decodeBase64((String)this.xpt.nodeForPath(this.qbc).get("license", null));
    }

    public ProductLicenseKey key() {
        try {
            return this.sua.getLicenseDetails();
        }
        catch (LicensingException licensingException) {
            LicenseKey licenseKey = licensingException.getKey();
            if (!(licenseKey instanceof ProductLicenseKey)) {
                return null;
            }
            return (ProductLicenseKey)licenseKey;
        }
    }
}

