/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.config;

import java.io.Serializable;
import java.util.Set;

public interface CUserRoleMapping
extends Serializable,
Cloneable {
    public void addRole(String var1);

    public Set<String> getRoles();

    public String getSource();

    public String getUserId();

    public void removeRole(String var1);

    public void setRoles(Set<String> var1);

    public void setSource(String var1);

    public void setUserId(String var1);

    public String getVersion();

    public void setVersion(String var1);

    public CUserRoleMapping clone();
}

