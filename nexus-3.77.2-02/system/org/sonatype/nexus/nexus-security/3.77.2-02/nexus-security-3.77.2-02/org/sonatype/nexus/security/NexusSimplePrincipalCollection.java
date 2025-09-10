/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.apache.shiro.subject.SimplePrincipalCollection
 */
package org.sonatype.nexus.security;

import com.google.common.base.Preconditions;
import java.util.Objects;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.sonatype.nexus.security.RealmCaseMapping;

public class NexusSimplePrincipalCollection
extends SimplePrincipalCollection {
    private final RealmCaseMapping realmCaseMapping;

    public NexusSimplePrincipalCollection(String principal, RealmCaseMapping realmCaseMapping) {
        super((Object)principal, realmCaseMapping.getRealmName());
        this.realmCaseMapping = (RealmCaseMapping)Preconditions.checkNotNull((Object)realmCaseMapping);
    }

    public String getRealmName() {
        return this.realmCaseMapping.getRealmName();
    }

    public boolean isRealmCaseSensitive() {
        return this.realmCaseMapping.isCaseSensitive();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || ((Object)((Object)this)).getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        NexusSimplePrincipalCollection that = (NexusSimplePrincipalCollection)((Object)o);
        return Objects.equals(this.realmCaseMapping, that.realmCaseMapping);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), this.realmCaseMapping);
    }
}

