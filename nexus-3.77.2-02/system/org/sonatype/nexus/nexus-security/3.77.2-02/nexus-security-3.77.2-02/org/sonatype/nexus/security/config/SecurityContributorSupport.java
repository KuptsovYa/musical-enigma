/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.StringUtils
 */
package org.sonatype.nexus.security.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.sonatype.nexus.security.config.SecurityContributor;
import org.sonatype.nexus.security.config.memory.MemoryCPrivilege;
import org.sonatype.nexus.security.config.memory.MemoryCRole;

public abstract class SecurityContributorSupport
implements SecurityContributor {
    protected static final String TYPE_APPLICATION = "application";
    protected static final String APPLICATION_DOMAIN = "domain";
    protected static final String APPLICATION_ACTIONS = "actions";
    protected static final String ACTION_CREATE_ONLY = "create";
    protected static final String ACTION_READ = "read";
    protected static final String ACTION_UPDATE_ONLY = "update";
    protected static final String ACTION_DELETE_ONLY = "delete";
    protected static final String ACTION_CREATE = "create,read";
    protected static final String ACTION_UPDATE = "update,read";
    protected static final String ACTION_DELETE = "delete,read";
    protected static final String ACTION_ALL = "*";
    protected static final String TYPE_WILDCARD = "wildcard";
    protected static final String WILDCARD_PATTERN = "pattern";
    protected static final String ALL_DESCRIPTION_BASE = "All permissions for ";
    protected static final String CREATE_DESCRIPTION_BASE = "Create permission for ";
    protected static final String READ_DESCRIPTION_BASE = "Read permission for ";
    protected static final String UPDATE_DESCRIPTION_BASE = "Update permission for ";
    protected static final String DELETE_DESCRIPTION_BASE = "Delete permission for ";

    protected MemoryCPrivilege createWildcardPrivilege(String id, String description, String pattern) {
        return new MemoryCPrivilege.MemoryCPrivilegeBuilder(id).description(description).type(TYPE_WILDCARD).readOnly(true).name(id).property(WILDCARD_PATTERN, pattern).build();
    }

    protected MemoryCPrivilege createApplicationPrivilege(String id, String description, String domain, String actions) {
        return new MemoryCPrivilege.MemoryCPrivilegeBuilder(id).description(description).type(TYPE_APPLICATION).readOnly(true).name(id).property(APPLICATION_DOMAIN, domain).property(APPLICATION_ACTIONS, actions).build();
    }

    protected List<MemoryCPrivilege> createCrudApplicationPrivileges(String idBase, String domain) {
        return this.doCreateCrudApplicationPrivileges(idBase, domain, false);
    }

    protected List<MemoryCPrivilege> createCrudAndAllApplicationPrivileges(String idBase, String domain) {
        return this.doCreateCrudApplicationPrivileges(idBase, domain, true);
    }

    private List<MemoryCPrivilege> doCreateCrudApplicationPrivileges(String idBase, String domain, boolean addAll) {
        ArrayList<MemoryCPrivilege> results = new ArrayList<MemoryCPrivilege>();
        String domainUpper = StringUtils.capitalize((String)domain);
        if (addAll) {
            results.add(this.createApplicationPrivilege(idBase + "-all", ALL_DESCRIPTION_BASE + domainUpper, domain, ACTION_ALL));
        }
        results.add(this.createApplicationPrivilege(idBase + "-create", CREATE_DESCRIPTION_BASE + domainUpper, domain, ACTION_CREATE));
        results.add(this.createApplicationPrivilege(idBase + "-read", READ_DESCRIPTION_BASE + domainUpper, domain, ACTION_READ));
        results.add(this.createApplicationPrivilege(idBase + "-update", UPDATE_DESCRIPTION_BASE + domainUpper, domain, ACTION_UPDATE));
        results.add(this.createApplicationPrivilege(idBase + "-delete", DELETE_DESCRIPTION_BASE + domainUpper, domain, ACTION_DELETE));
        return results;
    }

    protected MemoryCRole createRole(String id, String name, String description, String ... privileges) {
        MemoryCRole role = new MemoryCRole();
        role.setId(id);
        role.setName(name);
        role.setDescription(description);
        role.setReadOnly(true);
        Arrays.stream(privileges).forEach(role::addPrivilege);
        return role;
    }
}

