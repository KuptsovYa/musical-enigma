/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing;

import org.sonatype.licensing.LicenseContentException;
import org.sonatype.licensing.LicenseKey;

public class LicensingException
extends RuntimeException {
    private static final long vep = -7698990696599896544L;
    private static final String vbu = "Sonatype Product Licensing Exception";
    private LicenseKey bzt = null;

    public LicensingException(String string) {
        super(string);
    }

    public LicensingException(Throwable throwable) {
        super(vbu, throwable);
    }

    public LicensingException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public LicensingException(LicenseKey licenseKey, String string, LicenseContentException licenseContentException) {
        this(string, licenseContentException);
        this.bzt = licenseKey;
    }

    public LicenseKey getKey() {
        return this.bzt;
    }
}

