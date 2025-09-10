/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.realm.api;

import org.sonatype.nexus.security.realm.SecurityRealm;

public class RealmApiXO {
    private String id;
    private String name;

    public RealmApiXO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static RealmApiXO from(SecurityRealm securityRealm) {
        return new RealmApiXO(securityRealm.getId(), securityRealm.getName());
    }
}

