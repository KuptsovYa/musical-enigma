/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.user;

import org.sonatype.nexus.security.user.AbstractUserManager;
import org.sonatype.nexus.security.user.User;
import org.sonatype.nexus.security.user.UserNotFoundException;

public abstract class AbstractReadOnlyUserManager
extends AbstractUserManager {
    @Override
    public boolean supportsWrite() {
        return false;
    }

    @Override
    public User addUser(User user, String password) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void changePassword(String userId, String newPassword) throws UserNotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteUser(String userId) throws UserNotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public User updateUser(User user) throws UserNotFoundException {
        throw new UnsupportedOperationException();
    }
}

