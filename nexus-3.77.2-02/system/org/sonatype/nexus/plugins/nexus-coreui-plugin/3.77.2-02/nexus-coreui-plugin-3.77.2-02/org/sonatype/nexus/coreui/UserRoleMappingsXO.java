/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.NotBlank
 *  org.sonatype.nexus.security.realm.RealmExists
 *  org.sonatype.nexus.security.role.RolesExist
 *  org.sonatype.nexus.security.user.UserExists
 *  org.sonatype.nexus.validation.group.Create
 */
package org.sonatype.nexus.coreui;

import java.util.Set;
import javax.validation.constraints.NotBlank;
import org.sonatype.nexus.security.realm.RealmExists;
import org.sonatype.nexus.security.role.RolesExist;
import org.sonatype.nexus.security.user.UserExists;
import org.sonatype.nexus.validation.group.Create;

public class UserRoleMappingsXO {
    @NotBlank
    @UserExists(groups={Create.class})
    private String userId;
    @NotBlank
    @RealmExists(groups={Create.class})
    private String realm;
    @RolesExist
    private Set<String> roles;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealm() {
        return this.realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public Set<String> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String toString() {
        return "UserRoleMappingsXO{userId='" + this.userId + "', realm='" + this.realm + "', roles=" + this.roles + "}";
    }
}

