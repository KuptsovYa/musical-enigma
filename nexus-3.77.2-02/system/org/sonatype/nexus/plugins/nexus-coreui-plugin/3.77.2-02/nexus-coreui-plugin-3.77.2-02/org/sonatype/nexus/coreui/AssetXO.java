/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.NotEmpty
 */
package org.sonatype.nexus.coreui;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import javax.validation.constraints.NotEmpty;

public class AssetXO {
    @NotEmpty
    private String id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String format;
    @NotEmpty
    private String contentType;
    @NotEmpty
    private long size;
    @NotEmpty
    private String repositoryName;
    @NotEmpty
    private String containingRepositoryName;
    private Date blobCreated;
    private Date blobUpdated;
    private Date lastDownloaded;
    @NotEmpty
    private String blobRef;
    private String componentId;
    private String createdBy;
    private String createdByIp;
    @NotEmpty
    private Map<String, Object> attributes;

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

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getRepositoryName() {
        return this.repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getContainingRepositoryName() {
        return this.containingRepositoryName;
    }

    public void setContainingRepositoryName(String containingRepositoryName) {
        this.containingRepositoryName = containingRepositoryName;
    }

    public Date getBlobCreated() {
        return this.blobCreated;
    }

    public void setBlobCreated(Date blobCreated) {
        this.blobCreated = blobCreated;
    }

    public Date getBlobUpdated() {
        return this.blobUpdated;
    }

    public void setBlobUpdated(Date blobUpdated) {
        this.blobUpdated = blobUpdated;
    }

    public Date getLastDownloaded() {
        return this.lastDownloaded;
    }

    public void setLastDownloaded(Date lastDownloaded) {
        this.lastDownloaded = lastDownloaded;
    }

    public String getBlobRef() {
        return this.blobRef;
    }

    public void setBlobRef(String blobRef) {
        this.blobRef = blobRef;
    }

    public String getComponentId() {
        return this.componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByIp() {
        return this.createdByIp;
    }

    public void setCreatedByIp(String createdByIp) {
        this.createdByIp = createdByIp;
    }

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        AssetXO assetXO = (AssetXO)o;
        return Objects.equals(this.id, assetXO.id);
    }

    public int hashCode() {
        return Objects.hash(this.id);
    }

    public String toString() {
        return "AssetXO{id='" + this.id + "', name='" + this.name + "', format='" + this.format + "', contentType='" + this.contentType + "', size=" + this.size + ", repositoryName='" + this.repositoryName + "', containingRepositoryName='" + this.containingRepositoryName + "', blobCreated=" + this.blobCreated + ", blobUpdated=" + this.blobUpdated + ", lastDownloaded=" + this.lastDownloaded + ", blobRef='" + this.blobRef + "', componentId='" + this.componentId + "', createdBy='" + this.createdBy + "', createdByIp='" + this.createdByIp + "', attributes=" + this.attributes + "}";
    }
}

