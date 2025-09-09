/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Named
 *  javax.inject.Singleton
 */
package com.sonatype.nexus.licensing.ext;

import java.io.File;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class LicenseEnvironment {
    private static final String ENV_LICENSE = "NEXUS_LICENSE_FILE";

    public File getLicenseFile() {
        return System.getenv(ENV_LICENSE) != null ? new File(System.getenv(ENV_LICENSE)).getAbsoluteFile() : null;
    }
}

