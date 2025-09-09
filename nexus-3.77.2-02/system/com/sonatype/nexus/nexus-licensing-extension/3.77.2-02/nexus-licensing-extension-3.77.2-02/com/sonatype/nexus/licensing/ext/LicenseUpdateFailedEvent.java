/*
 * Decompiled with CFR 0.152.
 */
package com.sonatype.nexus.licensing.ext;

public class LicenseUpdateFailedEvent {
    private final byte[] licenseFile;
    private final String reason;

    public LicenseUpdateFailedEvent(byte[] licenseFile, String reason) {
        this.licenseFile = licenseFile;
        this.reason = reason;
    }

    public byte[] getLicenseFile() {
        return this.licenseFile;
    }

    public String getReason() {
        return this.reason;
    }

    public String toString() {
        return String.valueOf(this.getClass().getSimpleName()) + "{" + "reason=" + this.reason + '}';
    }
}

