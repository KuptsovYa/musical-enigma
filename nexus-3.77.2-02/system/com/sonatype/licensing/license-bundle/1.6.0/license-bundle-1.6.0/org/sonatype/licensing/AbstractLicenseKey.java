/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 */
package org.sonatype.licensing;

import codeguard.licensing.rnn;
import java.util.Map;
import javax.inject.Inject;
import org.sonatype.licensing.CustomLicenseContent;
import org.sonatype.licensing.LicenseKey;
import org.sonatype.licensing.feature.Feature;
import org.sonatype.licensing.feature.Features;
import org.sonatype.licensing.util.LicensingUtil;

public abstract class AbstractLicenseKey
extends rnn
implements LicenseKey {
    protected Map<String, Feature> availableFeatures;
    private final Features qei;

    @Inject
    public AbstractLicenseKey(Features features) {
        this.qei = features;
    }

    @Override
    public void populateFromLicenseContent(CustomLicenseContent customLicenseContent) {
        String string = customLicenseContent.getHolder().getName();
        String[] stringArray = LicensingUtil.getDNParts(string).toArray(new String[0]);
        this.setContactName(this.clk(stringArray[0]));
        this.setContactCompany(this.clk(stringArray[2]));
        this.setContactEmailAddress(this.clk(stringArray[3]));
        this.setContactTelephone(this.clk(stringArray[4]));
        this.setContactCountry(this.clk(stringArray[5]));
        this.setExpirationDate(customLicenseContent.getNotAfter());
        this.setEffectiveDate(customLicenseContent.getNotBefore());
        this.parseExtraContent(customLicenseContent.getExtra().toString());
    }

    private String clk(String string) {
        String[] stringArray = string.split("=");
        if (stringArray.length < 2) {
            return null;
        }
        return stringArray[1];
    }

    protected Map<String, Feature> getAvailableFeatures() {
        if (this.availableFeatures == null) {
            this.availableFeatures = this.qei.getAvailableFeatures();
        }
        return this.availableFeatures;
    }

    protected abstract void parseExtraContent(String var1);
}

