/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing;

import de.schlichtherle.license.LicenseContent;

public class CustomLicenseContent
extends LicenseContent {
    private static final long vep = 2744561669396670639L;

    public CustomLicenseContent() {
    }

    public CustomLicenseContent(LicenseContent licenseContent) {
        this.setConsumerAmount(licenseContent.getConsumerAmount());
        this.setConsumerType(licenseContent.getConsumerType());
        this.setExtra(licenseContent.getExtra());
        this.setHolder(licenseContent.getHolder());
        this.setInfo(licenseContent.getInfo());
        this.setIssued(licenseContent.getIssued());
        this.setIssuer(licenseContent.getIssuer());
        this.setNotAfter(licenseContent.getNotAfter());
        this.setNotBefore(licenseContent.getNotBefore());
        this.setSubject(licenseContent.getSubject());
    }

    public LicenseContent getExtraLicenseContent() {
        return null;
    }
}

