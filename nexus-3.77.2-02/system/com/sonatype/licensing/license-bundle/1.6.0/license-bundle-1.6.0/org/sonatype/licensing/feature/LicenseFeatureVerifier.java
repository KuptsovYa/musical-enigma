/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing.feature;

import org.sonatype.licensing.LicensingException;
import org.sonatype.licensing.feature.Feature;

public interface LicenseFeatureVerifier {
    public void verifyLicenseAndFeature(Feature var1) throws LicensingException;

    public boolean verify(Feature var1);
}

