/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.Sets
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.eclipse.sisu.Description
 *  org.sonatype.nexus.common.text.Strings2
 */
package org.sonatype.nexus.security.user;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.eclipse.sisu.Description;
import org.sonatype.nexus.common.text.Strings2;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.config.CUserRoleMapping;
import org.sonatype.nexus.security.config.SecurityConfigurationManager;
import org.sonatype.nexus.security.user.AbstractReadOnlyUserManager;
import org.sonatype.nexus.security.user.NoSuchUserManagerException;
import org.sonatype.nexus.security.user.User;
import org.sonatype.nexus.security.user.UserNotFoundException;
import org.sonatype.nexus.security.user.UserSearchCriteria;

@Singleton
@Named(value="allConfigured")
@Description(value="All Users with Roles")
public class ConfiguredUsersUserManager
extends AbstractReadOnlyUserManager {
    public static final String SOURCE = "allConfigured";
    private final SecuritySystem securitySystem;
    private final SecurityConfigurationManager configuration;

    @Inject
    public ConfiguredUsersUserManager(SecuritySystem securitySystem, SecurityConfigurationManager configuration) {
        this.securitySystem = (SecuritySystem)Preconditions.checkNotNull((Object)securitySystem);
        this.configuration = (SecurityConfigurationManager)Preconditions.checkNotNull((Object)configuration);
    }

    @Override
    public String getSource() {
        return SOURCE;
    }

    @Override
    public Set<User> listUsers() {
        HashSet<User> users = new HashSet<User>();
        List<CUserRoleMapping> userRoleMappings = this.configuration.listUserRoleMappings();
        for (CUserRoleMapping userRoleMapping : userRoleMappings) {
            try {
                User user = this.securitySystem.getUser(userRoleMapping.getUserId(), userRoleMapping.getSource(), userRoleMapping.getRoles());
                if (user == null) continue;
                users.add(user);
            }
            catch (UserNotFoundException e) {
                this.log.warn("User: '{}' of source: '{}' could not be found", (Object)userRoleMapping.getUserId(), (Object)userRoleMapping.getSource());
                this.log.debug("Most likely caused by a user role mapping that is invalid", (Throwable)e);
            }
            catch (NoSuchUserManagerException e) {
                this.log.warn("User: '{}' of source: '{}' could not be found", new Object[]{userRoleMapping.getUserId(), userRoleMapping.getSource(), e});
            }
        }
        return users;
    }

    @Override
    public Set<String> listUserIds() {
        HashSet<String> userIds = new HashSet<String>();
        List<CUserRoleMapping> userRoleMappings = this.configuration.listUserRoleMappings();
        for (CUserRoleMapping userRoleMapping : userRoleMappings) {
            String userId = userRoleMapping.getUserId();
            if (Strings2.isBlank((String)userId)) continue;
            userIds.add(userId);
        }
        return userIds;
    }

    @Override
    public User getUser(String userId) {
        return null;
    }

    @Override
    public User getUser(String userId, Set<String> roleIds) {
        return null;
    }

    @Override
    public Set<User> searchUsers(UserSearchCriteria criteria) {
        if (this.getSource().equals(criteria.getSource())) {
            return this.filterListInMemeory(this.listUsers(), criteria);
        }
        return new HashSet<User>();
    }

    @Override
    protected boolean matchesCriteria(String userId, String userSource, Collection<String> usersRoles, UserSearchCriteria criteria) {
        if (!Strings2.isBlank((String)criteria.getUserId()) && !userId.toLowerCase().startsWith(criteria.getUserId().toLowerCase())) {
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

    @Override
    public String getAuthenticationRealmName() {
        return null;
    }

    @Override
    public boolean isConfigured() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

