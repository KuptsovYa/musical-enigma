/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.NotNull
 */
package org.sonatype.nexus.coreui;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.sonatype.nexus.coreui.FormFieldXO;

public class TaskTypeXO {
    @NotBlank
    private String id;
    @NotBlank
    private String name;
    @NotNull
    private Boolean exposed;
    @NotNull
    private Boolean concurrentRun;
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

    public Boolean getExposed() {
        return this.exposed;
    }

    public void setExposed(Boolean exposed) {
        this.exposed = exposed;
    }

    public Boolean getConcurrentRun() {
        return this.concurrentRun;
    }

    public void setConcurrentRun(Boolean concurrentRun) {
        this.concurrentRun = concurrentRun;
    }

    public List<FormFieldXO> getFormFields() {
        return this.formFields;
    }

    public void setFormFields(List<FormFieldXO> formFields) {
        this.formFields = formFields;
    }

    public String toString() {
        return "TaskTypeXO{id='" + this.id + "', name='" + this.name + "', exposed=" + this.exposed + ", concurrentRun=" + this.concurrentRun + ", formFields=" + this.formFields + "}";
    }
}

