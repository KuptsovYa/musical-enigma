/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.annotation.ExceptionMetered
 *  com.codahale.metrics.annotation.Timed
 *  com.softwarementors.extjs.djn.config.annotations.DirectAction
 *  com.softwarementors.extjs.djn.config.annotations.DirectMethod
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.nexus.extdirect.DirectComponentSupport
 *  org.sonatype.nexus.repository.webhooks.RepositoryWebhook
 *  org.sonatype.nexus.webhooks.GlobalWebhook
 *  org.sonatype.nexus.webhooks.WebhookService
 *  org.sonatype.nexus.webhooks.WebhookType
 */
package org.sonatype.nexus.coreui;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.softwarementors.extjs.djn.config.annotations.DirectAction;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.nexus.coreui.ReferenceXO;
import org.sonatype.nexus.extdirect.DirectComponentSupport;
import org.sonatype.nexus.repository.webhooks.RepositoryWebhook;
import org.sonatype.nexus.webhooks.GlobalWebhook;
import org.sonatype.nexus.webhooks.WebhookService;
import org.sonatype.nexus.webhooks.WebhookType;

@Named
@Singleton
@DirectAction(action={"coreui_Webhook"})
public class WebhookComponent
extends DirectComponentSupport {
    private final WebhookService webhookService;

    @Inject
    public WebhookComponent(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:settings:read"})
    public List<ReferenceXO> listWithTypeGlobal() {
        return this.findWebhooksWithType(GlobalWebhook.TYPE);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:settings:read"})
    public List<ReferenceXO> listWithTypeRepository() {
        return this.findWebhooksWithType(RepositoryWebhook.TYPE);
    }

    private List<ReferenceXO> findWebhooksWithType(WebhookType type) {
        return this.webhookService.getWebhooks().stream().filter(webhook -> webhook.getType() == type).map(webhook -> new ReferenceXO(webhook.getName(), webhook.getName())).collect(Collectors.toList());
    }
}

