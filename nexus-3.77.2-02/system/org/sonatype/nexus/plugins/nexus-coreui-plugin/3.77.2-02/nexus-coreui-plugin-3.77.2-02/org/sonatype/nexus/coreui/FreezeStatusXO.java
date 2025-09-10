/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.coreui;

public class FreezeStatusXO {
    private boolean frozen;

    public boolean isFrozen() {
        return this.frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public String toString() {
        return "FreezeStatusXO{frozen=" + this.frozen + "}";
    }
}

