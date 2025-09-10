/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.annotation.Nullable
 *  org.sonatype.nexus.blobstore.api.BlobStoreConfiguration
 *  org.sonatype.nexus.blobstore.api.BlobStoreMetrics
 */
package org.sonatype.nexus.coreui.internal.blobstore;

import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import org.sonatype.nexus.blobstore.api.BlobStoreConfiguration;
import org.sonatype.nexus.blobstore.api.BlobStoreMetrics;

public class BlobStoreUIResponse {
    private final String name;
    private final String typeId;
    private final String typeName;
    private final String path;
    private final boolean unavailable;
    private final long blobCount;
    private final long totalSizeInBytes;
    private final long availableSpaceInBytes;
    private final boolean unlimited;

    public BlobStoreUIResponse(String typeId, BlobStoreConfiguration configuration, @Nullable BlobStoreMetrics metrics, String path) {
        if (metrics != null) {
            this.unavailable = metrics.isUnavailable();
            this.blobCount = metrics.getBlobCount();
            this.totalSizeInBytes = metrics.getTotalSize();
            this.availableSpaceInBytes = metrics.getAvailableSpace();
            this.unlimited = metrics.isUnlimited();
        } else {
            this.unavailable = true;
            this.blobCount = 0L;
            this.totalSizeInBytes = 0L;
            this.availableSpaceInBytes = 0L;
            this.unlimited = false;
        }
        this.name = (String)Preconditions.checkNotNull((Object)configuration.getName());
        this.typeId = (String)Preconditions.checkNotNull((Object)typeId);
        this.typeName = configuration.getType();
        this.path = (String)Preconditions.checkNotNull((Object)path);
    }

    public String getName() {
        return this.name;
    }

    public String getTypeId() {
        return this.typeId;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public String getPath() {
        return this.path;
    }

    public boolean isUnavailable() {
        return this.unavailable;
    }

    public long getBlobCount() {
        return this.blobCount;
    }

    public long getTotalSizeInBytes() {
        return this.totalSizeInBytes;
    }

    public long getAvailableSpaceInBytes() {
        return this.availableSpaceInBytes;
    }

    public boolean isUnlimited() {
        return this.unlimited;
    }
}

