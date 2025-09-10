/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.Pattern
 *  org.sonatype.nexus.security.privilege.UniquePrivilegeId
 *  org.sonatype.nexus.security.privilege.UniquePrivilegeName
 *  org.sonatype.nexus.validation.group.Create
 *  org.sonatype.nexus.validation.group.Update
 */
package org.sonatype.nexus.coreui;

import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import org.sonatype.nexus.security.privilege.UniquePrivilegeId;
import org.sonatype.nexus.security.privilege.UniquePrivilegeName;
import org.sonatype.nexus.validation.group.Create;
import org.sonatype.nexus.validation.group.Update;

public class PrivilegeXO {
    @NotBlank(groups={Update.class})
    @UniquePrivilegeId(groups={Create.class})
    private String id;
    @NotBlank(groups={Update.class})
    private String version;
    @NotBlank
    @Pattern(regexp="^[a-zA-Z0-9\\-]{1}[a-zA-Z0-9_\\-\\.]*$", message="{org.sonatype.nexus.validation.constraint.name}")
    @UniquePrivilegeName(groups={Create.class})
    private @NotBlank @Pattern(regexp="^[a-zA-Z0-9\\-]{1}[a-zA-Z0-9_\\-\\.]*$", message="{org.sonatype.nexus.validation.constraint.name}") String name;
    private String description;
    @NotBlank
    private String type;
    private Boolean readOnly;
    @NotEmpty
    private Map<String, String> properties;
    private String permission;

    public String getId() {
        return this.id;
    }

    public PrivilegeXO withId(String id) {
        this.id = id;
        return this;
    }

    public String getVersion() {
        return this.version;
    }

    public PrivilegeXO withVersion(String version) {
        this.version = version;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public PrivilegeXO withName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public PrivilegeXO withDescription(String description) {
        this.description = description;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public PrivilegeXO withType(String type) {
        this.type = type;
        return this;
    }

    public Boolean getReadOnly() {
        return this.readOnly;
    }

    public PrivilegeXO withReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }

    public PrivilegeXO withProperties(Map<String, String> properties) {
        this.properties = properties;
        return this;
    }

    public String getPermission() {
        return this.permission;
    }

    public PrivilegeXO withPermission(String permission) {
        this.permission = permission;
        return this;
    }

    public String toString() {
        return "PrivilegeXO{id='" + this.id + "', version='" + this.version + "', name='" + this.name + "', description='" + this.description + "', type='" + this.type + "', readOnly=" + this.readOnly + ", properties=" + this.properties + ", permission='" + this.permission + "'}";
    }
}

