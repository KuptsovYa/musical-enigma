/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModelProperty
 *  javax.annotation.Nullable
 *  javax.validation.constraints.Email
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 */
package org.sonatype.nexus.security.internal.rest;

import io.swagger.annotations.ApiModelProperty;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.sonatype.nexus.security.internal.rest.ApiUserStatus;
import org.sonatype.nexus.security.role.RoleIdentifier;
import org.sonatype.nexus.security.user.User;

public class ApiUser {
    @NotBlank
    @ApiModelProperty(value="The userid which is required for login. This value cannot be changed.")
    private String userId;
    @NotEmpty
    @ApiModelProperty(value="The first name of the user.")
    private String firstName;
    @NotEmpty
    @ApiModelProperty(value="The last name of the user.")
    private String lastName;
    @NotEmpty
    @Email
    @ApiModelProperty(value="The email address associated with the user.")
    private String emailAddress;
    @NotBlank
    @ApiModelProperty(value="The user source which is the origin of this user. This value cannot be changed.")
    private String source;
    @NotNull
    @ApiModelProperty(value="The user's status, e.g. active or disabled.")
    private ApiUserStatus status;
    @ApiModelProperty(value="Indicates whether the user's properties could be modified by the Nexus Repository Manager. When false only roles are considered during update.")
    private boolean readOnly;
    @NotEmpty
    @ApiModelProperty(value="The roles which the user has been assigned within Nexus.")
    private Set<String> roles;
    @ApiModelProperty(value="The roles which the user has been assigned in an external source, e.g. LDAP group. These cannot be changed within the Nexus Repository Manager.")
    private Set<String> externalRoles;

    private ApiUser() {
    }

    ApiUser(String userId, String firstName, String lastName, String emailAddress, String source, ApiUserStatus status, boolean readOnly, Set<String> roles, Set<String> externalRoles) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.source = source;
        this.status = status;
        this.readOnly = readOnly;
        this.roles = roles;
        this.externalRoles = externalRoles;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public String getSource() {
        return this.source;
    }

    public ApiUserStatus getStatus() {
        return this.status;
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public Set<String> getRoles() {
        return this.roles;
    }

    @Nullable
    public Set<String> getExternalRoles() {
        return this.externalRoles;
    }

    public void setExternalRoles(Set<String> externalRoles) {
        this.externalRoles = externalRoles;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setStatus(ApiUserStatus status) {
        this.status = status;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    User toUser() {
        User user = new User();
        user.setUserId(this.userId);
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setEmailAddress(this.emailAddress);
        user.setSource(this.source);
        user.setStatus(this.status.getStatus());
        user.setReadOnly(this.readOnly);
        user.setVersion(1);
        HashSet<RoleIdentifier> roleIdentifiers = new HashSet<RoleIdentifier>();
        this.roles.stream().map(r -> new RoleIdentifier("default", (String)r)).forEach(roleIdentifiers::add);
        if (this.externalRoles != null) {
            this.externalRoles.stream().map(r -> new RoleIdentifier(this.source, (String)r)).forEach(roleIdentifiers::add);
        }
        user.setRoles(roleIdentifiers);
        return user;
    }
}

