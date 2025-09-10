/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.user;

import java.util.Set;
import org.sonatype.nexus.security.role.RoleIdentifier;
import org.sonatype.nexus.security.user.UserManager;
import org.sonatype.nexus.security.user.UserNotFoundException;

public interface RoleMappingUserManager
extends UserManager {
    public Set<RoleIdentifier> getUsersRoles(String var1, String var2) throws UserNotFoundException;

    public void setUsersRoles(String var1, String var2, Set<RoleIdentifier> var3) throws UserNotFoundException;
}

