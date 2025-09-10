/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.anonymous;

public interface AnonymousConfiguration
extends Cloneable {
    public static final String DEFAULT_USER_ID = "anonymous";
    public static final String DEFAULT_REALM_NAME = "NexusAuthorizingRealm";

    public AnonymousConfiguration copy();

    public String getRealmName();

    public String getUserId();

    public boolean isEnabled();

    public void setEnabled(boolean var1);

    public void setRealmName(String var1);

    public void setUserId(String var1);
}

