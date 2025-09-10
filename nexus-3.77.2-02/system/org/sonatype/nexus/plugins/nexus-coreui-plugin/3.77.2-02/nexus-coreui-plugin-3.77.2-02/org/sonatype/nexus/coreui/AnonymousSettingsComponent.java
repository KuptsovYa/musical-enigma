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
 *  javax.validation.constraints.NotNull
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.nexus.extdirect.DirectComponentSupport
 *  org.sonatype.nexus.security.anonymous.AnonymousConfiguration
 *  org.sonatype.nexus.security.anonymous.AnonymousManager
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
import javax.validation.constraints.NotNull;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.nexus.coreui.AnonymousSettingsXO;
import org.sonatype.nexus.extdirect.DirectComponentSupport;
import org.sonatype.nexus.security.anonymous.AnonymousConfiguration;
import org.sonatype.nexus.security.anonymous.AnonymousManager;

@Named
@Singleton
@DirectAction(action={"coreui_AnonymousSettings"})
public class AnonymousSettingsComponent
extends DirectComponentSupport {
    private final AnonymousManager anonymousManager;

    @Inject
    public AnonymousSettingsComponent(AnonymousManager anonymousManager) {
        this.anonymousManager = (AnonymousManager)Preconditions.checkNotNull((Object)anonymousManager);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:settings:read"})
    public AnonymousSettingsXO read() {
        AnonymousConfiguration config = this.anonymousManager.getConfiguration();
        AnonymousSettingsXO xo = new AnonymousSettingsXO();
        xo.setEnabled(config.isEnabled());
        xo.setUserId(config.getUserId());
        xo.setRealmName(config.getRealmName());
        return xo;
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:settings:update"})
    public AnonymousSettingsXO update(@NotNull @Valid AnonymousSettingsXO anonymousXO) {
        AnonymousConfiguration configuration = this.anonymousManager.newConfiguration();
        configuration.setEnabled(anonymousXO.getEnabled().booleanValue());
        configuration.setRealmName(anonymousXO.getRealmName());
        configuration.setUserId(anonymousXO.getUserId());
        this.anonymousManager.setConfiguration(configuration);
        return this.read();
    }
}

