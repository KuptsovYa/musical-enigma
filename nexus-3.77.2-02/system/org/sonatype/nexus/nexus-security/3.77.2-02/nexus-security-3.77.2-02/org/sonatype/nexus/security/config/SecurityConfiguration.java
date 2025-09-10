/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package org.sonatype.nexus.security.config;

import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import org.sonatype.nexus.security.config.CPrivilege;
import org.sonatype.nexus.security.config.CRole;
import org.sonatype.nexus.security.config.CRoleBuilder;
import org.sonatype.nexus.security.config.CUser;
import org.sonatype.nexus.security.config.CUserRoleMapping;
import org.sonatype.nexus.security.user.NoSuchRoleMappingException;
import org.sonatype.nexus.security.user.UserNotFoundException;

public interface SecurityConfiguration {
    public List<CUser> getUsers();

    @Nullable
    public CUser getUser(String var1);

    public CUser newUser();

    public void addUser(CUser var1, Set<String> var2);

    public void addRoleMapping(String var1, Set<String> var2, String var3);

    public void updateUser(CUser var1) throws UserNotFoundException;

    public void updateUser(CUser var1, Set<String> var2) throws UserNotFoundException;

    public boolean removeUser(String var1);

    public List<CUserRoleMapping> getUserRoleMappings();

    @Nullable
    public CUserRoleMapping getUserRoleMapping(String var1, String var2);

    public CUserRoleMapping newUserRoleMapping();

    public void addUserRoleMapping(CUserRoleMapping var1);

    public void updateUserRoleMapping(CUserRoleMapping var1) throws NoSuchRoleMappingException;

    public boolean removeUserRoleMapping(String var1, String var2);

    public List<CPrivilege> getPrivileges();

    @Nullable
    public CPrivilege getPrivilege(String var1);

    @Nullable
    public CPrivilege getPrivilegeByName(String var1);

    public List<CPrivilege> getPrivileges(Set<String> var1);

    public CPrivilege newPrivilege();

    public CPrivilege addPrivilege(CPrivilege var1);

    public void updatePrivilege(CPrivilege var1);

    public void updatePrivilegeByName(CPrivilege var1);

    public boolean removePrivilege(String var1);

    public boolean removePrivilegeByName(String var1);

    public List<CRole> getRoles();

    @Nullable
    public CRole getRole(String var1);

    public CRole newRole();

    default public CRoleBuilder newRoleBuilder() {
        return new CRoleBuilder(this.newRole());
    }

    public void addRole(CRole var1);

    public void updateRole(CRole var1);

    public boolean removeRole(String var1);
}

