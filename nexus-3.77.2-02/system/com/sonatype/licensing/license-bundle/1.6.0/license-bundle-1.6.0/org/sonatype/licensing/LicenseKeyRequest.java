/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing;

import java.io.File;
import javax.security.auth.x500.X500Principal;
import org.sonatype.licensing.CustomLicenseContent;
import org.sonatype.licensing.License;

public interface LicenseKeyRequest
extends License {
    public void setLicenseKeyFile(File var1);

    public File getLicenseKeyFile();

    public CustomLicenseContent getLicenseContent();

    public X500Principal getIssuer();

    public String getExtraContent();
}

