/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.coreui.internal.blobstore;

public class BlobStoreUsageUIResponse {
    private final long repositoryUsage;
    private final long blobStoreUsage;

    public BlobStoreUsageUIResponse(long repositoryUsage, long blobStoreUsage) {
        this.repositoryUsage = repositoryUsage;
        this.blobStoreUsage = blobStoreUsage;
    }

    public long getRepositoryUsage() {
        return this.repositoryUsage;
    }

    public long getBlobStoreUsage() {
        return this.blobStoreUsage;
    }
}

