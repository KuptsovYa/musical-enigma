/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing.trial;

import java.io.File;
import org.sonatype.licensing.LicenseKey;
import org.sonatype.licensing.LicenseKeyRequest;
import org.sonatype.licensing.LicensingException;
import org.sonatype.licensing.feature.Feature;
import org.sonatype.licensing.trial.TrialLicenseParam;

public interface TrialLicenseManager {
    public LicenseKey createLicense(TrialLicenseParam var1, LicenseKeyRequest var2) throws LicensingException;

    public LicenseKey installLicense(TrialLicenseParam var1, File var2) throws LicensingException;

    public void uninstallLicense(TrialLicenseParam var1) throws LicensingException;

    public LicenseKey verifyLicense(TrialLicenseParam var1) throws LicensingException;

    public LicenseKey verifyLicense(TrialLicenseParam var1, File var2) throws LicensingException;

    public void validateFeature(LicenseKey var1, Feature var2) throws LicensingException;
}

