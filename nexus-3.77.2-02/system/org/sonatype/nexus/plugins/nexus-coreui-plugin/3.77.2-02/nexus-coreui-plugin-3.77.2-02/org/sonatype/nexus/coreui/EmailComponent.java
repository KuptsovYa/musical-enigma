/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.annotation.ExceptionMetered
 *  com.codahale.metrics.annotation.Timed
 *  com.google.common.base.Preconditions
 *  com.softwarementors.extjs.djn.config.annotations.DirectAction
 *  com.softwarementors.extjs.djn.config.annotations.DirectMethod
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.validation.Valid
 *  javax.validation.constraints.Email
 *  javax.validation.constraints.NotNull
 *  org.apache.commons.mail.EmailException
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.nexus.email.EmailConfiguration
 *  org.sonatype.nexus.email.EmailManager
 *  org.sonatype.nexus.extdirect.DirectComponentSupport
 *  org.sonatype.nexus.rapture.PasswordPlaceholder
 *  org.sonatype.nexus.validation.Validate
 */
package org.sonatype.nexus.coreui;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Preconditions;
import com.softwarementors.extjs.djn.config.annotations.DirectAction;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import org.apache.commons.mail.EmailException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.nexus.coreui.EmailConfigurationXO;
import org.sonatype.nexus.email.EmailConfiguration;
import org.sonatype.nexus.email.EmailManager;
import org.sonatype.nexus.extdirect.DirectComponentSupport;
import org.sonatype.nexus.rapture.PasswordPlaceholder;
import org.sonatype.nexus.validation.Validate;

@Named
@Singleton
@DirectAction(action={"coreui_Email"})
public class EmailComponent
extends DirectComponentSupport {
    private final EmailManager emailManager;

    @Inject
    public EmailComponent(EmailManager emailManager) {
        this.emailManager = (EmailManager)Preconditions.checkNotNull((Object)emailManager);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:settings:read"})
    public EmailConfigurationXO read() {
        return this.convert(this.emailManager.getConfiguration());
    }

    EmailConfigurationXO convert(EmailConfiguration value) {
        return new EmailConfigurationXO(value.isEnabled(), value.getHost(), value.getPort(), value.getUsername(), value.getPassword() != null ? PasswordPlaceholder.get() : null, value.getFromAddress(), value.getSubjectPrefix(), value.isStartTlsEnabled(), value.isStartTlsRequired(), value.isSslOnConnectEnabled(), value.isSslCheckServerIdentityEnabled(), value.isNexusTrustStoreEnabled());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:settings:update"})
    @Validate
    public EmailConfigurationXO update(@NotNull @Valid EmailConfigurationXO configuration) {
        this.emailManager.setConfiguration(this.convert(configuration), configuration.getPassword());
        return this.read();
    }

    EmailConfiguration convert(EmailConfigurationXO value) {
        EmailConfiguration emailConfiguration = this.emailManager.newConfiguration();
        emailConfiguration.setEnabled(value.isEnabled());
        emailConfiguration.setHost(value.getHost());
        emailConfiguration.setPort(value.getPort());
        emailConfiguration.setUsername(value.getUsername());
        emailConfiguration.setFromAddress(value.getFromAddress());
        emailConfiguration.setSubjectPrefix(value.getSubjectPrefix());
        emailConfiguration.setStartTlsEnabled(value.isStartTlsEnabled());
        emailConfiguration.setStartTlsRequired(value.isStartTlsRequired());
        emailConfiguration.setSslOnConnectEnabled(value.isSslOnConnectEnabled());
        emailConfiguration.setSslCheckServerIdentityEnabled(value.isSslCheckServerIdentityEnabled());
        emailConfiguration.setNexusTrustStoreEnabled(value.isNexusTrustStoreEnabled());
        return emailConfiguration;
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:settings:update"})
    @Validate
    public void sendVerification(@NotNull @Valid EmailConfigurationXO configuration, @NotNull @Email String address) throws EmailException {
        this.emailManager.sendVerification(this.convert(configuration), configuration.getPassword(), address);
    }
}

