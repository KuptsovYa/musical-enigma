/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.Email
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.NotNull
 *  org.sonatype.nexus.validation.constraint.Hostname
 *  org.sonatype.nexus.validation.constraint.PortNumber
 */
package org.sonatype.nexus.coreui;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.sonatype.nexus.validation.constraint.Hostname;
import org.sonatype.nexus.validation.constraint.PortNumber;

public class EmailConfigurationXO {
    private boolean enabled;
    @Hostname
    @NotBlank
    private String host;
    @PortNumber
    @NotNull
    private int port;
    private String username;
    private String password;
    @Email
    @NotBlank
    private String fromAddress;
    private String subjectPrefix;
    private boolean startTlsEnabled;
    private boolean startTlsRequired;
    private boolean sslOnConnectEnabled;
    private boolean sslCheckServerIdentityEnabled;
    private boolean nexusTrustStoreEnabled;

    public EmailConfigurationXO(boolean enabled, String host, int port, String username, String password, String fromAddress, String subjectPrefix, boolean startTlsEnabled, boolean startTlsRequired, boolean sslOnConnectEnabled, boolean sslCheckServerIdentityEnabled, boolean nexusTrustStoreEnabled) {
        this.enabled = enabled;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.fromAddress = fromAddress;
        this.subjectPrefix = subjectPrefix;
        this.startTlsEnabled = startTlsEnabled;
        this.startTlsRequired = startTlsRequired;
        this.sslOnConnectEnabled = sslOnConnectEnabled;
        this.sslCheckServerIdentityEnabled = sslCheckServerIdentityEnabled;
        this.nexusTrustStoreEnabled = nexusTrustStoreEnabled;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFromAddress() {
        return this.fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getSubjectPrefix() {
        return this.subjectPrefix;
    }

    public void setSubjectPrefix(String subjectPrefix) {
        this.subjectPrefix = subjectPrefix;
    }

    public boolean isStartTlsEnabled() {
        return this.startTlsEnabled;
    }

    public void setStartTlsEnabled(boolean startTlsEnabled) {
        this.startTlsEnabled = startTlsEnabled;
    }

    public boolean isStartTlsRequired() {
        return this.startTlsRequired;
    }

    public void setStartTlsRequired(boolean startTlsRequired) {
        this.startTlsRequired = startTlsRequired;
    }

    public boolean isSslOnConnectEnabled() {
        return this.sslOnConnectEnabled;
    }

    public void setSslOnConnectEnabled(boolean sslOnConnectEnabled) {
        this.sslOnConnectEnabled = sslOnConnectEnabled;
    }

    public boolean isSslCheckServerIdentityEnabled() {
        return this.sslCheckServerIdentityEnabled;
    }

    public void setSslCheckServerIdentityEnabled(boolean sslCheckServerIdentityEnabled) {
        this.sslCheckServerIdentityEnabled = sslCheckServerIdentityEnabled;
    }

    public boolean isNexusTrustStoreEnabled() {
        return this.nexusTrustStoreEnabled;
    }

    public void setNexusTrustStoreEnabled(boolean nexusTrustStoreEnabled) {
        this.nexusTrustStoreEnabled = nexusTrustStoreEnabled;
    }
}

