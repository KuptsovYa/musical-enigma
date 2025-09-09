/*
 * Decompiled with CFR 0.152.
 */
package com.sonatype.nexus.licensing.ext;

public enum LicenseSource {
    UI("User Interface"),
    SYSTEM_PROPERTY("System Property"),
    ENVIRONMENT_VARIABLE("Environment Variable"),
    CACHE("Cache"),
    API("API"),
    REGISTER("Registering");

    private final String description;

    private LicenseSource(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}

