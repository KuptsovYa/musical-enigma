/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.annotation.ExceptionMetered
 *  com.codahale.metrics.annotation.Timed
 *  com.google.common.base.Preconditions
 *  com.google.inject.Key
 *  com.softwarementors.extjs.djn.config.annotations.DirectAction
 *  com.softwarementors.extjs.djn.config.annotations.DirectMethod
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.validation.Valid
 *  javax.validation.constraints.NotNull
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.apache.shiro.realm.Realm
 *  org.eclipse.sisu.inject.BeanLocator
 *  org.sonatype.nexus.extdirect.DirectComponentSupport
 *  org.sonatype.nexus.security.realm.RealmManager
 *  org.sonatype.nexus.validation.Validate
 */
package org.sonatype.nexus.coreui;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Preconditions;
import com.google.inject.Key;
import com.softwarementors.extjs.djn.config.annotations.DirectAction;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.realm.Realm;
import org.eclipse.sisu.inject.BeanLocator;
import org.sonatype.nexus.coreui.RealmSettingsXO;
import org.sonatype.nexus.coreui.ReferenceXO;
import org.sonatype.nexus.extdirect.DirectComponentSupport;
import org.sonatype.nexus.security.realm.RealmManager;
import org.sonatype.nexus.validation.Validate;

@Named
@Singleton
@DirectAction(action={"coreui_RealmSettings"})
public class RealmSettingsComponent
extends DirectComponentSupport {
    private final RealmManager realmManager;
    private final BeanLocator beanLocator;

    @Inject
    public RealmSettingsComponent(RealmManager realmManager, BeanLocator beanLocator) {
        this.realmManager = (RealmManager)Preconditions.checkNotNull((Object)realmManager);
        this.beanLocator = (BeanLocator)Preconditions.checkNotNull((Object)beanLocator);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:settings:read"})
    public RealmSettingsXO read() {
        RealmSettingsXO settingsXO = new RealmSettingsXO();
        settingsXO.setRealms(this.realmManager.getConfiguredRealmIds());
        return settingsXO;
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:settings:read"})
    public List<ReferenceXO> readRealmTypes() {
        return StreamSupport.stream(this.beanLocator.locate(Key.get(Realm.class, Named.class)).spliterator(), false).map(entry -> new ReferenceXO(((Named)entry.getKey()).value(), entry.getDescription())).sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName())).collect(Collectors.toList());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:settings:update"})
    @Validate
    public RealmSettingsXO update(@NotNull @Valid RealmSettingsXO realmSettingsXO) {
        this.realmManager.setConfiguredRealmIds(realmSettingsXO.getRealms());
        return this.read();
    }
}

