/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.apache.shiro.subject.PrincipalCollection
 *  org.apache.shiro.subject.Subject
 */
package org.sonatype.nexus.security.anonymous;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.sonatype.nexus.security.anonymous.AnonymousPrincipalCollection;
import org.sonatype.nexus.security.user.UserManager;

public class AnonymousHelper {
    private AnonymousHelper() {
    }

    public static boolean isAnonymous(@Nullable Subject subject) {
        return subject != null && subject.getPrincipals() instanceof AnonymousPrincipalCollection;
    }

    public static boolean isAnonymous(@Nullable PrincipalCollection principals) {
        return principals instanceof AnonymousPrincipalCollection;
    }

    public static List<String> getAuthenticationRealms(List<UserManager> userManagers) {
        return userManagers.stream().map(UserManager::getAuthenticationRealmName).filter(Objects::nonNull).map(realm -> realm.equals("NexusAuthenticatingRealm") ? "NexusAuthorizingRealm" : realm).collect(Collectors.toList());
    }
}

