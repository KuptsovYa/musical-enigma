/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Named
 */
package org.sonatype.licensing.internal;

import javax.inject.Named;
import org.sonatype.licensing.LicenseKey;
import org.sonatype.licensing.LicensingException;
import org.sonatype.licensing.feature.Feature;
import org.sonatype.licensing.feature.FeatureValidator;

@Named(value="licensing.default")
public class DefaultFeatureValidator
implements FeatureValidator {
    @Override
    public boolean isValid(Feature feature, LicenseKey licenseKey) {
        return licenseKey.isEvaluation() || licenseKey.getFeatureSet().hasFeature(feature);
    }

    @Override
    public void validate(Feature feature, LicenseKey licenseKey) throws LicensingException {
        if (!this.isValid(feature, licenseKey)) {
            throw new LicensingException("License does not permit use of feature '" + feature.getId() + "'");
        }
    }
}

