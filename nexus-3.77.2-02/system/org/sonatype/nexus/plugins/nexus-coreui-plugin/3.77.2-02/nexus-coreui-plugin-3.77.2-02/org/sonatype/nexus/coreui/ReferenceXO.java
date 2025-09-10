/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.NotBlank
 */
package org.sonatype.nexus.coreui;

import java.util.Objects;
import javax.validation.constraints.NotBlank;

public class ReferenceXO {
    @NotBlank
    private String id;
    @NotBlank
    private String name;

    public ReferenceXO() {
    }

    public ReferenceXO(String id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ReferenceXO other = (ReferenceXO)o;
        return Objects.equals(this.id, other.id) && Objects.equals(this.name, other.name);
    }

    public int hashCode() {
        return Objects.hash(this.id, this.name);
    }

    public String toString() {
        return "ReferenceXO{id='" + this.id + "', name='" + this.name + "'}";
    }
}

