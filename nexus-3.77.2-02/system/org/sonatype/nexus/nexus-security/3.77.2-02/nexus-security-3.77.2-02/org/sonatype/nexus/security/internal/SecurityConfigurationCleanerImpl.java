/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.goodies.common.ComponentSupport
 */
package org.sonatype.nexus.security.internal;

import java.util.ConcurrentModificationException;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.security.config.CRole;
import org.sonatype.nexus.security.config.CUserRoleMapping;
import org.sonatype.nexus.security.config.SecurityConfiguration;
import org.sonatype.nexus.security.config.SecurityConfigurationCleaner;
import org.sonatype.nexus.security.role.NoSuchRoleException;
import org.sonatype.nexus.security.user.NoSuchRoleMappingException;

@Named
@Singleton
public class SecurityConfigurationCleanerImpl
extends ComponentSupport
implements SecurityConfigurationCleaner {
    @Override
    public void privilegeRemoved(SecurityConfiguration configuration, String privilegeId) {
        this.log.debug("Cleaning privilege id {} from roles.", (Object)privilegeId);
        for (CRole role : configuration.getRoles()) {
            boolean concurrentlyUpdated;
            do {
                concurrentlyUpdated = false;
                CRole currentRole = configuration.getRole(role.getId());
                if (currentRole == null || !currentRole.getPrivileges().contains(privilegeId)) continue;
                this.log.debug("removing privilege {} from role {}", (Object)privilegeId, (Object)currentRole.getId());
                currentRole.removePrivilege(privilegeId);
                try {
                    configuration.updateRole(currentRole);
                }
                catch (NoSuchRoleException noSuchRoleException) {
                }
                catch (ConcurrentModificationException e) {
                    concurrentlyUpdated = true;
                }
            } while (concurrentlyUpdated);
        }
    }

    @Override
    public void roleRemoved(SecurityConfiguration configuration, String roleId) {
        boolean concurrentlyUpdated;
        this.log.debug("Cleaning role id {} from users and roles.", (Object)roleId);
        for (CRole role : configuration.getRoles()) {
            do {
                concurrentlyUpdated = false;
                CRole currentRole = configuration.getRole(role.getId());
                if (currentRole == null || !currentRole.getRoles().contains(roleId)) continue;
                this.log.debug("removing ref to role {} from role {}", (Object)roleId, (Object)currentRole.getId());
                currentRole.removeRole(roleId);
                try {
                    configuration.updateRole(currentRole);
                }
                catch (NoSuchRoleException noSuchRoleException) {
                }
                catch (ConcurrentModificationException e) {
                    concurrentlyUpdated = true;
                }
            } while (concurrentlyUpdated);
        }
        for (CUserRoleMapping mapping : configuration.getUserRoleMappings()) {
            do {
                concurrentlyUpdated = false;
                CUserRoleMapping currentMapping = configuration.getUserRoleMapping(mapping.getUserId(), mapping.getSource());
                if (currentMapping == null || !currentMapping.getRoles().contains(roleId)) continue;
                this.log.debug("removing ref to role {} from user {}", (Object)roleId, (Object)currentMapping.getUserId());
                currentMapping.removeRole(roleId);
                try {
                    configuration.updateUserRoleMapping(currentMapping);
                }
                catch (NoSuchRoleMappingException e) {
                }
                catch (ConcurrentModificationException e) {
                    concurrentlyUpdated = true;
                }
            } while (concurrentlyUpdated);
        }
    }
}

