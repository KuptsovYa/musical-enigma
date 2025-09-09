/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.licensing.CustomLicenseContent
 *  org.sonatype.licensing.LicenseKeyRequest
 *  org.sonatype.licensing.feature.Feature
 *  org.sonatype.licensing.feature.FeatureSet
 *  org.sonatype.licensing.trial.TrialParam
 *  org.sonatype.licensing.util.LicensingUtil
 *  org.sonatype.nexus.common.property.SystemPropertiesHelper
 */
package com.sonatype.nexus.licensing.ext.builder;

import com.google.common.base.Preconditions;
import com.sonatype.nexus.licensing.ext.builder.NexusLicenseBuilder;
import java.util.Map;
import java.util.prefs.Preferences;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.licensing.CustomLicenseContent;
import org.sonatype.licensing.LicenseKeyRequest;
import org.sonatype.licensing.feature.Feature;
import org.sonatype.licensing.feature.FeatureSet;
import org.sonatype.licensing.trial.TrialParam;
import org.sonatype.licensing.util.LicensingUtil;
import org.sonatype.nexus.common.property.SystemPropertiesHelper;

@Named
@Singleton
public class NexusPlexusTrialParam
extends ComponentSupport
implements TrialParam {
    private final Map<String, Feature> availableFeatures;
    private final LicenseKeyRequest licenseRequest;
    private final String autoGenLicenseProperty = LicensingUtil.unobfuscate((long[])new long[]{7448020992081988024L, -9059860507678535253L, 7722278387301246170L, 1503749209478560901L});
    private final boolean autoGenLicense = SystemPropertiesHelper.getBoolean((String)this.autoGenLicenseProperty, (boolean)false);

    @Inject
    public NexusPlexusTrialParam(Map<String, Feature> availableFeatures, LicenseKeyRequest licenseRequest) {
        this.availableFeatures = (Map)Preconditions.checkNotNull(availableFeatures);
        this.licenseRequest = (LicenseKeyRequest)Preconditions.checkNotNull((Object)licenseRequest);
    }

    public CustomLicenseContent createTrialLicenseContent() {
        this.licenseRequest.setContactCompany("");
        this.licenseRequest.setContactCountry("");
        this.licenseRequest.setContactEmailAddress("");
        this.licenseRequest.setContactName("");
        this.licenseRequest.setContactTelephone("");
        this.licenseRequest.setEvaluation(true);
        FeatureSet featureSet = new FeatureSet();
        for (Feature feature : this.availableFeatures.values()) {
            featureSet.addFeature(feature);
        }
        this.licenseRequest.setFeatureSet(featureSet);
        return this.licenseRequest.getLicenseContent();
    }

    public int getDaysForTrial() {
        return 15;
    }

    public boolean isEligibleForTrial() {
        if (this.autoGenLicense) {
            this.log.debug("Checking trial eligibility");
            return Preferences.userRoot().node(NexusLicenseBuilder.PACKAGE).getBoolean(NexusLicenseBuilder.TRIAL_PREFERENCE, true);
        }
        return false;
    }

    public void removeTrialEligibility() {
        if (this.autoGenLicense) {
            Preferences.userRoot().node(NexusLicenseBuilder.PACKAGE).putBoolean(NexusLicenseBuilder.TRIAL_PREFERENCE, false);
        }
    }

    public void trialGranted(CustomLicenseContent arg0) {
        this.log.debug("Running a trial license");
    }
}

