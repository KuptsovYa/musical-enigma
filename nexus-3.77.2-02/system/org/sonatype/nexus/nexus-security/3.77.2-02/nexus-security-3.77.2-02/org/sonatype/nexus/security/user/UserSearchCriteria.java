/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.user;

import java.util.HashSet;
import java.util.Set;

public class UserSearchCriteria {
    private String userId;
    private Set<String> oneOfRoleIds = new HashSet<String>();
    private String source;
    private String email;
    private Integer limit;

    public UserSearchCriteria() {
    }

    public UserSearchCriteria(String userId) {
        this.userId = userId;
    }

    public UserSearchCriteria(String userId, Set<String> oneOfRoleIds, String source) {
        this.userId = userId;
        this.oneOfRoleIds = oneOfRoleIds;
        this.source = source;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<String> getOneOfRoleIds() {
        return this.oneOfRoleIds;
    }

    public void setOneOfRoleIds(Set<String> oneOfRoleIds) {
        this.oneOfRoleIds = oneOfRoleIds;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getLimit() {
        return this.limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}

