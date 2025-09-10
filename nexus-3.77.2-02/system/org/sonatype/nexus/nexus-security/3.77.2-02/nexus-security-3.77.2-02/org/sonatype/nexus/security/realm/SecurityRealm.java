/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.realm;

public class SecurityRealm {
    private String id;
    private String name;

    public SecurityRealm(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "id=" + this.id + " name=" + this.name;
    }
}

