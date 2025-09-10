/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.sonatype.nexus.blobstore.quota.BlobStoreQuota
 */
package org.sonatype.nexus.coreui.internal.blobstore;

import java.util.Map;
import org.sonatype.nexus.blobstore.quota.BlobStoreQuota;

public class BlobStoreQuotaTypesUIResponse {
    private final String id;
    private final String name;

    public BlobStoreQuotaTypesUIResponse(Map.Entry<String, BlobStoreQuota> entry) {
        this.id = entry.getKey();
        this.name = entry.getValue().getDisplayName();
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}

