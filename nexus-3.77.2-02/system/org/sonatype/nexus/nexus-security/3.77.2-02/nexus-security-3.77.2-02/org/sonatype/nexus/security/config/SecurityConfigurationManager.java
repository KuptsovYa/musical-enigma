/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.config;

import java.util.List;
import java.util.Set;
import org.sonatype.nexus.security.config.CPrivilege;
import org.sonatype.nexus.security.config.CRole;
import org.sonatype.nexus.security.config.CUser;
import org.sonatype.nexus.security.config.CUserRoleMapping;
import org.sonatype.nexus.security.user.NoSuchRoleMappingException;
import org.sonatype.nexus.security.user.UserNotFoundException;

public interface SecurityConfigurationManager {
    public List<CUser> listUsers();

    public void createUser(CUser var1, Set<String> var2);

    public void createUser(CUser var1, String var2, Set<String> var3);

    public CUser newUser();

    public CUser readUser(String var1) throws UserNotFoundException;

    public void updateUser(CUser var1) throws UserNotFoundException;

    public void updateUser(CUser var1, Set<String> var2) throws UserNotFoundException;

    public void deleteUser(String var1) throws UserNotFoundException;

    public List<CRole> listRoles();

    public void createRole(CRole var1);

    public CRole newRole();

    public CRole readRole(String var1);

    public void updateRole(CRole var1);

    public void deleteRole(String var1);

    public List<CPrivilege> listPrivileges();

    public void createPrivilege(CPrivilege var1);

    public CPrivilege newPrivilege();

    public CPrivilege readPrivilege(String var1);

    public CPrivilege readPrivilegeByName(String var1);

    public List<CPrivilege> readPrivileges(Set<String> var1);

    public void updatePrivilege(CPrivilege var1);

    public void updatePrivilegeByName(CPrivilege var1);

    public void deletePrivilege(String var1);

    public void deletePrivilegeByName(String var1);

    public void cleanRemovedPrivilege(String var1);

    public void createUserRoleMapping(CUserRoleMapping var1);

    public CUserRoleMapping newUserRoleMapping();

    public void updateUserRoleMapping(CUserRoleMapping var1) throws NoSuchRoleMappingException;

    public CUserRoleMapping readUserRoleMapping(String var1, String var2) throws NoSuchRoleMappingException;

    public List<CUserRoleMapping> listUserRoleMappings();

    public void deleteUserRoleMapping(String var1, String var2) throws NoSuchRoleMappingException;
}

