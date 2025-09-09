/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing.trial;

import org.sonatype.licensing.CustomLicenseContent;

public interface TrialParam {
    public boolean isEligibleForTrial();

    public void removeTrialEligibility();

    public int getDaysForTrial();

    public CustomLicenseContent createTrialLicenseContent();

    public void trialGranted(CustomLicenseContent var1);
}

