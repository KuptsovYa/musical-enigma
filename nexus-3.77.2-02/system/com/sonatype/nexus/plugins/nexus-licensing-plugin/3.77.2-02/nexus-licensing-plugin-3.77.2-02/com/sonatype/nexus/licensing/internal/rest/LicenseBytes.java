/*
 * Decompiled with CFR 0.152.
 */
package com.sonatype.nexus.licensing.internal.rest;

public class LicenseBytes {
    private final byte[] licenseBytes;

    public LicenseBytes(byte[] licenseBytes) {
        this.licenseBytes = licenseBytes;
    }

    public byte[] getBytes() {
        return this.licenseBytes;
    }
}

