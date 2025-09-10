/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.shiro.authz.Permission
 */
package org.sonatype.nexus.security.privilege;

import java.util.HashMap;
import java.util.Map;
import org.apache.shiro.authz.Permission;

public class Privilege {
    private String id;
    private String name;
    private String description;
    private String type;
    private Map<String, String> properties = new HashMap<String, String>();
    private boolean readOnly;
    private int version;
    private Permission permission;

    public Privilege() {
    }

    public Privilege(String id, String name, String description, String type, Map<String, String> properties, boolean readOnly) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.properties = properties;
        this.readOnly = readOnly;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }

    public void addProperty(String key, String value) {
        this.properties.put(key, value);
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getPrivilegeProperty(String key) {
        return this.properties.get(key);
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Permission getPermission() {
        return this.permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}

