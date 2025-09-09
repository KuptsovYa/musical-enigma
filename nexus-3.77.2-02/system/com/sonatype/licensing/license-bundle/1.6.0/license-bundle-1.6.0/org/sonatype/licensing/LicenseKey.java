/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing;

import org.sonatype.licensing.CustomLicenseContent;
import org.sonatype.licensing.License;

public interface LicenseKey
extends License {
    public void populateFromLicenseContent(CustomLicenseContent var1);
}

