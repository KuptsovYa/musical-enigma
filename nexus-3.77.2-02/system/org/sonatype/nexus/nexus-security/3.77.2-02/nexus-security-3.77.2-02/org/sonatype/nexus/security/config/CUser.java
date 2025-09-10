/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.config;

import java.io.Serializable;

public interface CUser
extends Serializable,
Cloneable {
    public static final String STATUS_DISABLED = "disabled";
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_CHANGE_PASSWORD = "changepassword";

    public String getEmail();

    public String getFirstName();

    public String getId();

    public String getLastName();

    public String getPassword();

    public String getStatus();

    public void setEmail(String var1);

    public void setFirstName(String var1);

    public void setId(String var1);

    public void setLastName(String var1);

    public void setPassword(String var1);

    public void setStatus(String var1);

    public int getVersion();

    public void setVersion(int var1);

    public boolean isActive();

    public CUser clone();
}

