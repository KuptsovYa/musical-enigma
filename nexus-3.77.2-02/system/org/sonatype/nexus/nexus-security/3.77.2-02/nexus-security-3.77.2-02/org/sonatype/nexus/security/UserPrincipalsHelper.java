/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.apache.shiro.subject.PrincipalCollection
 *  org.sonatype.goodies.common.ComponentSupport
 */
package org.sonatype.nexus.security;

import com.google.common.base.Preconditions;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.shiro.subject.PrincipalCollection;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.security.user.NoSuchUserManagerException;
import org.sonatype.nexus.security.user.User;
import org.sonatype.nexus.security.user.UserManager;
import org.sonatype.nexus.security.user.UserNotFoundException;
import org.sonatype.nexus.security.user.UserNotFoundTransientException;
import org.sonatype.nexus.security.user.UserStatus;

@Named
@Singleton
public class UserPrincipalsHelper
extends ComponentSupport {
    private final List<UserManager> userManagers;

    @Inject
    public UserPrincipalsHelper(List<UserManager> userManagers) {
        this.userManagers = (List)Preconditions.checkNotNull(userManagers);
    }

    public UserStatus getUserStatus(PrincipalCollection principals) throws UserNotFoundException {
        String userId = null;
        if (principals != null) {
            userId = principals.getPrimaryPrincipal().toString();
            try {
                User user = this.findUserManager(principals).getUser(userId);
                if (user != null) {
                    return user.getStatus();
                }
            }
            catch (NoSuchUserManagerException e) {
                throw new UserNotFoundException(userId, e.getMessage(), e);
            }
            catch (UserNotFoundTransientException e) {
                this.log.debug("Ignoring transient user error", (Throwable)e);
                return UserStatus.disabled;
            }
            catch (UserNotFoundException e) {
                throw e;
            }
            catch (RuntimeException e) {
                this.log.debug("Ignoring transient user error", (Throwable)e);
                return UserStatus.disabled;
            }
        }
        throw new UserNotFoundException(userId);
    }

    public UserManager findUserManager(PrincipalCollection principals) throws NoSuchUserManagerException {
        if (principals == null) {
            throw new NoSuchUserManagerException("Missing principals");
        }
        boolean isPrimary = true;
        for (String realmName : principals.getRealmNames()) {
            if (!isPrimary && !principals.fromRealm(realmName).contains(principals.getPrimaryPrincipal())) continue;
            for (UserManager userManager : this.userManagers) {
                if (!realmName.equals(userManager.getAuthenticationRealmName())) continue;
                return userManager;
            }
            isPrimary = false;
        }
        throw new NoSuchUserManagerException("User-manager not found for realm(s)", principals.getRealmNames().toString());
    }
}

