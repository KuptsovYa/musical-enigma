/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import codeguard.licensing.eui;
import codeguard.licensing.omj;
import de.schlichtherle.license.LicenseParam;
import java.util.prefs.Preferences;

public class rkn
implements LicenseParam {
    private final String eui;
    private final Preferences mif;
    private final eui bao;
    private final omj bab;

    public rkn(String string, Preferences preferences, eui eui2, omj omj2) {
        this.eui = string;
        this.mif = preferences;
        this.bao = eui2;
        this.bab = omj2;
    }

    @Override
    public String getSubject() {
        return this.eui;
    }

    @Override
    public Preferences getPreferences() {
        return this.mif;
    }

    @Override
    public eui getKeyStoreParam() {
        return this.bao;
    }

    @Override
    public omj getCipherParam() {
        return this.bab;
    }
}

