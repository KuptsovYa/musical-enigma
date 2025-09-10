/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.coreui.internal.node;

public class NodeInfoXO {
    private String name;
    private Boolean local;
    private String displayName;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getLocal() {
        return this.local;
    }

    public void setLocal(Boolean local) {
        this.local = local;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String toString() {
        return "NodeInfoXO(name=" + this.name + ", local=" + this.local + ", displayName=" + this.displayName + ")";
    }
}

