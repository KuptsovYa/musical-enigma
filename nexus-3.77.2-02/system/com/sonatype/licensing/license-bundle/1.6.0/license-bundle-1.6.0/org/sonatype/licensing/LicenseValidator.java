/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing;

import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseContentException;
import de.schlichtherle.license.LicenseParam;

public interface LicenseValidator {
    public void validate(LicenseContent var1, LicenseParam var2) throws LicenseContentException;
}

