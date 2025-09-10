/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.coreui;

import org.sonatype.nexus.coreui.ReferenceXO;
import org.sonatype.nexus.coreui.RepositoryStatusXO;

public class RepositoryReferenceXO
extends ReferenceXO {
    private String type;
    private String format;
    private String versionPolicy;
    private String url;
    private String blobStoreName;
    private RepositoryStatusXO status;
    private int sortOrder = 0;

    public RepositoryReferenceXO(String id, String name, String type, String format, String versionPolicy, String url, String blobStoreName, RepositoryStatusXO status, int sortOrder) {
        this(id, name, type, format, versionPolicy, url, blobStoreName, status);
        this.sortOrder = sortOrder;
    }

    public RepositoryReferenceXO(String id, String name, String type, String format, String versionPolicy, String url, String blobStoreName, RepositoryStatusXO status) {
        this.setId(id);
        this.setName(name);
        this.type = type;
        this.format = format;
        this.versionPolicy = versionPolicy;
        this.url = url;
        this.status = status;
        this.blobStoreName = blobStoreName;
    }

    public String getType() {
        return this.type;
    }

    public String getFormat() {
        return this.format;
    }

    public String getVersionPolicy() {
        return this.versionPolicy;
    }

    public String getUrl() {
        return this.url;
    }

    public String getBlobStoreName() {
        return this.blobStoreName;
    }

    public RepositoryStatusXO getStatus() {
        return this.status;
    }

    public int getSortOrder() {
        return this.sortOrder;
    }

    @Override
    public String toString() {
        return "RepositoryReferenceXO [id=" + this.getId() + ", name=" + this.getName() + ", type=" + this.type + ", format=" + this.format + ", versionPolicy=" + this.versionPolicy + ", url=" + this.url + ", blobStoreName=" + this.blobStoreName + ", status=" + this.status + ", sortOrder=" + this.sortOrder + "]";
    }
}

