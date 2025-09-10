/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonSetter
 *  javax.validation.constraints.Min
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.Size
 *  org.sonatype.nexus.validation.group.Create
 */
package org.sonatype.nexus.coreui;

import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.Map;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import org.sonatype.nexus.coreui.UniqueBlobStoreName;
import org.sonatype.nexus.validation.group.Create;

public class BlobStoreXO {
    @NotEmpty
    @UniqueBlobStoreName(groups={Create.class})
    @Size(min=1, max=255)
    private @NotEmpty @Size(min=1, max=255) String name;
    @NotEmpty
    private String type;
    private boolean isQuotaEnabled;
    private String quotaType;
    @Min(value=0L)
    private @Min(value=0L) Long quotaLimit;
    @NotEmpty
    private Map<String, Map<String, Object>> attributes;
    @Min(value=0L)
    private @Min(value=0L) long blobCount;
    @Min(value=0L)
    private @Min(value=0L) long totalSize;
    @Min(value=0L)
    private @Min(value=0L) long availableSpace;
    @Min(value=0L)
    private @Min(value=0L) long repositoryUseCount;
    private boolean unlimited;
    private boolean unavailable;
    @Min(value=0L)
    private @Min(value=0L) long blobStoreUseCount;
    private boolean inUse;
    private boolean convertable;
    private int taskUseCount;
    private String groupName;

    public Map<String, Map<String, Object>> getAttributes() {
        return this.attributes;
    }

    public long getAvailableSpace() {
        return this.availableSpace;
    }

    public long getBlobCount() {
        return this.blobCount;
    }

    public long getBlobStoreUseCount() {
        return this.blobStoreUseCount;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public boolean isQuotaEnabled() {
        return this.isQuotaEnabled;
    }

    public String getName() {
        return this.name;
    }

    public Long getQuotaLimit() {
        return this.quotaLimit;
    }

    public String getQuotaType() {
        return this.quotaType;
    }

    public long getRepositoryUseCount() {
        return this.repositoryUseCount;
    }

    public int getTaskUseCount() {
        return this.taskUseCount;
    }

    public long getTotalSize() {
        return this.totalSize;
    }

    public String getType() {
        return this.type;
    }

    public boolean isConvertable() {
        return this.convertable;
    }

    public boolean isInUse() {
        return this.inUse;
    }

    public boolean isUnavailable() {
        return this.unavailable;
    }

    public boolean isUnlimited() {
        return this.unlimited;
    }

    @JsonSetter(value="attributes")
    public BlobStoreXO withAttributes(Map<String, Map<String, Object>> attributes) {
        this.attributes = attributes;
        return this;
    }

    @JsonSetter(value="availableSpace")
    public BlobStoreXO withAvailableSpace(long availableSpace) {
        this.availableSpace = availableSpace;
        return this;
    }

    @JsonSetter(value="blobCount")
    public BlobStoreXO withBlobCount(long blobCount) {
        this.blobCount = blobCount;
        return this;
    }

    @JsonSetter(value="blobStoreUseCount")
    public BlobStoreXO withBlobStoreUseCount(long blobStoreUseCount) {
        this.blobStoreUseCount = blobStoreUseCount;
        return this;
    }

    @JsonSetter(value="convertable")
    public BlobStoreXO withConvertable(boolean convertable) {
        this.convertable = convertable;
        return this;
    }

    @JsonSetter(value="groupName")
    public BlobStoreXO withGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    @JsonSetter(value="inUse")
    public BlobStoreXO withInUse(boolean inUse) {
        this.inUse = inUse;
        return this;
    }

    @JsonSetter(value="isQuotaEnabled")
    public BlobStoreXO withIsQuotaEnabled(boolean isQuotaEnabled) {
        this.isQuotaEnabled = isQuotaEnabled;
        return this;
    }

    @JsonSetter(value="isQuotaEnabled")
    public BlobStoreXO withIsQuotaEnabled(String isQuotaEnabled) {
        this.isQuotaEnabled = isQuotaEnabled != null && ("true".equalsIgnoreCase(isQuotaEnabled) || "on".equalsIgnoreCase(isQuotaEnabled) || "1".equalsIgnoreCase(isQuotaEnabled));
        return this;
    }

    @JsonSetter(value="name")
    public BlobStoreXO withName(String name) {
        this.name = name;
        return this;
    }

    @JsonSetter(value="quotaLimit")
    public BlobStoreXO withQuotaLimit(Long quotaLimit) {
        this.quotaLimit = quotaLimit;
        return this;
    }

    @JsonSetter(value="quotaType")
    public BlobStoreXO withQuotaType(String quotaType) {
        this.quotaType = quotaType;
        return this;
    }

    @JsonSetter(value="repositoryUseCount")
    public BlobStoreXO withRepositoryUseCount(long repositoryUseCount) {
        this.repositoryUseCount = repositoryUseCount;
        return this;
    }

    @JsonSetter(value="taskUseCount")
    public BlobStoreXO withTaskUseCount(int taskUseCount) {
        this.taskUseCount = taskUseCount;
        return this;
    }

    @JsonSetter(value="totalSize")
    public BlobStoreXO withTotalSize(long totalSize) {
        this.totalSize = totalSize;
        return this;
    }

    @JsonSetter(value="type")
    public BlobStoreXO withType(String type) {
        this.type = type;
        return this;
    }

    @JsonSetter(value="unavailable")
    public BlobStoreXO withUnavailable(boolean unavailable) {
        this.unavailable = unavailable;
        return this;
    }

    @JsonSetter(value="unlimited")
    public BlobStoreXO withUnlimited(boolean unlimited) {
        this.unlimited = unlimited;
        return this;
    }
}

