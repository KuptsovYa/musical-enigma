/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.coreui;

public class AnonymousSettingsXO {
    private Boolean enabled;
    private String userId;
    private String realmName;

    public Boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealmName() {
        return this.realmName;
    }

    public void setRealmName(String realmName) {
        this.realmName = realmName;
    }

    public String toString() {
        return "AnonymousSettingsXO(enabled:" + this.enabled + ", userId:" + this.userId + ", realmName:" + this.realmName + ")";
    }
}

