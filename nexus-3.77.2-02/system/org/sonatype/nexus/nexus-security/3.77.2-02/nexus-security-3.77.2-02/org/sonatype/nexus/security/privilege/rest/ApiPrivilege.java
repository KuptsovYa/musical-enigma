/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.Pattern
 */
package org.sonatype.nexus.security.privilege.rest;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.sonatype.nexus.security.privilege.Privilege;

public abstract class ApiPrivilege {
    @ApiModelProperty(value="The type of privilege, each type covers different portions of the system. External values supplied to this will be ignored by the system.")
    private String type;
    @NotBlank
    @Pattern(regexp="^[a-zA-Z0-9\\-]{1}[a-zA-Z0-9_\\-\\.]*$", message="{org.sonatype.nexus.validation.constraint.name}")
    @ApiModelProperty(value="The name of the privilege.  This value cannot be changed.")
    private @NotBlank @Pattern(regexp="^[a-zA-Z0-9\\-]{1}[a-zA-Z0-9_\\-\\.]*$", message="{org.sonatype.nexus.validation.constraint.name}") String name;
    private String description = "";
    @ApiModelProperty(value="Indicates whether the privilege can be changed. External values supplied to this will be ignored by the system.")
    private boolean readOnly;

    public ApiPrivilege(String type) {
        this(type, null, "", false);
    }

    public ApiPrivilege(String type, String name, String description, boolean readOnly) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.readOnly = readOnly;
    }

    public ApiPrivilege(Privilege privilege) {
        this.type = privilege.getType();
        this.name = privilege.getName();
        this.description = privilege.getDescription();
        this.readOnly = privilege.isReadOnly();
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description == null ? "" : description;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public Privilege asPrivilege() {
        Privilege privilege = new Privilege();
        privilege.setId(this.name);
        privilege.setName(this.name);
        privilege.setDescription(this.description);
        privilege.setReadOnly(this.readOnly);
        privilege.setType(this.type);
        return this.doAsPrivilege(privilege);
    }

    protected abstract Privilege doAsPrivilege(Privilege var1);
}

