/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.sonatype.goodies.lifecycle.Lifecycle
 */
package org.sonatype.nexus.security.realm;

import java.util.List;
import org.sonatype.goodies.lifecycle.Lifecycle;
import org.sonatype.nexus.security.realm.SecurityRealm;

public interface RealmManager
extends Lifecycle {
    public boolean isRealmEnabled(String var1);

    public void enableRealm(String var1, boolean var2);

    public void enableRealm(String var1);

    public void enableRealm(String var1, int var2);

    public void disableRealm(String var1);

    public List<SecurityRealm> getAvailableRealms();

    public List<SecurityRealm> getAvailableRealms(boolean var1);

    public List<String> getConfiguredRealmIds();

    public List<String> getConfiguredRealmIds(boolean var1);

    public void setConfiguredRealmIds(List<String> var1);
}

