/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.coreui;

public class TreeStoreLoadParameters {
    private String node;
    private String repositoryName;
    private String filter;

    public String getNode() {
        return this.node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getRepositoryName() {
        return this.repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getFilter() {
        return this.filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String toString() {
        return "TreeStoreLoadParameters{node=" + this.node + "repositoryName=" + this.repositoryName + "filter=" + this.filter + "}";
    }
}

