/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.internal.rest;

import org.sonatype.nexus.security.user.UserManager;

public class ApiUserSource {
    private String id;
    private String name;

    public ApiUserSource(UserManager manager) {
        this.id = manager.getSource();
        this.name = manager.getAuthenticationRealmName();
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}

