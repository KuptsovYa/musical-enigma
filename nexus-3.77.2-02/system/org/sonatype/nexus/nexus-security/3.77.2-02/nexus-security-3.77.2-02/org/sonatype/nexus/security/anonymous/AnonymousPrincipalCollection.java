/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.shiro.subject.SimplePrincipalCollection
 */
package org.sonatype.nexus.security.anonymous;

import org.apache.shiro.subject.SimplePrincipalCollection;

public class AnonymousPrincipalCollection
extends SimplePrincipalCollection {
    public AnonymousPrincipalCollection(Object principal, String realmName) {
        super(principal, realmName);
    }
}

