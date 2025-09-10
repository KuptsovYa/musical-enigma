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
import org.sonatype.nexus.security.config.CUserRoleMapping;

public class MemoryCUserRoleMapping
implements CUserRoleMapping {
    private Set<String> roles;
    private String source;
    private String userId;
    private String version;

    @Override
    public void addRole(String string) {
        this.getRoles().add(string);
    }

    @Override
    public MemoryCUserRoleMapping clone() {
        try {
            MemoryCUserRoleMapping copy = (MemoryCUserRoleMapping)super.clone();
            if (this.roles != null) {
                copy.roles = Sets.newHashSet(this.roles);
            }
            return copy;
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<String> getRoles() {
        if (this.roles == null) {
            this.roles = Sets.newHashSet();
        }
        return this.roles;
    }

    @Override
    public String getSource() {
        return this.source;
    }

    @Override
    public String getUserId() {
        return this.userId;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public void removeRole(String string) {
        this.getRoles().remove(string);
    }

    @Override
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    public MemoryCUserRoleMapping withRoles(String ... roles) {
        this.setRoles(new HashSet<String>(Set.of(roles)));
        return this;
    }

    public MemoryCUserRoleMapping withSource(String source) {
        this.source = source;
        return this;
    }

    public MemoryCUserRoleMapping withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{userId='" + this.userId + "', source='" + this.source + "', roles=" + this.roles + ", version='" + this.version + "'}";
    }
}

