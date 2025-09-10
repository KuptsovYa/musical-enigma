/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.sonatype.nexus.common.script.ScriptApi
 */
package org.sonatype.nexus.security;

import java.util.List;
import org.sonatype.nexus.common.script.ScriptApi;
import org.sonatype.nexus.security.anonymous.AnonymousConfiguration;
import org.sonatype.nexus.security.authz.NoSuchAuthorizationManagerException;
import org.sonatype.nexus.security.role.Role;
import org.sonatype.nexus.security.user.NoSuchUserManagerException;
import org.sonatype.nexus.security.user.User;
import org.sonatype.nexus.security.user.UserNotFoundException;

public interface SecurityApi
extends ScriptApi {
    default public String getName() {
        return "security";
    }

    public AnonymousConfiguration setAnonymousAccess(boolean var1);

    public User addUser(String var1, String var2, String var3, String var4, boolean var5, String var6, List<String> var7) throws NoSuchUserManagerException;

    public Role addRole(String var1, String var2, String var3, List<String> var4, List<String> var5) throws NoSuchAuthorizationManagerException;

    public User setUserRoles(String var1, List<String> var2) throws UserNotFoundException, NoSuchUserManagerException;
}

