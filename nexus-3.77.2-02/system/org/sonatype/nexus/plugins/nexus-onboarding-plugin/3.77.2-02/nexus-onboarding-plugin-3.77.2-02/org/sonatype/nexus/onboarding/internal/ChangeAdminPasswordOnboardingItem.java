/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.security.SecuritySystem
 *  org.sonatype.nexus.security.user.NoSuchUserManagerException
 *  org.sonatype.nexus.security.user.User
 *  org.sonatype.nexus.security.user.UserNotFoundException
 *  org.sonatype.nexus.security.user.UserStatus
 */
package org.sonatype.nexus.onboarding.internal;

import com.google.common.base.Preconditions;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.onboarding.OnboardingItem;
import org.sonatype.nexus.onboarding.OnboardingItemPriority;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.user.NoSuchUserManagerException;
import org.sonatype.nexus.security.user.User;
import org.sonatype.nexus.security.user.UserNotFoundException;
import org.sonatype.nexus.security.user.UserStatus;

@Named
@Singleton
public class ChangeAdminPasswordOnboardingItem
extends ComponentSupport
implements OnboardingItem {
    private final SecuritySystem securitySystem;

    @Inject
    public ChangeAdminPasswordOnboardingItem(SecuritySystem securitySystem) {
        this.securitySystem = (SecuritySystem)Preconditions.checkNotNull((Object)securitySystem);
    }

    @Override
    public String getType() {
        return "ChangeAdminPassword";
    }

    @Override
    public int getPriority() {
        return OnboardingItemPriority.CHANGE_ADMIN_PASSWORD_ONBOARDING;
    }

    @Override
    public boolean applies() {
        try {
            User user = this.securitySystem.getUser("admin", "default");
            return UserStatus.changepassword.equals((Object)user.getStatus());
        }
        catch (UserNotFoundException e) {
            this.log.trace("admin user not found in system, marking onboarding item as not applicable.", (Throwable)e);
        }
        catch (NoSuchUserManagerException e) {
            this.log.trace("default UserManager not found in system, marking onboarding item as not applicable.", (Throwable)e);
        }
        return false;
    }
}

