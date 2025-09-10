/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package org.sonatype.nexus.security.realm;

import java.util.List;
import javax.annotation.Nullable;

public interface RealmConfiguration {
    public List<String> getRealmNames();

    public void setRealmNames(@Nullable List<String> var1);

    public RealmConfiguration copy();
}

