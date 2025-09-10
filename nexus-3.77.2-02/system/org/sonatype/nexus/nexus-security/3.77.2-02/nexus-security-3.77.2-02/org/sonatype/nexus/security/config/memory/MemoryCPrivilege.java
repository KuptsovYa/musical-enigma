/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package org.sonatype.nexus.security.config.memory;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import org.sonatype.nexus.security.config.CPrivilege;

public class MemoryCPrivilege
implements CPrivilege {
    private String description;
    private String id;
    private String name;
    private Map<String, String> properties;
    private boolean readOnly = false;
    private String type;
    private int version;

    @Override
    public MemoryCPrivilege clone() {
        try {
            MemoryCPrivilege copy = (MemoryCPrivilege)super.clone();
            if (this.properties != null) {
                copy.properties = Maps.newHashMap(this.properties);
            }
            return copy;
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Map<String, String> getProperties() {
        if (this.properties == null) {
            this.properties = Maps.newHashMap();
        }
        return this.properties;
    }

    @Override
    public String getProperty(String key) {
        return this.getProperties().get(key);
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public int getVersion() {
        return this.version;
    }

    @Override
    public boolean isReadOnly() {
        return this.readOnly;
    }

    @Override
    public void removeProperty(String key) {
        this.getProperties().remove(key);
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public void setProperty(String key, String value) {
        this.getProperties().put(key, value);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{id='" + this.id + "', name='" + this.name + "', description='" + this.description + "', type='" + this.type + "', properties=" + this.properties + ", readOnly=" + this.readOnly + ", version='" + this.version + "'}";
    }

    public static class MemoryCPrivilegeBuilder {
        private final String id;
        private String description;
        private String name;
        private final Map<String, String> properties = new HashMap<String, String>();
        private boolean readOnly = false;
        private String type;
        private int version;

        public MemoryCPrivilegeBuilder(String id) {
            this.id = id;
        }

        public MemoryCPrivilegeBuilder description(String description) {
            this.description = description;
            return this;
        }

        public MemoryCPrivilegeBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MemoryCPrivilegeBuilder readOnly(boolean readOnly) {
            this.readOnly = readOnly;
            return this;
        }

        public MemoryCPrivilegeBuilder type(String type) {
            this.type = type;
            return this;
        }

        public MemoryCPrivilegeBuilder version(int version) {
            this.version = version;
            return this;
        }

        public MemoryCPrivilegeBuilder property(String key, String value) {
            this.properties.put(key, value);
            return this;
        }

        public MemoryCPrivilege build() {
            MemoryCPrivilege privilege = new MemoryCPrivilege();
            privilege.setId(this.id);
            privilege.setDescription(this.description);
            privilege.setName(this.name);
            privilege.setProperties(this.properties);
            privilege.setReadOnly(this.readOnly);
            privilege.setType(this.type);
            privilege.setVersion(this.version);
            return privilege;
        }
    }
}

