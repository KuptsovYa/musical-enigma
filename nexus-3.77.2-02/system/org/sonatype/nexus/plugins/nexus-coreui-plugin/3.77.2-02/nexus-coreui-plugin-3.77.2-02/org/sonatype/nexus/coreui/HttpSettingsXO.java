/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.Max
 *  javax.validation.constraints.Min
 *  org.sonatype.nexus.httpclient.config.NonProxyHosts
 *  org.sonatype.nexus.validation.constraint.Hostname
 *  org.sonatype.nexus.validation.constraint.PortNumber
 */
package org.sonatype.nexus.coreui;

import java.util.Set;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.sonatype.nexus.httpclient.config.NonProxyHosts;
import org.sonatype.nexus.validation.constraint.Hostname;
import org.sonatype.nexus.validation.constraint.PortNumber;

public class HttpSettingsXO {
    private String userAgentSuffix;
    @Min(value=1L)
    @Max(value=3600L)
    private @Min(value=1L) @Max(value=3600L) Integer timeout;
    @Min(value=0L)
    @Max(value=10L)
    private @Min(value=0L) @Max(value=10L) Integer retries;
    private Boolean httpEnabled;
    @Hostname
    private String httpHost;
    @PortNumber
    private Integer httpPort;
    private Boolean httpAuthEnabled;
    private String httpAuthUsername;
    private String httpAuthPassword;
    private String httpAuthNtlmHost;
    private String httpAuthNtlmDomain;
    private Boolean httpsEnabled;
    @Hostname
    private String httpsHost;
    @PortNumber
    private Integer httpsPort;
    private Boolean httpsAuthEnabled;
    private String httpsAuthUsername;
    private String httpsAuthPassword;
    private String httpsAuthNtlmHost;
    private String httpsAuthNtlmDomain;
    @NonProxyHosts
    private Set<String> nonProxyHosts;

    public String getUserAgentSuffix() {
        return this.userAgentSuffix;
    }

    public void setUserAgentSuffix(String userAgentSuffix) {
        this.userAgentSuffix = userAgentSuffix;
    }

    public Integer getTimeout() {
        return this.timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getRetries() {
        return this.retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Boolean getHttpEnabled() {
        return this.httpEnabled;
    }

    public void setHttpEnabled(Boolean httpEnabled) {
        this.httpEnabled = httpEnabled;
    }

    public String getHttpHost() {
        return this.httpHost;
    }

    public void setHttpHost(String httpHost) {
        this.httpHost = httpHost;
    }

    public Integer getHttpPort() {
        return this.httpPort;
    }

    public void setHttpPort(Integer httpPort) {
        this.httpPort = httpPort;
    }

    public Boolean getHttpAuthEnabled() {
        return this.httpAuthEnabled;
    }

    public void setHttpAuthEnabled(Boolean httpAuthEnabled) {
        this.httpAuthEnabled = httpAuthEnabled;
    }

    public String getHttpAuthUsername() {
        return this.httpAuthUsername;
    }

    public void setHttpAuthUsername(String httpAuthUsername) {
        this.httpAuthUsername = httpAuthUsername;
    }

    public String getHttpAuthPassword() {
        return this.httpAuthPassword;
    }

    public void setHttpAuthPassword(String httpAuthPassword) {
        this.httpAuthPassword = httpAuthPassword;
    }

    public String getHttpAuthNtlmHost() {
        return this.httpAuthNtlmHost;
    }

    public void setHttpAuthNtlmHost(String httpAuthNtlmHost) {
        this.httpAuthNtlmHost = httpAuthNtlmHost;
    }

    public String getHttpAuthNtlmDomain() {
        return this.httpAuthNtlmDomain;
    }

    public void setHttpAuthNtlmDomain(String httpAuthNtlmDomain) {
        this.httpAuthNtlmDomain = httpAuthNtlmDomain;
    }

    public Boolean getHttpsEnabled() {
        return this.httpsEnabled;
    }

    public void setHttpsEnabled(Boolean httpsEnabled) {
        this.httpsEnabled = httpsEnabled;
    }

    public String getHttpsHost() {
        return this.httpsHost;
    }

    public void setHttpsHost(String httpsHost) {
        this.httpsHost = httpsHost;
    }

    public Integer getHttpsPort() {
        return this.httpsPort;
    }

    public void setHttpsPort(Integer httpsPort) {
        this.httpsPort = httpsPort;
    }

    public Boolean getHttpsAuthEnabled() {
        return this.httpsAuthEnabled;
    }

    public void setHttpsAuthEnabled(Boolean httpsAuthEnabled) {
        this.httpsAuthEnabled = httpsAuthEnabled;
    }

    public String getHttpsAuthUsername() {
        return this.httpsAuthUsername;
    }

    public void setHttpsAuthUsername(String httpsAuthUsername) {
        this.httpsAuthUsername = httpsAuthUsername;
    }

    public String getHttpsAuthPassword() {
        return this.httpsAuthPassword;
    }

    public void setHttpsAuthPassword(String httpsAuthPassword) {
        this.httpsAuthPassword = httpsAuthPassword;
    }

    public String getHttpsAuthNtlmHost() {
        return this.httpsAuthNtlmHost;
    }

    public void setHttpsAuthNtlmHost(String httpsAuthNtlmHost) {
        this.httpsAuthNtlmHost = httpsAuthNtlmHost;
    }

    public String getHttpsAuthNtlmDomain() {
        return this.httpsAuthNtlmDomain;
    }

    public void setHttpsAuthNtlmDomain(String httpsAuthNtlmDomain) {
        this.httpsAuthNtlmDomain = httpsAuthNtlmDomain;
    }

    public Set<String> getNonProxyHosts() {
        return this.nonProxyHosts;
    }

    public void setNonProxyHosts(Set<String> nonProxyHosts) {
        this.nonProxyHosts = nonProxyHosts;
    }

    public String toString() {
        return "HttpSettingsXO{userAgentSuffix='" + this.userAgentSuffix + "', timeout=" + this.timeout + ", retries=" + this.retries + ", httpEnabled=" + this.httpEnabled + ", httpHost='" + this.httpHost + "', httpPort=" + this.httpPort + ", httpAuthEnabled=" + this.httpAuthEnabled + ", httpAuthUsername='" + this.httpAuthUsername + "', httpAuthPassword='" + this.httpAuthPassword + "', httpAuthNtlmHost='" + this.httpAuthNtlmHost + "', httpAuthNtlmDomain='" + this.httpAuthNtlmDomain + "', httpsEnabled=" + this.httpsEnabled + ", httpsHost='" + this.httpsHost + "', httpsPort=" + this.httpsPort + ", httpsAuthEnabled=" + this.httpsAuthEnabled + ", httpsAuthUsername='" + this.httpsAuthUsername + "', httpsAuthPassword='" + this.httpsAuthPassword + "', httpsAuthNtlmHost='" + this.httpsAuthNtlmHost + "', httpsAuthNtlmDomain='" + this.httpsAuthNtlmDomain + "', nonProxyHosts=" + this.nonProxyHosts + "}";
    }
}

