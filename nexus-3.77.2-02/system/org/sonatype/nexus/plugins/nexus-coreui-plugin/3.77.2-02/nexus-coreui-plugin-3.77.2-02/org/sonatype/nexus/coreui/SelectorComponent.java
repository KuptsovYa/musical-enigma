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
 *  javax.validation.ConstraintViolationException
 *  javax.validation.Valid
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 *  javax.validation.groups.Default
 *  org.apache.shiro.SecurityUtils
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.nexus.extdirect.DirectComponentSupport
 *  org.sonatype.nexus.security.SecuritySystem
 *  org.sonatype.nexus.security.privilege.Privilege
 *  org.sonatype.nexus.selector.SelectorConfiguration
 *  org.sonatype.nexus.selector.SelectorConfigurationStore
 *  org.sonatype.nexus.selector.SelectorFactory
 *  org.sonatype.nexus.selector.SelectorManager
 *  org.sonatype.nexus.validation.ConstraintViolationFactory
 *  org.sonatype.nexus.validation.Validate
 *  org.sonatype.nexus.validation.group.Create
 *  org.sonatype.nexus.validation.group.Update
 */
package org.sonatype.nexus.coreui;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Preconditions;
import com.softwarementors.extjs.djn.config.annotations.DirectAction;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.nexus.coreui.ReferenceXO;
import org.sonatype.nexus.coreui.SelectorXO;
import org.sonatype.nexus.extdirect.DirectComponentSupport;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.privilege.Privilege;
import org.sonatype.nexus.selector.SelectorConfiguration;
import org.sonatype.nexus.selector.SelectorConfigurationStore;
import org.sonatype.nexus.selector.SelectorFactory;
import org.sonatype.nexus.selector.SelectorManager;
import org.sonatype.nexus.validation.ConstraintViolationFactory;
import org.sonatype.nexus.validation.Validate;
import org.sonatype.nexus.validation.group.Create;
import org.sonatype.nexus.validation.group.Update;

@Named
@Singleton
@DirectAction(action={"coreui_Selector"})
public class SelectorComponent
extends DirectComponentSupport {
    private static final String EXPRESSION_KEY = "expression";
    private final SelectorManager selectorManager;
    private final ConstraintViolationFactory constraintViolationFactory;
    private final SelectorFactory selectorFactory;
    private final SecuritySystem securitySystem;
    private final SelectorConfigurationStore store;

    @Inject
    public SelectorComponent(SelectorManager selectorManager, ConstraintViolationFactory constraintViolationFactory, SelectorFactory selectorFactory, SecuritySystem securitySystem, SelectorConfigurationStore store) {
        this.selectorManager = (SelectorManager)Preconditions.checkNotNull((Object)selectorManager);
        this.constraintViolationFactory = (ConstraintViolationFactory)Preconditions.checkNotNull((Object)constraintViolationFactory);
        this.selectorFactory = (SelectorFactory)Preconditions.checkNotNull((Object)selectorFactory);
        this.securitySystem = (SecuritySystem)Preconditions.checkNotNull((Object)securitySystem);
        this.store = (SelectorConfigurationStore)Preconditions.checkNotNull((Object)store);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:selectors:read"})
    public List<SelectorXO> read() {
        Set privileges = this.securitySystem.listPrivileges();
        return this.store.browse().stream().map(config -> this.asSelector((SelectorConfiguration)config, privileges)).collect(Collectors.toList());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:selectors:create"})
    @Validate(groups={Create.class, Default.class})
    public SelectorXO create(@NotNull @Valid SelectorXO selectorXO) {
        this.selectorFactory.validateSelector(selectorXO.getType(), selectorXO.getExpression());
        SelectorConfiguration configuration = this.selectorManager.newSelectorConfiguration(selectorXO.getName(), selectorXO.getType(), selectorXO.getDescription(), Collections.singletonMap(EXPRESSION_KEY, selectorXO.getExpression()));
        this.selectorManager.create(configuration);
        return this.asSelector(configuration, this.securitySystem.listPrivileges());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:selectors:update"})
    @Validate(groups={Update.class, Default.class})
    public SelectorXO update(@NotNull @Valid SelectorXO selectorXO) {
        this.selectorFactory.validateSelector(selectorXO.getType(), selectorXO.getExpression());
        SelectorConfiguration config = this.selectorManager.readByName(selectorXO.getName());
        config.setDescription(selectorXO.getDescription());
        config.setAttributes(Collections.singletonMap(EXPRESSION_KEY, selectorXO.getExpression()));
        this.selectorManager.update(config);
        return selectorXO;
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:selectors:delete"})
    @Validate
    public void remove(@NotEmpty String name) {
        try {
            this.selectorManager.delete(this.selectorManager.readByName(name));
        }
        catch (IllegalStateException e) {
            throw new ConstraintViolationException(e.getMessage(), Collections.singleton(this.constraintViolationFactory.createViolation("*", e.getMessage())));
        }
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:selectors:read"})
    public List<ReferenceXO> readReferences() {
        return this.selectorManager.browse().stream().map(config -> new ReferenceXO(config.getName(), config.getName())).collect(Collectors.toList());
    }

    private SelectorXO asSelector(SelectorConfiguration configuration, Set<Privilege> privilegeSet) {
        List<String> privileges = this.getPrivilegesUsingSelector(configuration, privilegeSet);
        SelectorXO selectorXO = new SelectorXO();
        selectorXO.setId(configuration.getName());
        selectorXO.setName(configuration.getName());
        selectorXO.setType(configuration.getType());
        selectorXO.setDescription(configuration.getDescription());
        selectorXO.setExpression((String)configuration.getAttributes().get(EXPRESSION_KEY));
        selectorXO.setUsedBy(SelectorComponent.canReadPrivileges() ? privileges : Collections.emptyList());
        selectorXO.setUsedByCount(privileges.size());
        return selectorXO;
    }

    private List<String> getPrivilegesUsingSelector(SelectorConfiguration selectorConfiguration, Set<Privilege> privileges) {
        return privileges.stream().filter(privilege -> "repository-content-selector".equals(privilege.getType())).filter(privilege -> selectorConfiguration.getName().equals(privilege.getProperties().get("contentSelector"))).map(Privilege::getName).collect(Collectors.toList());
    }

    private static boolean canReadPrivileges() {
        return SecurityUtils.getSubject().isPermitted("nexus:privileges:read");
    }
}

