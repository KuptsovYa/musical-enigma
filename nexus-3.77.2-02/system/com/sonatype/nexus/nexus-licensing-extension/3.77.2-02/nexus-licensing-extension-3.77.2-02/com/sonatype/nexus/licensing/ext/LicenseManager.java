/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.sonatype.licensing.product.ProductLicenseKey
 */
package com.sonatype.nexus.licensing.ext;

import com.sonatype.nexus.licensing.ext.LicenseSource;
import java.io.IOException;
import java.io.InputStream;
import org.sonatype.licensing.product.ProductLicenseKey;

public interface LicenseManager {
    public void installLicense(byte[] var1, LicenseSource var2) throws IOException;

    public void uninstallLicense(LicenseSource var1);

    public ProductLicenseKey getLicenseDetails();

    public ProductLicenseKey getLicenseDetails(InputStream var1) throws IOException;

    public String getLicenseFingerprint();

    public byte[] getLicenseFile();
}

