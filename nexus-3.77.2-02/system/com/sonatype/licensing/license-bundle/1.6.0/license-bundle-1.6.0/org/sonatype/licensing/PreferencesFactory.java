/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing;

import java.util.prefs.Preferences;

public interface PreferencesFactory {
    public Preferences nodeForPackage(Class<?> var1);

    public Preferences nodeForPath(String var1);
}

