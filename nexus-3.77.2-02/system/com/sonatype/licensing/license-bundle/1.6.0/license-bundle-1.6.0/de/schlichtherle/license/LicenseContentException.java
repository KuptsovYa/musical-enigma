/*
 * Decompiled with CFR 0.152.
 */
package de.schlichtherle.license;

import codeguard.licensing.bxh;

public class LicenseContentException
extends Exception {
    private static final long vep = 1L;

    public LicenseContentException(String string) {
        super(string);
    }

    @Override
    public String getLocalizedMessage() {
        return bxh.omj(super.getMessage());
    }
}

