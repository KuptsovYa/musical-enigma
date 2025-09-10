/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.shiro.subject.PrincipalCollection
 */
package org.sonatype.nexus.security.authc.apikey;

import org.apache.shiro.subject.PrincipalCollection;

public interface ApiKeyFactory {
    public char[] makeApiKey(PrincipalCollection var1);
}

