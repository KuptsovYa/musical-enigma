/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotBlank
 */
package org.sonatype.nexus.security.privilege.rest;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import org.sonatype.nexus.security.privilege.Privilege;
import org.sonatype.nexus.security.privilege.rest.ApiPrivilege;

public class ApiPrivilegeWildcard
extends ApiPrivilege {
    public static final String PATTERN_KEY = "pattern";
    @NotBlank
    @ApiModelProperty(value="A colon separated list of parts that create a permission string.")
    private String pattern;

    private ApiPrivilegeWildcard() {
        super("wildcard");
    }

    public ApiPrivilegeWildcard(String name, String description, boolean readOnly, String pattern) {
        super("wildcard", name, description, readOnly);
        this.pattern = pattern;
    }

    public ApiPrivilegeWildcard(Privilege privilege) {
        super(privilege);
        this.pattern = privilege.getPrivilegeProperty(PATTERN_KEY);
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return this.pattern;
    }

    @Override
    protected Privilege doAsPrivilege(Privilege privilege) {
        privilege.addProperty(PATTERN_KEY, this.pattern);
        return privilege;
    }
}

