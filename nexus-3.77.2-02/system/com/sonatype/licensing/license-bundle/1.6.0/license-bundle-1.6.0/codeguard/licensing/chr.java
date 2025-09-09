/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import codeguard.licensing.eui;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseParam;

public interface chr
extends LicenseParam {
    public eui getFTPKeyStoreParam();

    public int getFTPDays();

    public boolean isFTPEligible();

    public LicenseContent createFTPLicenseContent();

    public void removeFTPEligibility();

    public void ftpGranted(LicenseContent var1);
}

