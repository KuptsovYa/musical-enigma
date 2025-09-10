/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.coreui;

import java.util.List;

public class RealmSettingsXO {
    private List<String> realms;

    public List<String> getRealms() {
        return this.realms;
    }

    public void setRealms(List<String> realms) {
        this.realms = realms;
    }

    public String toString() {
        return "RealmSettingsXO{realms=" + this.realms + "}";
    }
}

