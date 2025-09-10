/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.NotBlank
 */
package org.sonatype.nexus.coreui;

import java.util.List;
import javax.validation.constraints.NotBlank;
import org.sonatype.nexus.coreui.FormFieldXO;

public class BlobStoreTypeXO {
    @NotBlank
    private String id;
    @NotBlank
    private String name;
    private List<FormFieldXO> formFields;
    private String customFormName;
    private boolean isModifiable;
    private boolean isEnabled;
    private boolean isConnectionTestable;

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

    public List<FormFieldXO> getFormFields() {
        return this.formFields;
    }

    public void setFormFields(List<FormFieldXO> formFields) {
        this.formFields = formFields;
    }

    public String getCustomFormName() {
        return this.customFormName;
    }

    public void setCustomFormName(String customFormName) {
        this.customFormName = customFormName;
    }

    public boolean isModifiable() {
        return this.isModifiable;
    }

    public void setIsModifiable(boolean isModifiable) {
        this.isModifiable = isModifiable;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean isConnectionTestable() {
        return this.isConnectionTestable;
    }

    public void setConnectionTestable(boolean isConnectionTestable) {
        this.isConnectionTestable = isConnectionTestable;
    }

    public String toString() {
        return "BlobStoreTypeXO{id='" + this.id + "', name='" + this.name + "', formFields=" + this.formFields + ", customFormName='" + this.customFormName + "', isModifiable=" + this.isModifiable + ", isEnabled=" + this.isEnabled + ", isConnectionTestable=" + this.isConnectionTestable + "}";
    }
}

