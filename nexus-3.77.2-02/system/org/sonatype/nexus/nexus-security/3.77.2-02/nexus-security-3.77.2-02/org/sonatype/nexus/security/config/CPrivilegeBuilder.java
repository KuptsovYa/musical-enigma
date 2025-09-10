/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Joiner
 *  com.google.common.base.Preconditions
 */
package org.sonatype.nexus.security.config;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import java.util.Arrays;
import org.sonatype.nexus.security.config.CPrivilege;
import org.sonatype.nexus.security.config.memory.MemoryCPrivilege;

public class CPrivilegeBuilder {
    private final CPrivilege model = new MemoryCPrivilege();

    public CPrivilegeBuilder type(String type) {
        this.model.setType(type);
        return this;
    }

    public CPrivilegeBuilder id(String id) {
        this.model.setId(id);
        return this;
    }

    public CPrivilegeBuilder name(String name) {
        this.model.setName(name);
        return this;
    }

    public CPrivilegeBuilder description(String description) {
        this.model.setDescription(description);
        return this;
    }

    public CPrivilegeBuilder readOnly(boolean readOnly) {
        this.model.setReadOnly(readOnly);
        return this;
    }

    public CPrivilegeBuilder property(String name, String value) {
        this.model.setProperty(name, value);
        return this;
    }

    public CPrivilegeBuilder property(String name, Iterable<String> values) {
        return this.property(name, Joiner.on((char)',').join(values));
    }

    public CPrivilegeBuilder property(String name, String ... values) {
        return this.property(name, Arrays.asList(values));
    }

    public CPrivilege create() {
        Preconditions.checkState((this.model.getType() != null ? 1 : 0) != 0, (Object)"Missing: type");
        Preconditions.checkState((this.model.getId() != null ? 1 : 0) != 0, (Object)"Missing: id");
        if (this.model.getName() == null) {
            this.model.setName(this.model.getId());
        }
        if (this.model.getDescription() == null) {
            this.model.setDescription(this.model.getId());
        }
        return this.model;
    }
}

