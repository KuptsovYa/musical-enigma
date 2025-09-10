/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.coreui;

import java.util.List;

public class ProprietaryRepositoriesXO {
    private List<String> enabledRepositories;

    public List<String> getEnabledRepositories() {
        return this.enabledRepositories;
    }

    public void setEnabledRepositories(List<String> enabledRepositories) {
        this.enabledRepositories = enabledRepositories;
    }

    public String toString() {
        return "ProprietaryRepositoriesXO{enabledRepositories=" + this.enabledRepositories + "}";
    }
}

