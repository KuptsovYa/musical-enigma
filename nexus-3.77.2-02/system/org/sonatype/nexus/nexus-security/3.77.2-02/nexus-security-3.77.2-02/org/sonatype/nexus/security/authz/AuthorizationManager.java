/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.authz;

import java.util.List;
import java.util.Set;
import org.sonatype.nexus.security.privilege.NoSuchPrivilegeException;
import org.sonatype.nexus.security.privilege.Privilege;
import org.sonatype.nexus.security.role.Role;

public interface AuthorizationManager {
    public String getSource();

    public boolean supportsWrite();

    public Set<Role> listRoles();

    public Set<Role> searchRoles(String var1);

    public Role getRole(String var1);

    public Role addRole(Role var1);

    public Role updateRole(Role var1);

    public void deleteRole(String var1);

    public Set<Privilege> listPrivileges();

    public Privilege getPrivilege(String var1) throws NoSuchPrivilegeException;

    public Privilege getPrivilegeByName(String var1) throws NoSuchPrivilegeException;

    public List<Privilege> getPrivileges(Set<String> var1);

    public Privilege addPrivilege(Privilege var1);

    public Privilege updatePrivilege(Privilege var1) throws NoSuchPrivilegeException;

    public Privilege updatePrivilegeByName(Privilege var1) throws NoSuchPrivilegeException;

    public void deletePrivilege(String var1) throws NoSuchPrivilegeException;

    public void deletePrivilegeByName(String var1) throws NoSuchPrivilegeException;

    default public String getRealmName() {
        return null;
    }
}

