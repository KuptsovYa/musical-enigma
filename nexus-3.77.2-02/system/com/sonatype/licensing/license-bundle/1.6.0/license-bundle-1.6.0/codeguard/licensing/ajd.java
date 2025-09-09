/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import codeguard.licensing.baj;
import codeguard.licensing.chr;
import codeguard.licensing.eui;
import codeguard.licensing.omj;
import codeguard.licensing.qyf;
import codeguard.licensing.rkn;
import de.schlichtherle.license.LicenseContent;
import java.util.prefs.Preferences;

public abstract class ajd
extends rkn
implements chr {
    private static final String mcv = baj.omj(new qyf(new long[]{3518397844622038312L, 5283581152211342346L, -5949173903170142050L, 5400406727937919437L}).toString());
    private final eui yhl;
    private final int jfe;

    protected ajd(String string, Preferences preferences, eui eui2, eui eui3, int n, omj omj2) {
        super(string, preferences, eui2, omj2);
        this.yhl = eui3;
        this.jfe = n;
    }

    @Override
    public LicenseContent createFTPLicenseContent() {
        LicenseContent licenseContent = new LicenseContent();
        licenseContent.setInfo(mcv);
        return licenseContent;
    }

    @Override
    public eui getFTPKeyStoreParam() {
        return this.yhl;
    }

    @Override
    public int getFTPDays() {
        return this.jfe;
    }
}

