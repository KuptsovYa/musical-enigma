/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.NotBlank
 */
package org.sonatype.nexus.coreui;

import javax.validation.constraints.NotBlank;

public class BlobStoreQuotaTypeXO {
    @NotBlank
    private String id;
    @NotBlank
    private String name;

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

    public String toString() {
        return "BlobStoreQuotaTypeXO{id='" + this.id + "', name='" + this.name + "'}";
    }
}

