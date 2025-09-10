/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.apache.shiro.subject.PrincipalCollection
 *  org.apache.shiro.subject.Subject
 *  org.sonatype.goodies.lifecycle.Lifecycle
 */
package org.sonatype.nexus.security;

import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.sonatype.goodies.lifecycle.Lifecycle;
import org.sonatype.nexus.security.authz.AuthorizationManager;
import org.sonatype.nexus.security.authz.NoSuchAuthorizationManagerException;
import org.sonatype.nexus.security.privilege.Privilege;
import org.sonatype.nexus.security.role.Role;
import org.sonatype.nexus.security.role.RoleIdentifier;
import org.sonatype.nexus.security.user.InvalidCredentialsException;
import org.sonatype.nexus.security.user.NoSuchUserManagerException;
import org.sonatype.nexus.security.user.User;
import org.sonatype.nexus.security.user.UserManager;
import org.sonatype.nexus.security.user.UserNotFoundException;
import org.sonatype.nexus.security.user.UserSearchCriteria;

public interface SecuritySystem
extends Lifecycle {
    @Deprecated
    public Subject getSubject();

    public boolean isPermitted(PrincipalCollection var1, String var2);

    public boolean[] isPermitted(PrincipalCollection var1, List<String> var2);

    public void checkPermission(PrincipalCollection var1, String var2);

    public Set<Role> listRoles();

    public Set<Role> listRoles(String var1) throws NoSuchAuthorizationManagerException;

    public Set<Role> searchRoles(String var1, String var2) throws NoSuchAuthorizationManagerException;

    @Nullable
    public User currentUser() throws UserNotFoundException;

    public User addUser(User var1, String var2) throws NoSuchUserManagerException;

    public User getUser(String var1, String var2) throws UserNotFoundException, NoSuchUserManagerException;

    public User getUser(String var1, String var2, Set<String> var3) throws UserNotFoundException, NoSuchUserManagerException;

    public User getUser(String var1) throws UserNotFoundException;

    public User updateUser(User var1) throws UserNotFoundException, NoSuchUserManagerException;

    @Deprecated
    public void deleteUser(String var1) throws UserNotFoundException;

    public void deleteUser(String var1, String var2) throws UserNotFoundException, NoSuchUserManagerException;

    public void setUsersRoles(String var1, String var2, Set<RoleIdentifier> var3) throws UserNotFoundException;

    @Deprecated
    public Set<User> listUsers();

    public Set<User> searchUsers(UserSearchCriteria var1);

    public void changePassword(String var1, String var2, String var3) throws UserNotFoundException, InvalidCredentialsException;

    public void changePassword(String var1, String var2) throws UserNotFoundException;

    public void changePassword(String var1, String var2, boolean var3) throws UserNotFoundException;

    public Set<Privilege> listPrivileges();

    public AuthorizationManager getAuthorizationManager(String var1) throws NoSuchAuthorizationManagerException;

    public UserManager getUserManager(String var1) throws NoSuchUserManagerException;

    public List<String> listSources();

    public boolean isValidRealm(String var1);
}

