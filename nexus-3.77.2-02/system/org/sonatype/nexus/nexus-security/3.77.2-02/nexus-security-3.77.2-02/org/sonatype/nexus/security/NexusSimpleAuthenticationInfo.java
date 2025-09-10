/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.shiro.authc.SimpleAuthenticationInfo
 */
package org.sonatype.nexus.security;

import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.sonatype.nexus.security.NexusSimplePrincipalCollection;
import org.sonatype.nexus.security.RealmCaseMapping;

public class NexusSimpleAuthenticationInfo
extends SimpleAuthenticationInfo {
    public NexusSimpleAuthenticationInfo(String principal, char[] credentials, RealmCaseMapping realmCaseMapping) {
        this.principals = new NexusSimplePrincipalCollection(principal, realmCaseMapping);
        this.credentials = credentials;
    }
}

