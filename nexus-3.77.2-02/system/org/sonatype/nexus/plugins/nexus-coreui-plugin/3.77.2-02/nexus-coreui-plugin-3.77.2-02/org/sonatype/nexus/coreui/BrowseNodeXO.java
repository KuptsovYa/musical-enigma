/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.NotBlank
 */
package org.sonatype.nexus.coreui;

import java.util.Objects;
import javax.validation.constraints.NotBlank;

public class BrowseNodeXO {
    @NotBlank
    private String id;
    @NotBlank
    private String text;
    @NotBlank
    private String type;
    private boolean leaf;
    private String componentId;
    private String assetId;
    private String packageUrl;

    public String getId() {
        return this.id;
    }

    public BrowseNodeXO withId(String id) {
        this.id = id;
        return this;
    }

    public String getText() {
        return this.text;
    }

    public BrowseNodeXO withText(String text) {
        this.text = text;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public BrowseNodeXO withType(String type) {
        this.type = type;
        return this;
    }

    public boolean isLeaf() {
        return this.leaf;
    }

    public BrowseNodeXO withLeaf(boolean leaf) {
        this.leaf = leaf;
        return this;
    }

    public String getComponentId() {
        return this.componentId;
    }

    public BrowseNodeXO withComponentId(String componentId) {
        this.componentId = componentId;
        return this;
    }

    public String getAssetId() {
        return this.assetId;
    }

    public BrowseNodeXO withAssetId(String assetId) {
        this.assetId = assetId;
        return this;
    }

    public String getPackageUrl() {
        return this.packageUrl;
    }

    public BrowseNodeXO withPackageUrl(String packageUrl) {
        this.packageUrl = packageUrl;
        return this;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        BrowseNodeXO other = (BrowseNodeXO)o;
        return Objects.equals(this.id, other.id);
    }

    public int hashCode() {
        return Objects.hash(this.id);
    }

    public String toString() {
        return "BrowseNodeXO{id='" + this.id + "', text='" + this.text + "', type='" + this.type + "', leaf=" + this.leaf + ", componentId='" + this.componentId + "', assetId='" + this.assetId + "', packageUrl='" + this.packageUrl + "'}";
    }
}

