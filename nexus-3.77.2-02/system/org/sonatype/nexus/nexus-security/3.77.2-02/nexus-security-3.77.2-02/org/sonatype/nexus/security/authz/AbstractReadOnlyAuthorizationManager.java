/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.authz;

import java.util.Set;
import org.sonatype.nexus.security.authz.AuthorizationManager;
import org.sonatype.nexus.security.privilege.NoSuchPrivilegeException;
import org.sonatype.nexus.security.privilege.Privilege;
import org.sonatype.nexus.security.role.NoSuchRoleException;
import org.sonatype.nexus.security.role.Role;

public abstract class AbstractReadOnlyAuthorizationManager
implements AuthorizationManager {
    @Override
    public boolean supportsWrite() {
        return false;
    }

    @Override
    public Privilege addPrivilege(Privilege privilege) {
        throw this.unsupported();
    }

    @Override
    public Role addRole(Role role) {
        throw this.unsupported();
    }

    @Override
    public Set<Role> searchRoles(String query) {
        return this.listRoles();
    }

    @Override
    public void deletePrivilege(String privilegeId) throws NoSuchPrivilegeException {
        throw this.unsupported();
    }

    @Override
    public void deletePrivilegeByName(String privilegeName) throws NoSuchPrivilegeException {
        throw this.unsupported();
    }

    @Override
    public void deleteRole(String roleId) throws NoSuchRoleException {
        throw this.unsupported();
    }

    @Override
    public Privilege updatePrivilege(Privilege privilege) throws NoSuchPrivilegeException {
        throw this.unsupported();
    }

    @Override
    public Privilege updatePrivilegeByName(Privilege privilege) throws NoSuchPrivilegeException {
        return null;
    }

    @Override
    public Role updateRole(Role role) throws NoSuchRoleException {
        throw this.unsupported();
    }

    private IllegalStateException unsupported() {
        throw new IllegalStateException("AuthorizationManager: '" + this.getSource() + "' does not support write operations.");
    }
}

