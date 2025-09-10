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
 *  org.sonatype.nexus.common.app.FreezeService
 *  org.sonatype.nexus.extdirect.DirectComponentSupport
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
import javax.validation.constraints.NotNull;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.nexus.common.app.FreezeService;
import org.sonatype.nexus.coreui.FreezeStatusXO;
import org.sonatype.nexus.extdirect.DirectComponentSupport;
import org.sonatype.nexus.validation.Validate;

@Named
@Singleton
@DirectAction(action={"coreui_Freeze"})
class FreezeComponent
extends DirectComponentSupport {
    private final FreezeService freezeService;

    @Inject
    public FreezeComponent(FreezeService freezeService) {
        this.freezeService = (FreezeService)Preconditions.checkNotNull((Object)freezeService);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    public FreezeStatusXO read() {
        return this.buildStatus();
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:*"})
    @Validate
    public FreezeStatusXO update(@NotNull @Valid FreezeStatusXO freezeStatusXO) {
        if (freezeStatusXO.isFrozen()) {
            this.freezeService.requestFreeze("UI request");
        } else {
            this.freezeService.cancelFreeze();
        }
        return this.buildStatus();
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:*"})
    @Validate
    public FreezeStatusXO forceRelease() {
        this.freezeService.cancelAllFreezeRequests();
        return this.buildStatus();
    }

    private FreezeStatusXO buildStatus() {
        FreezeStatusXO freezeStatus = new FreezeStatusXO();
        freezeStatus.setFrozen(this.freezeService.isFrozen());
        return freezeStatus;
    }
}

