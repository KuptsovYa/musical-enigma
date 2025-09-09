/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Named
 */
package org.sonatype.licensing;

import java.util.prefs.Preferences;
import javax.inject.Named;
import org.sonatype.licensing.PreferencesFactory;

@Named(value="licensing.default")
public class DefaultPreferencesFactory
implements PreferencesFactory {
    @Override
    public Preferences nodeForPackage(Class<?> clazz) {
        return Preferences.userNodeForPackage(clazz);
    }

    @Override
    public Preferences nodeForPath(String string) {
        return Preferences.userRoot().node(string);
    }
}

