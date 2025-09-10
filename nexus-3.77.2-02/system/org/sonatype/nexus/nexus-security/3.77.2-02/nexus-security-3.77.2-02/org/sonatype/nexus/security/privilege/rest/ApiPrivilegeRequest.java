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

public abstract class ApiPrivilegeRequest {
    @NotBlank
    @Pattern(regexp="^[a-zA-Z0-9\\-]{1}[a-zA-Z0-9_\\-\\.]*$", message="{org.sonatype.nexus.validation.constraint.name}")
    @ApiModelProperty(value="The name of the privilege.  This value cannot be changed.")
    private @NotBlank @Pattern(regexp="^[a-zA-Z0-9\\-]{1}[a-zA-Z0-9_\\-\\.]*$", message="{org.sonatype.nexus.validation.constraint.name}") String name;
    private String description = "";

    public ApiPrivilegeRequest() {
        this(null, "");
    }

    public ApiPrivilegeRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public ApiPrivilegeRequest(Privilege privilege) {
        this.name = privilege.getName();
        this.description = privilege.getDescription();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description == null ? "" : description;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Privilege asPrivilege() {
        Privilege privilege = new Privilege();
        privilege.setId(this.name);
        privilege.setName(this.name);
        privilege.setDescription(this.description);
        return this.doAsPrivilege(privilege);
    }

    protected abstract Privilege doAsPrivilege(Privilege var1);
}

