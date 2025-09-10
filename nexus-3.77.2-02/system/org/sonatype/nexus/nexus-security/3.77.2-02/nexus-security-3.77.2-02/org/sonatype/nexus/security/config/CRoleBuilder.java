/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.sonatype.nexus.security.config;

import com.google.common.base.Preconditions;
import org.sonatype.nexus.security.config.CRole;

public class CRoleBuilder {
    private final CRole model;

    CRoleBuilder(CRole model) {
        this.model = model;
    }

    public CRoleBuilder id(String id) {
        this.model.setId(id);
        return this;
    }

    public CRoleBuilder name(String name) {
        this.model.setName(name);
        return this;
    }

    public CRoleBuilder description(String description) {
        this.model.setDescription(description);
        return this;
    }

    public CRoleBuilder privilege(String privilege) {
        this.model.addPrivilege(privilege);
        return this;
    }

    public CRoleBuilder role(String role) {
        this.model.addRole(role);
        return this;
    }

    public CRoleBuilder readOnly(boolean readOnly) {
        this.model.setReadOnly(readOnly);
        return this;
    }

    public CRole create() {
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

