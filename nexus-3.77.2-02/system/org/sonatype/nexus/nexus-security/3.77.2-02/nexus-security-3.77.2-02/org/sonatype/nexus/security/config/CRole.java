/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.config;

import java.io.Serializable;
import java.util.Set;

public interface CRole
extends Cloneable,
Serializable {
    public void addPrivilege(String var1);

    public void addRole(String var1);

    public String getDescription();

    public String getId();

    public String getName();

    public Set<String> getPrivileges();

    public Set<String> getRoles();

    public boolean isReadOnly();

    public void removePrivilege(String var1);

    public void removeRole(String var1);

    public void setDescription(String var1);

    public void setId(String var1);

    public void setName(String var1);

    public void setPrivileges(Set<String> var1);

    public void setReadOnly(boolean var1);

    public void setRoles(Set<String> var1);

    public int getVersion();

    public void setVersion(int var1);

    public CRole clone();
}

