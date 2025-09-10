/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.internal;

import java.util.Arrays;

public enum RealmToSource {
    SAML_USER_SOURCE("SamlRealm", "SAML"),
    CROWD_USER_SOURCE("Crowd", "Crowd"),
    LDAP_USER_SOURCE("LdapRealm", "LDAP"),
    NEXUS_USER_SOURCE("NexusAuthenticatingRealm", "default");

    private final String realmName;
    private final String sourceName;

    private RealmToSource(String realmName, String sourceName) {
        this.realmName = realmName;
        this.sourceName = sourceName;
    }

    public static String getSource(String realm) {
        return Arrays.stream(RealmToSource.values()).filter(keyRealm -> keyRealm.realmName.equals(realm)).map(keyRealm -> keyRealm.sourceName).findFirst().orElse("default");
    }
}

