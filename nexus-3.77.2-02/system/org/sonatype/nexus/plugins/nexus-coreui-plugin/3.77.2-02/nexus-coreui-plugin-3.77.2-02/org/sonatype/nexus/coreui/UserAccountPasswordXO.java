/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.NotEmpty
 */
package org.sonatype.nexus.coreui;

import javax.validation.constraints.NotEmpty;

public class UserAccountPasswordXO {
    @NotEmpty
    private String authToken;
    @NotEmpty
    private String password;

    public String getAuthToken() {
        return this.authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

