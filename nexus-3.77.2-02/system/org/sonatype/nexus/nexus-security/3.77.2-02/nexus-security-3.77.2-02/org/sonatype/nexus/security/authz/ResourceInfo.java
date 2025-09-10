/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.authz;

public class ResourceInfo {
    private final String accessProtocol;
    private final String accessMethod;
    private final String action;
    private final String accessedUri;

    public ResourceInfo(String accessProtocol, String accessMethod, String action, String accessedUri) {
        this.accessProtocol = accessProtocol;
        this.accessMethod = accessMethod;
        this.action = action;
        this.accessedUri = accessedUri;
    }

    public String getAccessProtocol() {
        return this.accessProtocol;
    }

    public String getAccessMethod() {
        return this.accessMethod;
    }

    public String getAction() {
        return this.action;
    }

    public String getAccessedUri() {
        return this.accessedUri;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ResourceInfo that = (ResourceInfo)o;
        if (this.accessMethod != null ? !this.accessMethod.equals(that.accessMethod) : that.accessMethod != null) {
            return false;
        }
        if (this.accessProtocol != null ? !this.accessProtocol.equals(that.accessProtocol) : that.accessProtocol != null) {
            return false;
        }
        if (this.accessedUri != null ? !this.accessedUri.equals(that.accessedUri) : that.accessedUri != null) {
            return false;
        }
        return !(this.action != null ? !this.action.equals(that.action) : that.action != null);
    }

    public int hashCode() {
        int result = this.accessProtocol != null ? this.accessProtocol.hashCode() : 0;
        result = 31 * result + (this.accessMethod != null ? this.accessMethod.hashCode() : 0);
        result = 31 * result + (this.action != null ? this.action.hashCode() : 0);
        result = 31 * result + (this.accessedUri != null ? this.accessedUri.hashCode() : 0);
        return result;
    }
}

