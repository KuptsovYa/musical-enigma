/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing;

import java.io.File;
import org.sonatype.licensing.LicenseKey;
import org.sonatype.licensing.LicenseKeyRequest;
import org.sonatype.licensing.LicenseParam;
import org.sonatype.licensing.LicensingException;
import org.sonatype.licensing.feature.Feature;

public interface LicenseManager {
    public LicenseKey createLicense(LicenseParam var1, LicenseKeyRequest var2) throws LicensingException;

    public LicenseKey installLicense(LicenseParam var1, File var2) throws LicensingException;

    public void uninstallLicense(LicenseParam var1) throws LicensingException;

    public LicenseKey verifyLicense(LicenseParam var1) throws LicensingException;

    public LicenseKey verifyLicense(LicenseParam var1, File var2) throws LicensingException;

    public void validateFeature(LicenseKey var1, Feature var2) throws LicensingException;
}

