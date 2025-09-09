/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.licensing.PreferencesFactory
 */
package com.sonatype.nexus.licensing.ext;

import com.sonatype.nexus.licensing.ext.builder.NexusLicenseBuilder;
import java.util.prefs.Preferences;
import javax.annotation.Nullable;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.licensing.PreferencesFactory;

@Named
@Singleton
public class MultiProductPreferenceFactory
implements PreferencesFactory {
    private static final String PREFERENCE_KEY = "license";
    private static final String COMMUNITY_LICENSE_PATH = "/org/sonatype/nexus/community";
    private String product;

    public Preferences nodeForPackage(Class<?> aClass) {
        throw new UnsupportedOperationException();
    }

    public Preferences nodeForPath(String absoluteFilePath) {
        if ("PRO".equals(this.product)) {
            return Preferences.userRoot().node(NexusLicenseBuilder.PACKAGE);
        }
        if ("COMMUNITY".equals(this.product)) {
            return Preferences.userRoot().node(COMMUNITY_LICENSE_PATH);
        }
        return this.extractPreferences();
    }

    public Preferences nodeForProduct(String product) {
        if ("COMMUNITY".equals(product)) {
            return Preferences.userRoot().node(COMMUNITY_LICENSE_PATH);
        }
        return this.extractPreferences();
    }

    public void setProduct(@Nullable String product) {
        this.product = product;
    }

    public Preferences extractPreferences() {
        byte[] result = Preferences.userRoot().node(NexusLicenseBuilder.PACKAGE).getByteArray(PREFERENCE_KEY, null);
        if (result != null) {
            return Preferences.userRoot().node(NexusLicenseBuilder.PACKAGE);
        }
        return Preferences.userRoot().node(COMMUNITY_LICENSE_PATH);
    }
}

