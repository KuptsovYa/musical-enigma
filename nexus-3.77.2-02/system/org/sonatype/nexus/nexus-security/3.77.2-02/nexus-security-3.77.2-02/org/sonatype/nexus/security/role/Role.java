/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.role;

import java.util.HashSet;
import java.util.Set;

public class Role
implements Comparable<Role> {
    private String roleId;
    private String name;
    private String description;
    private String source;
    private boolean readOnly;
    private Set<String> roles = new HashSet<String>();
    private Set<String> privileges = new HashSet<String>();
    private int version;

    public Role() {
    }

    public Role(String roleId, String name, String description, String source, boolean readOnly, Set<String> roles, Set<String> privileges) {
        this.roleId = roleId;
        this.name = name;
        this.description = description;
        this.source = source;
        this.readOnly = readOnly;
        this.roles = roles;
        this.privileges = privileges;
    }

    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Set<String> getRoles() {
        return this.roles;
    }

    public void addRole(String role) {
        this.roles.add(role);
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getPrivileges() {
        return this.privileges;
    }

    public void addPrivilege(String privilege) {
        this.privileges.add(privilege);
    }

    public void setPrivileges(Set<String> privilege) {
        this.privileges = privilege;
    }

    @Override
    public int compareTo(Role o) {
        int before = -1;
        boolean equal = false;
        boolean after = true;
        if (this == o) {
            return 0;
        }
        if (o == null) {
            return 1;
        }
        if (this.getRoleId() == null && o.getRoleId() != null) {
            return -1;
        }
        if (this.getRoleId() != null && o.getRoleId() == null) {
            return 1;
        }
        int result = this.getRoleId().compareTo(o.getRoleId());
        if (result != 0) {
            return result;
        }
        if (this.getSource() == null) {
            return -1;
        }
        return this.getSource().compareTo(o.getSource());
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Role role = (Role)o;
        if (this.readOnly != role.readOnly) {
            return false;
        }
        if (this.description != null ? !this.description.equals(role.description) : role.description != null) {
            return false;
        }
        if (this.name != null ? !this.name.equals(role.name) : role.name != null) {
            return false;
        }
        if (this.privileges != null ? !this.privileges.equals(role.privileges) : role.privileges != null) {
            return false;
        }
        if (this.roleId != null ? !this.roleId.equals(role.roleId) : role.roleId != null) {
            return false;
        }
        if (this.roles != null ? !this.roles.equals(role.roles) : role.roles != null) {
            return false;
        }
        if (this.source != null ? !this.source.equals(role.source) : role.source != null) {
            return false;
        }
        return this.version == role.version;
    }

    public int hashCode() {
        int result = this.roleId != null ? this.roleId.hashCode() : 0;
        result = 31 * result + (this.name != null ? this.name.hashCode() : 0);
        result = 31 * result + (this.description != null ? this.description.hashCode() : 0);
        result = 31 * result + (this.source != null ? this.source.hashCode() : 0);
        result = 31 * result + (this.readOnly ? 1 : 0);
        result = 31 * result + (this.roles != null ? this.roles.hashCode() : 0);
        result = 31 * result + (this.privileges != null ? this.privileges.hashCode() : 0);
        result = 31 * result + this.version;
        return result;
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{roleId='" + this.roleId + "', name='" + this.name + "', source='" + this.source + "'}";
    }
}

