/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 *  org.sonatype.nexus.capability.CapabilityTypeExists
 *  org.sonatype.nexus.validation.group.Create
 *  org.sonatype.nexus.validation.group.Update
 */
package org.sonatype.nexus.coreui.internal.capability;

import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.sonatype.nexus.capability.CapabilityTypeExists;
import org.sonatype.nexus.validation.group.Create;
import org.sonatype.nexus.validation.group.Update;

public class CapabilityXO {
    @NotEmpty(groups={Update.class})
    private String id;
    @NotBlank(groups={Create.class})
    @CapabilityTypeExists(groups={Create.class})
    private String typeId;
    @NotNull
    private Boolean enabled;
    private String notes;
    private Map<String, String> properties;
    private Boolean active;
    private Boolean error;
    private String description;
    private String state;
    private String stateDescription;
    private String status;
    private String typeName;
    private Map<String, String> tags;
    private String disableWarningMessage;
    private String deleteWarningMessage;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeId() {
        return this.typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getError() {
        return this.error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateDescription() {
        return this.stateDescription;
    }

    public void setStateDescription(String stateDescription) {
        this.stateDescription = stateDescription;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Map<String, String> getTags() {
        return this.tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public String getDisableWarningMessage() {
        return this.disableWarningMessage;
    }

    public void setDisableWarningMessage(String disableWarningMessage) {
        this.disableWarningMessage = disableWarningMessage;
    }

    public String getDeleteWarningMessage() {
        return this.deleteWarningMessage;
    }

    public void setDeleteWarningMessage(String deleteWarningMessage) {
        this.deleteWarningMessage = deleteWarningMessage;
    }

    public String toString() {
        return "CapabilityXO(id:" + this.id + "', typeId:" + this.typeId + ", enabled:" + this.enabled + ", notes:" + this.notes + ", properties:" + this.properties + ", active:" + this.active + ", error:" + this.error + ", description:" + this.description + ", state:" + this.state + ", stateDescription:" + this.stateDescription + ", status:" + this.status + ", typeName:" + this.typeName + ", tags:" + this.tags + ", disableWarningMessage:" + this.disableWarningMessage + ", deleteWarningMessage:" + this.deleteWarningMessage + ")";
    }
}

