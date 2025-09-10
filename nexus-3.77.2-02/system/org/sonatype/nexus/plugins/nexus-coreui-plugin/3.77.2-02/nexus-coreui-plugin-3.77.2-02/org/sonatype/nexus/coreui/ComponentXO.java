/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.NotBlank
 */
package org.sonatype.nexus.coreui;

import java.util.Objects;
import javax.validation.constraints.NotBlank;

public class ComponentXO {
    @NotBlank
    private String id;
    @NotBlank
    private String repositoryName;
    @NotBlank
    private String group;
    @NotBlank
    private String name;
    @NotBlank
    private String version;
    @NotBlank
    private String format;
    @NotBlank
    private String lastBlobUpdated;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRepositoryName() {
        return this.repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getLastBlobUpdated() {
        return this.lastBlobUpdated;
    }

    public void setLastBlobUpdated(String lastBlobUpdated) {
        this.lastBlobUpdated = lastBlobUpdated;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ComponentXO that = (ComponentXO)o;
        return Objects.equals(this.id, that.id);
    }

    public int hashCode() {
        return Objects.hash(this.id);
    }

    public String toString() {
        return "ComponentXO{id='" + this.id + "', repositoryName='" + this.repositoryName + "', group='" + this.group + "', name='" + this.name + "', version='" + this.version + "', format='" + this.format + "', lastBlobUpdated='" + this.lastBlobUpdated + "'}";
    }
}

