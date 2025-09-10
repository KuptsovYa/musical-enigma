/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.config;

import java.io.Serializable;
import java.util.Map;

public interface CPrivilege
extends Serializable,
Cloneable {
    public void setProperty(String var1, String var2);

    public String getDescription();

    public String getId();

    public String getName();

    public Map<String, String> getProperties();

    public String getProperty(String var1);

    public String getType();

    public boolean isReadOnly();

    public void removeProperty(String var1);

    public void setDescription(String var1);

    public void setId(String var1);

    public void setName(String var1);

    public void setProperties(Map<String, String> var1);

    public void setReadOnly(boolean var1);

    public void setType(String var1);

    public int getVersion();

    public void setVersion(int var1);

    public CPrivilege clone();
}

