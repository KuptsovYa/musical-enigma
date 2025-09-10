/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.NotEmpty
 */
package org.sonatype.nexus.coreui.internal.capability;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import org.sonatype.nexus.coreui.FormFieldXO;

public class CapabilityTypeXO {
    @NotEmpty
    private String id;
    @NotEmpty
    private String name;
    private String about;
    private List<FormFieldXO> formFields;

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

    public String getAbout() {
        return this.about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public List<FormFieldXO> getFormFields() {
        return this.formFields;
    }

    public void setFormFields(List<FormFieldXO> formFields) {
        this.formFields = formFields;
    }

    public String toString() {
        return "CapabilityTypeXO(id:" + this.id + ", name:" + this.name + ", about:" + this.about + ", formFields:" + this.formFields + ")";
    }
}

