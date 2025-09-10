/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.NotEmpty
 *  org.sonatype.nexus.validation.group.Update
 */
package org.sonatype.nexus.coreui.internal.capability;

import javax.validation.constraints.NotEmpty;
import org.sonatype.nexus.validation.group.Update;

public class CapabilityNotesXO {
    @NotEmpty(groups={Update.class})
    private String id;
    private String notes;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String toString() {
        return "CapabilityNotesXO(id:" + this.id + ", notes:" + this.notes + ")";
    }
}

