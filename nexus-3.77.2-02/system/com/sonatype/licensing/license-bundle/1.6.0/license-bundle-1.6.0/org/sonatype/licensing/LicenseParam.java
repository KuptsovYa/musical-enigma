/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing;

import codeguard.licensing.eui;
import codeguard.licensing.omj;
import codeguard.licensing.zxn;
import java.util.prefs.Preferences;
import org.sonatype.licensing.internal.DefaultKeyStoreParam;

public final class LicenseParam
implements de.schlichtherle.license.LicenseParam {
    private final String eui;
    private final Preferences mif;
    private final eui bao;
    private final omj bab;

    public LicenseParam(String string, Preferences preferences, Class<?> clazz, String string2, String string3, String string4, String string5) {
        this.eui = string;
        this.mif = preferences;
        this.bao = new DefaultKeyStoreParam(clazz, string2, string3, string4, string5);
        this.bab = new zxn(string5 != null ? string5 : string4);
    }

    @Override
    public omj getCipherParam() {
        return this.bab;
    }

    @Override
    public eui getKeyStoreParam() {
        return this.bao;
    }

    @Override
    public Preferences getPreferences() {
        return this.mif;
    }

    @Override
    public String getSubject() {
        return this.eui;
    }
}

