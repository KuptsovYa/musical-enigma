/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.sonatype.nexus.security;

import com.google.common.base.Preconditions;
import java.io.Serializable;

public class RealmCaseMapping
implements Serializable {
    private final String realmName;
    private final boolean isCaseSensitive;

    public RealmCaseMapping(String realmName, boolean isCaseSensitive) {
        this.realmName = (String)Preconditions.checkNotNull((Object)realmName);
        this.isCaseSensitive = isCaseSensitive;
    }

    public String getRealmName() {
        return this.realmName;
    }

    public boolean isCaseSensitive() {
        return this.isCaseSensitive;
    }
}

