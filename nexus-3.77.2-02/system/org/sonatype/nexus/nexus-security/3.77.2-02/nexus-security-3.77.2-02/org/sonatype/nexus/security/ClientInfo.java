/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security;

import java.util.Objects;

public class ClientInfo {
    private final String userid;
    private final String remoteIP;
    private final String userAgent;
    private final String path;

    private ClientInfo(Builder builder) {
        this.userid = builder.userId;
        this.remoteIP = builder.remoteIP;
        this.userAgent = builder.userAgent;
        this.path = builder.path;
    }

    public String getRemoteIP() {
        return this.remoteIP;
    }

    public String getUserid() {
        return this.userid;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public String getPath() {
        return this.path;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ClientInfo that = (ClientInfo)o;
        return Objects.equals(this.userid, that.userid) && Objects.equals(this.remoteIP, that.remoteIP) && Objects.equals(this.userAgent, that.userAgent) && Objects.equals(this.path, that.path);
    }

    public int hashCode() {
        return Objects.hash(this.userid, this.remoteIP, this.userAgent, this.path);
    }

    public String toString() {
        return "ClientInfo{userid='" + this.userid + "', remoteIP='" + this.remoteIP + "', userAgent='" + this.userAgent + "', path='" + this.path + "'}";
    }

    public static class Builder {
        private String userId;
        private String remoteIP;
        private String userAgent;
        private String path;

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder remoteIP(String remoteIP) {
            this.remoteIP = remoteIP;
            return this;
        }

        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public ClientInfo build() {
            return new ClientInfo(this);
        }
    }
}

