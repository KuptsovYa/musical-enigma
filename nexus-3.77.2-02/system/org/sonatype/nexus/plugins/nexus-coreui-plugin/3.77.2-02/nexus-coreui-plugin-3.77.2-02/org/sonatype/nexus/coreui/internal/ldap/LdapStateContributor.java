/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.rapture.StateContributor
 */
package org.sonatype.nexus.coreui.internal.ldap;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.rapture.StateContributor;

@Named
@Singleton
public class LdapStateContributor
extends ComponentSupport
implements StateContributor {
    public boolean featureFlag;
    public int mappedRoleQueryCharacterLimit;

    @Inject
    public LdapStateContributor(@Named(value="${nexus.react.ldap:-false}") Boolean featureFlag, @Named(value="${nexus.ldap.mapped.role.query.character.limit:-3}") int mappedRoleQueryCharacterLimit) {
        this.featureFlag = featureFlag;
        this.mappedRoleQueryCharacterLimit = mappedRoleQueryCharacterLimit;
    }

    public Map<String, Object> getState() {
        return ImmutableMap.of((Object)"nexus.react.ldap", (Object)this.featureFlag, (Object)"nexus.ldap.mapped.role.query.character.limit", (Object)this.mappedRoleQueryCharacterLimit);
    }
}

