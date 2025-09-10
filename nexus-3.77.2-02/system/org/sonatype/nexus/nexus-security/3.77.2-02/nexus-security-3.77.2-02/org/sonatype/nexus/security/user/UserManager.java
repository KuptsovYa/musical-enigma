/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.user;

import java.util.Set;
import org.sonatype.nexus.security.user.User;
import org.sonatype.nexus.security.user.UserNotFoundException;
import org.sonatype.nexus.security.user.UserSearchCriteria;

public interface UserManager {
    public static final String DEFAULT_SOURCE = "default";

    public String getSource();

    public String getAuthenticationRealmName();

    public boolean supportsWrite();

    public Set<User> listUsers();

    public Set<String> listUserIds();

    public User addUser(User var1, String var2);

    public User updateUser(User var1) throws UserNotFoundException;

    public void deleteUser(String var1) throws UserNotFoundException;

    public Set<User> searchUsers(UserSearchCriteria var1);

    public User getUser(String var1) throws UserNotFoundException;

    public User getUser(String var1, Set<String> var2) throws UserNotFoundException;

    public void changePassword(String var1, String var2) throws UserNotFoundException;

    public boolean isConfigured();
}

