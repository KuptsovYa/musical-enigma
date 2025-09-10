/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.Email
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 *  org.sonatype.nexus.security.role.RolesExist
 *  org.sonatype.nexus.security.user.UniqueUserId
 *  org.sonatype.nexus.security.user.UserStatus
 *  org.sonatype.nexus.validation.group.Create
 *  org.sonatype.nexus.validation.group.Update
 */
package org.sonatype.nexus.coreui;

import java.util.Set;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.sonatype.nexus.security.role.RolesExist;
import org.sonatype.nexus.security.user.UniqueUserId;
import org.sonatype.nexus.security.user.UserStatus;
import org.sonatype.nexus.validation.group.Create;
import org.sonatype.nexus.validation.group.Update;

public class UserXO {
    @NotBlank
    @UniqueUserId(groups={Create.class})
    private String userId;
    @NotBlank(groups={Update.class})
    private String version;
    private String realm;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Email
    private String email;
    @NotNull
    private UserStatus status;
    @NotBlank(groups={Create.class})
    private String password;
    @NotEmpty
    @RolesExist(groups={Create.class, Update.class})
    private Set<String> roles;
    private Boolean external;
    private Set<String> externalRoles;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRealm() {
        return this.realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserStatus getStatus() {
        return this.status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Boolean isExternal() {
        return this.external;
    }

    public void setExternal(Boolean external) {
        this.external = external;
    }

    public Set<String> getExternalRoles() {
        return this.externalRoles;
    }

    public void setExternalRoles(Set<String> externalRoles) {
        this.externalRoles = externalRoles;
    }

    public String toString() {
        return "UserXO{userId='" + this.userId + "', version='" + this.version + "', realm='" + this.realm + "', firstName='" + this.firstName + "', lastName='" + this.lastName + "', email='" + this.email + "', status=" + this.status + ", password='" + this.password + "', roles=" + this.roles + ", external=" + this.external + ", externalRoles=" + this.externalRoles + "}";
    }
}

