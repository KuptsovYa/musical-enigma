/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.NotEmpty
 *  org.sonatype.nexus.security.privilege.PrivilegesExist
 *  org.sonatype.nexus.security.role.RoleNotContainSelf
 *  org.sonatype.nexus.security.role.RolesExist
 *  org.sonatype.nexus.security.role.UniqueRoleId
 *  org.sonatype.nexus.validation.group.Create
 *  org.sonatype.nexus.validation.group.Update
 */
package org.sonatype.nexus.coreui;

import java.util.Set;
import javax.validation.constraints.NotEmpty;
import org.sonatype.nexus.security.privilege.PrivilegesExist;
import org.sonatype.nexus.security.role.RoleNotContainSelf;
import org.sonatype.nexus.security.role.RolesExist;
import org.sonatype.nexus.security.role.UniqueRoleId;
import org.sonatype.nexus.validation.group.Create;
import org.sonatype.nexus.validation.group.Update;

@RoleNotContainSelf(id="getId", roleIds="getRoles")
public class RoleXO {
    @NotEmpty
    @UniqueRoleId(groups={Create.class})
    private String id;
    @NotEmpty(groups={Update.class})
    private String version;
    private String source;
    @NotEmpty
    private String name;
    private String description;
    private Boolean readOnly;
    @PrivilegesExist(groups={Create.class, Update.class})
    private Set<String> privileges;
    @RolesExist(groups={Create.class, Update.class})
    private Set<String> roles;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getReadOnly() {
        return this.readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Set<String> getPrivileges() {
        return this.privileges;
    }

    public void setPrivileges(Set<String> privileges) {
        this.privileges = privileges;
    }

    public Set<String> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String toString() {
        return "RoleXO{id='" + this.id + "', version='" + this.version + "', source='" + this.source + "', name='" + this.name + "', description='" + this.description + "', readOnly=" + this.readOnly + ", privileges=" + this.privileges + ", roles=" + this.roles + "}";
    }
}

