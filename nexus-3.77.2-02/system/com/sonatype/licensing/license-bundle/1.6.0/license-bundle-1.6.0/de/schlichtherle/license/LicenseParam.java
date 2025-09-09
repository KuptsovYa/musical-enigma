/*
 * Decompiled with CFR 0.152.
 */
package de.schlichtherle.license;

import codeguard.licensing.eui;
import codeguard.licensing.omj;
import java.util.prefs.Preferences;

public interface LicenseParam {
    public String getSubject();

    public Preferences getPreferences();

    public eui getKeyStoreParam();

    public omj getCipherParam();
}

