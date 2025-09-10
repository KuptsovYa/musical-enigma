/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.text.Strings2
 */
package org.sonatype.nexus.security.user;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.text.Strings2;
import org.sonatype.nexus.security.role.RoleIdentifier;
import org.sonatype.nexus.security.user.User;
import org.sonatype.nexus.security.user.UserManager;
import org.sonatype.nexus.security.user.UserSearchCriteria;

public abstract class AbstractUserManager
extends ComponentSupport
implements UserManager {
    protected Set<User> filterListInMemeory(Set<User> users, UserSearchCriteria criteria) {
        HashSet<User> result = new HashSet<User>();
        for (User user : users) {
            if (!this.userMatchesCriteria(user, criteria)) continue;
            result.add(user);
        }
        return result;
    }

    protected boolean userMatchesCriteria(User user, UserSearchCriteria criteria) {
        HashSet<String> userRoles = new HashSet<String>();
        if (user.getRoles() != null) {
            for (RoleIdentifier roleIdentifier : user.getRoles()) {
                userRoles.add(roleIdentifier.getRoleId());
            }
        }
        return this.matchesCriteria(user.getUserId(), user.getSource(), userRoles, criteria);
    }

    protected boolean matchesCriteria(String userId, String userSource, Collection<String> usersRoles, UserSearchCriteria criteria) {
        if (!Strings2.isBlank((String)criteria.getUserId()) && !userId.toLowerCase().startsWith(criteria.getUserId().toLowerCase())) {
            return false;
        }
        if (criteria.getSource() != null && !criteria.getSource().equals(userSource)) {
            return false;
        }
        if (criteria.getOneOfRoleIds() != null && !criteria.getOneOfRoleIds().isEmpty()) {
            HashSet<String> userRoles = new HashSet<String>();
            if (usersRoles != null) {
                userRoles.addAll(usersRoles);
            }
            if (Sets.intersection(criteria.getOneOfRoleIds(), userRoles).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}

