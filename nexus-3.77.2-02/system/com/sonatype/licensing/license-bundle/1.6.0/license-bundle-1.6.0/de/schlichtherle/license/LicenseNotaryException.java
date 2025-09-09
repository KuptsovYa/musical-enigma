/*
 * Decompiled with CFR 0.152.
 */
package de.schlichtherle.license;

import codeguard.licensing.bxh;
import java.security.GeneralSecurityException;

public class LicenseNotaryException
extends GeneralSecurityException {
    private static final long vep = 1L;
    private String clk;

    public LicenseNotaryException(String string, String string2) {
        super(string);
        this.clk = string2;
    }

    @Override
    public String getLocalizedMessage() {
        return bxh.itm(super.getMessage(), this.clk);
    }
}

