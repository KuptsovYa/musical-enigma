/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModelProperty
 */
package org.sonatype.nexus.security.role.rest;

import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import java.util.Set;
import org.sonatype.nexus.security.role.Role;

public class RoleXOResponse {
    @ApiModelProperty(value="The id of the role.")
    private String id;
    @ApiModelProperty(value="The user source which is the origin of this role.")
    private String source;
    @ApiModelProperty(value="The name of the role.")
    private String name;
    @ApiModelProperty(value="The description of this role.")
    private String description;
    @ApiModelProperty(value="Indicates whether the role can be changed. The system will ignore any supplied external values.")
    private boolean readOnly;
    @ApiModelProperty(value="The list of privileges assigned to this role.")
    private Set<String> privileges;
    @ApiModelProperty(value="The list of roles assigned to this role.")
    private Set<String> roles;

    public void setSource(String source) {
        this.source = source;
    }

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

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getSource() {
        return this.source;
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

    public Boolean getReadOnly() {
        return this.readOnly;
    }

    public String toString() {
        return "id: " + this.id + " name: " + this.name + " source: " + this.source + " description: " + this.description + " readOnly: " + this.readOnly + " roles: " + this.roles + " privileges: " + this.privileges;
    }

    public int hashCode() {
        return Objects.hash(this.id, this.name, this.source, this.description, this.roles, this.privileges);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof RoleXOResponse)) {
            return false;
        }
        RoleXOResponse other = (RoleXOResponse)obj;
        return Objects.equals(this.id, other.id) && Objects.equals(this.name, other.name) && Objects.equals(this.source, other.source) && Objects.equals(this.description, other.description) && Objects.equals(this.roles, other.roles) && Objects.equals(this.privileges, other.privileges);
    }

    public static RoleXOResponse fromRole(Role input) {
        RoleXOResponse role = new RoleXOResponse();
        role.setId(input.getRoleId());
        role.setSource(input.getSource());
        role.setDescription(input.getDescription() != null ? input.getDescription() : input.getRoleId());
        role.setName(input.getName() != null ? input.getName() : input.getRoleId());
        role.setPrivileges(input.getPrivileges());
        role.setRoles(input.getRoles());
        role.setReadOnly(input.isReadOnly());
        return role;
    }
}

