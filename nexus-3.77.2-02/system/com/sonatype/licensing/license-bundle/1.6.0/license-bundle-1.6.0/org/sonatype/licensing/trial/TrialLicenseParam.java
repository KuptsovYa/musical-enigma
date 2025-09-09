/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing.trial;

import codeguard.licensing.chr;
import codeguard.licensing.eui;
import codeguard.licensing.omj;
import codeguard.licensing.zxn;
import de.schlichtherle.license.LicenseContent;
import java.util.prefs.Preferences;
import org.sonatype.licensing.CustomLicenseContent;
import org.sonatype.licensing.internal.DefaultKeyStoreParam;
import org.sonatype.licensing.trial.TrialParam;

public final class TrialLicenseParam
implements chr {
    private final String eui;
    private final Preferences mif;
    private final DefaultKeyStoreParam miu;
    private final omj bab;
    private final DefaultKeyStoreParam uwe;
    private final TrialParam uho;

    public TrialLicenseParam(String string, Preferences preferences, DefaultKeyStoreParam defaultKeyStoreParam, DefaultKeyStoreParam defaultKeyStoreParam2, TrialParam trialParam) {
        this.eui = string;
        this.mif = preferences;
        this.miu = defaultKeyStoreParam;
        this.bab = new zxn(defaultKeyStoreParam.getStorePwd() != null ? defaultKeyStoreParam.getStorePwd() : defaultKeyStoreParam.getKeyPwd());
        this.uwe = defaultKeyStoreParam2;
        this.uho = trialParam;
    }

    @Override
    public omj getCipherParam() {
        return this.bab;
    }

    @Override
    public eui getKeyStoreParam() {
        return this.miu;
    }

    @Override
    public Preferences getPreferences() {
        return this.mif;
    }

    @Override
    public String getSubject() {
        return this.eui;
    }

    @Override
    public LicenseContent createFTPLicenseContent() {
        return this.uho.createTrialLicenseContent();
    }

    @Override
    public void ftpGranted(LicenseContent licenseContent) {
        CustomLicenseContent customLicenseContent = null;
        customLicenseContent = licenseContent instanceof CustomLicenseContent ? (CustomLicenseContent)licenseContent : new CustomLicenseContent(licenseContent);
        this.uho.trialGranted(customLicenseContent);
    }

    @Override
    public int getFTPDays() {
        return this.uho.getDaysForTrial();
    }

    @Override
    public eui getFTPKeyStoreParam() {
        return this.uwe;
    }

    @Override
    public boolean isFTPEligible() {
        return this.uho.isEligibleForTrial();
    }

    @Override
    public void removeFTPEligibility() {
        this.uho.removeTrialEligibility();
    }
}

