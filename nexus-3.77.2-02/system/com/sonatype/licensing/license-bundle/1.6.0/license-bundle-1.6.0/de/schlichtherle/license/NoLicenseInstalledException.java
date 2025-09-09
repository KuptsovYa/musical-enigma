/*
 * Decompiled with CFR 0.152.
 */
package de.schlichtherle.license;

import codeguard.licensing.bxh;
import codeguard.licensing.qyf;

public class NoLicenseInstalledException
extends Exception {
    private static final long vep = 1L;
    private static final String fyi = new qyf(new long[]{5636850220590995934L, -798521115123526970L, 3054112192777193179L, 881750348384376277L}).toString();

    public NoLicenseInstalledException(String string) {
        super(string);
    }

    @Override
    public String getLocalizedMessage() {
        return bxh.itm(fyi, super.getMessage());
    }
}

