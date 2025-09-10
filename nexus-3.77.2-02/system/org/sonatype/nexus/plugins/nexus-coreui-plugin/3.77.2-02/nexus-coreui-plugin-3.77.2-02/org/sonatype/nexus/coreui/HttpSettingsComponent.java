/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.annotation.ExceptionMetered
 *  com.codahale.metrics.annotation.Timed
 *  com.google.common.base.Preconditions
 *  com.softwarementors.extjs.djn.config.annotations.DirectAction
 *  com.softwarementors.extjs.djn.config.annotations.DirectMethod
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.validation.Valid
 *  javax.validation.constraints.NotNull
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.goodies.common.Time
 *  org.sonatype.nexus.common.text.Strings2
 *  org.sonatype.nexus.crypto.secrets.Secret
 *  org.sonatype.nexus.crypto.secrets.SecretsService
 *  org.sonatype.nexus.extdirect.DirectComponentSupport
 *  org.sonatype.nexus.httpclient.HttpClientManager
 *  org.sonatype.nexus.httpclient.config.AuthenticationConfiguration
 *  org.sonatype.nexus.httpclient.config.ConnectionConfiguration
 *  org.sonatype.nexus.httpclient.config.HttpClientConfiguration
 *  org.sonatype.nexus.httpclient.config.NtlmAuthenticationConfiguration
 *  org.sonatype.nexus.httpclient.config.ProxyConfiguration
 *  org.sonatype.nexus.httpclient.config.ProxyServerConfiguration
 *  org.sonatype.nexus.httpclient.config.UsernameAuthenticationConfiguration
 *  org.sonatype.nexus.rapture.PasswordPlaceholder
 *  org.sonatype.nexus.security.UserIdHelper
 *  org.sonatype.nexus.validation.Validate
 */
package org.sonatype.nexus.coreui;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Preconditions;
import com.softwarementors.extjs.djn.config.annotations.DirectAction;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.goodies.common.Time;
import org.sonatype.nexus.common.text.Strings2;
import org.sonatype.nexus.coreui.HttpSettingsXO;
import org.sonatype.nexus.crypto.secrets.Secret;
import org.sonatype.nexus.crypto.secrets.SecretsService;
import org.sonatype.nexus.extdirect.DirectComponentSupport;
import org.sonatype.nexus.httpclient.HttpClientManager;
import org.sonatype.nexus.httpclient.config.AuthenticationConfiguration;
import org.sonatype.nexus.httpclient.config.ConnectionConfiguration;
import org.sonatype.nexus.httpclient.config.HttpClientConfiguration;
import org.sonatype.nexus.httpclient.config.NtlmAuthenticationConfiguration;
import org.sonatype.nexus.httpclient.config.ProxyConfiguration;
import org.sonatype.nexus.httpclient.config.ProxyServerConfiguration;
import org.sonatype.nexus.httpclient.config.UsernameAuthenticationConfiguration;
import org.sonatype.nexus.rapture.PasswordPlaceholder;
import org.sonatype.nexus.security.UserIdHelper;
import org.sonatype.nexus.validation.Validate;

@Named
@Singleton
@DirectAction(action={"coreui_HttpSettings"})
public class HttpSettingsComponent
extends DirectComponentSupport {
    private final HttpClientManager httpClientManager;
    private final SecretsService secretsService;

    @Inject
    public HttpSettingsComponent(HttpClientManager httpClientManager, SecretsService secretsService) {
        this.httpClientManager = (HttpClientManager)Preconditions.checkNotNull((Object)httpClientManager);
        this.secretsService = (SecretsService)Preconditions.checkNotNull((Object)secretsService);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:settings:read"})
    public HttpSettingsXO read() {
        return this.convert(this.httpClientManager.getConfiguration());
    }

    private HttpSettingsXO convert(HttpClientConfiguration value) {
        HttpSettingsXO result = new HttpSettingsXO();
        if (value.getConnection() != null) {
            ConnectionConfiguration connection = value.getConnection();
            result.setUserAgentSuffix(connection.getUserAgentSuffix());
            result.setTimeout(connection.getTimeout() != null ? Integer.valueOf(connection.getTimeout().toSecondsI()) : null);
            result.setRetries(connection.getRetries());
        }
        if (value.getProxy() != null) {
            ProxyConfiguration proxy = value.getProxy();
            if (proxy.getHttp() != null) {
                this.configureHttpProxy(proxy.getHttp(), result);
            }
            if (proxy.getHttps() != null) {
                this.configureHttpsProxy(proxy.getHttps(), result);
            }
            if (proxy.getNonProxyHosts() != null) {
                result.setNonProxyHosts(Set.of(proxy.getNonProxyHosts()));
            }
        }
        return result;
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:settings:update"})
    @Validate
    public HttpSettingsXO update(@NotNull @Valid HttpSettingsXO settings) {
        HttpClientConfiguration previous = this.httpClientManager.getConfiguration();
        HttpClientConfiguration model = null;
        try {
            model = this.convert(settings, previous);
        }
        catch (Exception e) {
            this.removeSecrets(previous, model);
            throw e;
        }
        this.httpClientManager.setConfiguration(model);
        this.removeSecrets(previous, model);
        return this.read();
    }

    private HttpClientConfiguration convert(HttpSettingsXO value, HttpClientConfiguration previous) {
        ProxyServerConfiguration proxyConfig;
        HttpClientConfiguration result = this.httpClientManager.newConfiguration();
        if (!Strings2.isBlank((String)value.getUserAgentSuffix())) {
            this.ensureConnectionInitialized(result);
            result.getConnection().setUserAgentSuffix(value.getUserAgentSuffix());
        }
        if (value.getTimeout() != null) {
            this.ensureConnectionInitialized(result);
            result.getConnection().setTimeout(Time.seconds((long)value.getTimeout().intValue()));
        }
        if (value.getRetries() != null) {
            this.ensureConnectionInitialized(result);
            result.getConnection().setRetries(value.getRetries());
        }
        if (Boolean.TRUE.equals(value.getHttpEnabled())) {
            this.ensureProxyInitialized(result);
            proxyConfig = new ProxyServerConfiguration();
            proxyConfig.setEnabled(true);
            proxyConfig.setHost(value.getHttpHost());
            proxyConfig.setPort(value.getHttpPort().intValue());
            proxyConfig.setAuthentication(this.auth(value.getHttpAuthEnabled(), value.getHttpAuthUsername(), value.getHttpAuthPassword(), value.getHttpAuthNtlmHost(), value.getHttpAuthNtlmDomain(), this.getHttpSecret(previous)));
            result.getProxy().setHttp(proxyConfig);
        }
        if (Boolean.TRUE.equals(value.getHttpsEnabled())) {
            this.ensureProxyInitialized(result);
            proxyConfig = new ProxyServerConfiguration();
            proxyConfig.setEnabled(true);
            proxyConfig.setHost(value.getHttpsHost());
            proxyConfig.setPort(value.getHttpsPort().intValue());
            proxyConfig.setAuthentication(this.auth(value.getHttpsAuthEnabled(), value.getHttpsAuthUsername(), value.getHttpsAuthPassword(), value.getHttpsAuthNtlmHost(), value.getHttpsAuthNtlmDomain(), this.getHttpsSecret(previous)));
            result.getProxy().setHttps(proxyConfig);
        }
        if (value.getNonProxyHosts() != null) {
            this.ensureProxyInitialized(result);
            result.getProxy().setNonProxyHosts(value.getNonProxyHosts().toArray(new String[0]));
        }
        return result;
    }

    @Nullable
    private AuthenticationConfiguration auth(Boolean enabled, String username, String password, String host, String domain, Secret previous) {
        if (Boolean.FALSE.equals(enabled)) {
            return null;
        }
        if (host != null || domain != null) {
            NtlmAuthenticationConfiguration ntlmAuthConfig = new NtlmAuthenticationConfiguration();
            ntlmAuthConfig.setUsername(username);
            ntlmAuthConfig.setPassword(this.encrypt(password, previous));
            ntlmAuthConfig.setHost(host);
            ntlmAuthConfig.setDomain(domain);
            return ntlmAuthConfig;
        }
        UsernameAuthenticationConfiguration userAuthConfig = new UsernameAuthenticationConfiguration();
        userAuthConfig.setUsername(username);
        userAuthConfig.setPassword(this.encrypt(password, previous));
        return userAuthConfig;
    }

    private Secret encrypt(String password, Secret previous) {
        if (Strings2.isBlank((String)password) || PasswordPlaceholder.is((String)password)) {
            return previous;
        }
        return this.secretsService.encryptMaven("authentication-config", password.toCharArray(), UserIdHelper.get());
    }

    private void removeSecrets(HttpClientConfiguration previous, HttpClientConfiguration newConfig) {
        if (!Objects.equals(this.getSecretId(this.getHttpSecret(previous)), this.getSecretId(this.getHttpSecret(newConfig)))) {
            this.removeSecret(this.getHttpAuthConfig(previous));
        }
        if (!Objects.equals(this.getSecretId(this.getHttpsSecret(previous)), this.getSecretId(this.getHttpsSecret(newConfig)))) {
            this.removeSecret(this.getHttpsAuthConfig(previous));
        }
    }

    private void removeSecret(AuthenticationConfiguration authConfig) {
        if (authConfig != null) {
            if ("ntlm".equals(authConfig.getType())) {
                NtlmAuthenticationConfiguration ntlmAuth = (NtlmAuthenticationConfiguration)authConfig;
                this.secretsService.remove(ntlmAuth.getPassword());
            } else {
                UsernameAuthenticationConfiguration userNameAuth = (UsernameAuthenticationConfiguration)authConfig;
                this.secretsService.remove(userNameAuth.getPassword());
            }
        }
    }

    private Secret getHttpSecret(HttpClientConfiguration configuration) {
        return Optional.ofNullable(configuration).map(HttpClientConfiguration::getProxy).map(ProxyConfiguration::getHttp).map(ProxyServerConfiguration::getAuthentication).map(AuthenticationConfiguration::getSecret).orElse(null);
    }

    private Secret getHttpsSecret(HttpClientConfiguration configuration) {
        return Optional.ofNullable(configuration).map(HttpClientConfiguration::getProxy).map(ProxyConfiguration::getHttps).map(ProxyServerConfiguration::getAuthentication).map(AuthenticationConfiguration::getSecret).orElse(null);
    }

    private String getSecretId(Secret secret) {
        return Optional.ofNullable(secret).map(Secret::getId).orElse(null);
    }

    private AuthenticationConfiguration getHttpAuthConfig(HttpClientConfiguration configuration) {
        return Optional.ofNullable(configuration).map(HttpClientConfiguration::getProxy).map(ProxyConfiguration::getHttp).map(ProxyServerConfiguration::getAuthentication).orElse(null);
    }

    private AuthenticationConfiguration getHttpsAuthConfig(HttpClientConfiguration configuration) {
        return Optional.ofNullable(configuration).map(HttpClientConfiguration::getProxy).map(ProxyConfiguration::getHttps).map(ProxyServerConfiguration::getAuthentication).orElse(null);
    }

    private void ensureConnectionInitialized(HttpClientConfiguration configuration) {
        if (configuration.getConnection() == null) {
            configuration.setConnection(new ConnectionConfiguration());
        }
    }

    private void ensureProxyInitialized(HttpClientConfiguration configuration) {
        if (configuration.getProxy() == null) {
            configuration.setProxy(new ProxyConfiguration());
        }
    }

    private void configureHttpProxy(ProxyServerConfiguration http, HttpSettingsXO result) {
        result.setHttpEnabled(http.isEnabled());
        result.setHttpHost(http.getHost());
        result.setHttpPort(http.getPort());
        AuthenticationConfiguration authenticationConfiguration = http.getAuthentication();
        if (authenticationConfiguration instanceof UsernameAuthenticationConfiguration) {
            UsernameAuthenticationConfiguration auth = (UsernameAuthenticationConfiguration)authenticationConfiguration;
            result.setHttpAuthEnabled(true);
            result.setHttpAuthUsername(auth.getUsername());
            result.setHttpAuthPassword(PasswordPlaceholder.get((Secret)auth.getPassword()));
        } else {
            authenticationConfiguration = http.getAuthentication();
            if (authenticationConfiguration instanceof NtlmAuthenticationConfiguration) {
                NtlmAuthenticationConfiguration auth = (NtlmAuthenticationConfiguration)authenticationConfiguration;
                result.setHttpAuthEnabled(true);
                result.setHttpAuthUsername(auth.getUsername());
                result.setHttpAuthPassword(PasswordPlaceholder.get((Secret)auth.getPassword()));
                result.setHttpAuthNtlmHost(auth.getHost());
                result.setHttpAuthNtlmDomain(auth.getDomain());
            }
        }
    }

    private void configureHttpsProxy(ProxyServerConfiguration https, HttpSettingsXO result) {
        result.setHttpsEnabled(https.isEnabled());
        result.setHttpsHost(https.getHost());
        result.setHttpsPort(https.getPort());
        AuthenticationConfiguration authenticationConfiguration = https.getAuthentication();
        if (authenticationConfiguration instanceof UsernameAuthenticationConfiguration) {
            UsernameAuthenticationConfiguration auth = (UsernameAuthenticationConfiguration)authenticationConfiguration;
            result.setHttpsAuthEnabled(true);
            result.setHttpsAuthUsername(auth.getUsername());
            result.setHttpsAuthPassword(PasswordPlaceholder.get((Secret)auth.getPassword()));
        } else {
            authenticationConfiguration = https.getAuthentication();
            if (authenticationConfiguration instanceof NtlmAuthenticationConfiguration) {
                NtlmAuthenticationConfiguration auth = (NtlmAuthenticationConfiguration)authenticationConfiguration;
                result.setHttpsAuthEnabled(true);
                result.setHttpsAuthUsername(auth.getUsername());
                result.setHttpsAuthPassword(PasswordPlaceholder.get((Secret)auth.getPassword()));
                result.setHttpsAuthNtlmHost(auth.getHost());
                result.setHttpsAuthNtlmDomain(auth.getDomain());
            }
        }
    }
}

