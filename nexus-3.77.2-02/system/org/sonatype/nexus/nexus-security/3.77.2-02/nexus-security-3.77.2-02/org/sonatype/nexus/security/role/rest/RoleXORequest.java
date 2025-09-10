/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotEmpty
 */
package org.sonatype.nexus.security.role.rest;

import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.NotEmpty;

public class RoleXORequest {
    @NotEmpty
    @ApiModelProperty(value="The id of the role.")
    private String id;
    @NotEmpty
    @ApiModelProperty(value="The name of the role.")
    private String name;
    @ApiModelProperty(value="The description of this role.")
    private String description;
    @ApiModelProperty(value="The list of privileges assigned to this role.")
    private Set<String> privileges;
    @ApiModelProperty(value="The list of roles assigned to this role.")
    private Set<String> roles;

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public void setPrivileges(Set<String> privileges) {
        this.privileges = privileges;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getRoles() {
        return this.roles;
    }

    public Set<String> getPrivileges() {
        return this.privileges;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public String toString() {
        return "id: " + this.id + " name: " + this.name + " description: " + this.description + " roles: " + this.roles + " privileges: " + this.privileges;
    }

    public int hashCode() {
        return Objects.hash(this.id, this.name, this.description, this.roles, this.privileges);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof RoleXORequest)) {
            return false;
        }
        RoleXORequest other = (RoleXORequest)obj;
        return Objects.equals(this.id, other.id) && Objects.equals(this.name, other.name) && Objects.equals(this.description, other.description) && Objects.equals(this.roles, other.roles) && Objects.equals(this.privileges, other.privileges);
    }
}

