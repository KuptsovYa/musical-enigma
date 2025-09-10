/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.role;

import java.util.HashSet;
import java.util.Set;

public class RoleIdentifier {
    private String source;
    private String roleId;

    public RoleIdentifier(String source, String roleId) {
        this.source = source;
        this.roleId = roleId;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public static Set<RoleIdentifier> getRoleIdentifiersForSource(String source, Set<RoleIdentifier> roleIdentifiers) {
        HashSet<RoleIdentifier> sourceRoleIdentifiers = new HashSet<RoleIdentifier>();
        if (roleIdentifiers != null) {
            for (RoleIdentifier roleIdentifier : roleIdentifiers) {
                if (!roleIdentifier.getSource().equals(source)) continue;
                sourceRoleIdentifiers.add(roleIdentifier);
            }
        }
        return sourceRoleIdentifiers;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        RoleIdentifier that = (RoleIdentifier)o;
        if (this.roleId != null ? !this.roleId.equals(that.roleId) : that.roleId != null) {
            return false;
        }
        return !(this.source != null ? !this.source.equals(that.source) : that.source != null);
    }

    public int hashCode() {
        int result = this.source != null ? this.source.hashCode() : 0;
        result = 31 * result + (this.roleId != null ? this.roleId.hashCode() : 0);
        return result;
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{source='" + this.source + "', roleId='" + this.roleId + "'}";
    }
}

