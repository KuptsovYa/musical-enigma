/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.NotEmpty
 */
package org.sonatype.nexus.coreui;

import javax.validation.constraints.NotEmpty;

public class RepositoryStatusXO {
    @NotEmpty
    private String repositoryName;
    private boolean online;
    private String description;
    private String reason;

    public String getRepositoryName() {
        return this.repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public boolean isOnline() {
        return this.online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String toString() {
        return "RepositoryStatusXO{repositoryName='" + this.repositoryName + "', online=" + this.online + ", description='" + this.description + "', reason='" + this.reason + "'}";
    }
}

