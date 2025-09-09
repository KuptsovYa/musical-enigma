/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing;

import de.schlichtherle.license.LicenseContent;

public class LicenseContentException
extends de.schlichtherle.license.LicenseContentException {
    private final LicenseContent tex;

    public LicenseContentException(String string, LicenseContent licenseContent) {
        super(string);
        this.tex = licenseContent;
    }

    public LicenseContent getLicenseContent() {
        return this.tex;
    }
}

