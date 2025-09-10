/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 */
package org.sonatype.nexus.security.config.memory;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import org.sonatype.nexus.security.config.CRole;

public class MemoryCRole
implements CRole {
    private String description;
    private String id;
    private String name;
    private Set<String> privileges;
    private boolean readOnly = false;
    private Set<String> roles;
    private int version;

    @Override
    public void addPrivilege(String string) {
        this.getPrivileges().add(string);
    }

    @Override
    public void addRole(String string) {
        this.getRoles().add(string);
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
    public Set<String> getPrivileges() {
        if (this.privileges == null) {
            this.privileges = Sets.newHashSet();
        }
        return this.privileges;
    }

    @Override
    public Set<String> getRoles() {
        if (this.roles == null) {
            this.roles = Sets.newHashSet();
        }
        return this.roles;
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
    public void removePrivilege(String string) {
        this.getPrivileges().remove(string);
    }

    @Override
    public void removeRole(String string) {
        this.getRoles().remove(string);
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
    public void setPrivileges(Set<String> privileges) {
        this.privileges = privileges;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    public MemoryCRole withDescription(String description) {
        this.description = description;
        return this;
    }

    public MemoryCRole withId(String id) {
        this.id = id;
        return this;
    }

    public MemoryCRole withName(String name) {
        this.name = name;
        return this;
    }

    public MemoryCRole withPrivileges(String ... privileges) {
        this.privileges = new HashSet<String>(Set.of(privileges));
        return this;
    }

    public MemoryCRole withReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }

    public MemoryCRole withRoles(String ... roles) {
        this.roles = Set.of(roles);
        return this;
    }

    public MemoryCRole withVersion(int version) {
        this.version = version;
        return this;
    }

    @Override
    public MemoryCRole clone() {
        try {
            MemoryCRole copy = (MemoryCRole)super.clone();
            if (this.privileges != null) {
                copy.privileges = Sets.newHashSet(this.privileges);
            }
            if (this.roles != null) {
                copy.roles = Sets.newHashSet(this.roles);
            }
            return copy;
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{id='" + this.id + "', name='" + this.name + "', description='" + this.description + "', privileges=" + this.privileges + ", roles=" + this.roles + ", readOnly=" + this.readOnly + ", version='" + this.version + "'}";
    }
}

