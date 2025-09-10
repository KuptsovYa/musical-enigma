/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.Email
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 */
package org.sonatype.nexus.security.internal.rest;

import io.swagger.annotations.ApiModelProperty;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.sonatype.nexus.security.internal.rest.ApiUserStatus;
import org.sonatype.nexus.security.role.RoleIdentifier;
import org.sonatype.nexus.security.user.User;

public class ApiCreateUser {
    @NotBlank
    @ApiModelProperty(value="The userid which is required for login. This value cannot be changed.")
    private String userId;
    @NotEmpty
    @ApiModelProperty(value="The first name of the user.")
    private String firstName;
    @NotEmpty
    @ApiModelProperty(value="The last name of the user.")
    private String lastName;
    @Email
    @NotEmpty
    @ApiModelProperty(value="The email address associated with the user.")
    private String emailAddress;
    @NotEmpty
    @ApiModelProperty(value="The password for the new user.")
    private String password;
    @NotNull
    @ApiModelProperty(value="The user's status, e.g. active or disabled.")
    private ApiUserStatus status;
    @NotEmpty
    @ApiModelProperty(value="The roles which the user has been assigned within Nexus.")
    private Set<String> roles;

    private ApiCreateUser() {
    }

    ApiCreateUser(String userId, String firstName, String lastName, String emailAddress, String password, ApiUserStatus status, Set<String> roles) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.status = status;
        this.roles = roles;
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

    public ApiUserStatus getStatus() {
        return this.status;
    }

    public String getPassword() {
        return this.password;
    }

    public Set<String> getRoles() {
        return this.roles;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(ApiUserStatus status) {
        this.status = status;
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
        user.setStatus(this.status.getStatus());
        user.setReadOnly(false);
        user.setVersion(1);
        user.setSource("default");
        user.setRoles(this.roles.stream().map(r -> new RoleIdentifier("default", (String)r)).collect(Collectors.toSet()));
        return user;
    }
}

