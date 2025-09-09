/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing.feature;

import org.sonatype.licensing.LicenseKey;
import org.sonatype.licensing.LicensingException;
import org.sonatype.licensing.feature.Feature;

public interface FeatureValidator {
    public boolean isValid(Feature var1, LicenseKey var2);

    public void validate(Feature var1, LicenseKey var2) throws LicensingException;
}

