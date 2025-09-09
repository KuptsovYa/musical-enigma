/*
 * Decompiled with CFR 0.152.
 */
package com.sonatype.nexus.licensing.ext;

import com.sonatype.nexus.licensing.ext.LicenseChangedEvent;
import com.sonatype.nexus.licensing.ext.LicenseSource;

public class LicenseChangedWrapperEvent {
    private final LicenseChangedEvent licenseChangedEvent;
    private final LicenseSource licenseSource;

    public LicenseChangedWrapperEvent(LicenseChangedEvent licenseChangedEvent, LicenseSource licenseSource) {
        this.licenseChangedEvent = licenseChangedEvent;
        this.licenseSource = licenseSource;
    }

    public LicenseChangedEvent getLicenseChangedEvent() {
        return this.licenseChangedEvent;
    }

    public LicenseSource getLicenseSource() {
        return this.licenseSource;
    }
}

