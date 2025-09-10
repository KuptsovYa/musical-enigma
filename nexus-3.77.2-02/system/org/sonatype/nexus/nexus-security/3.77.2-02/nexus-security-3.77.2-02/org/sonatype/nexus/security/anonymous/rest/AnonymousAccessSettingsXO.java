/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotBlank
 */
package org.sonatype.nexus.security.anonymous.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import org.sonatype.nexus.security.anonymous.AnonymousConfiguration;

@JsonInclude
public class AnonymousAccessSettingsXO {
    @ApiModelProperty(value="Whether or not Anonymous Access is enabled")
    private boolean enabled = false;
    @NotBlank
    @ApiModelProperty(value="The username of the anonymous account")
    private String userId = "anonymous";
    @NotBlank
    @ApiModelProperty(value="The name of the authentication realm for the anonymous account")
    private String realmName = "NexusAuthorizingRealm";

    public AnonymousAccessSettingsXO() {
    }

    public AnonymousAccessSettingsXO(AnonymousConfiguration anonymousConfiguration) {
        this.enabled = anonymousConfiguration.isEnabled();
        this.userId = anonymousConfiguration.getUserId();
        this.realmName = anonymousConfiguration.getRealmName();
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealmName() {
        return this.realmName;
    }

    public void setRealmName(String realmName) {
        this.realmName = realmName;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        AnonymousAccessSettingsXO that = (AnonymousAccessSettingsXO)o;
        return this.enabled == that.enabled && Objects.equals(this.userId, that.userId) && Objects.equals(this.realmName, that.realmName);
    }

    public int hashCode() {
        return Objects.hash(this.enabled, this.userId, this.realmName);
    }
}

