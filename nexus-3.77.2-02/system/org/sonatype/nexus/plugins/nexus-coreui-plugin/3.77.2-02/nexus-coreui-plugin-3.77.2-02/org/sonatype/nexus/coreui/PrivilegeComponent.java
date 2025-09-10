/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.annotation.ExceptionMetered
 *  com.codahale.metrics.annotation.Timed
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.Maps
 *  com.softwarementors.extjs.djn.config.annotations.DirectAction
 *  com.softwarementors.extjs.djn.config.annotations.DirectMethod
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.validation.Valid
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 *  javax.validation.groups.Default
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.nexus.extdirect.DirectComponentSupport
 *  org.sonatype.nexus.extdirect.model.PagedResponse
 *  org.sonatype.nexus.extdirect.model.StoreLoadParameters
 *  org.sonatype.nexus.extdirect.model.StoreLoadParameters$Filter
 *  org.sonatype.nexus.extdirect.model.StoreLoadParameters$Sort
 *  org.sonatype.nexus.formfields.FormField
 *  org.sonatype.nexus.security.SecuritySystem
 *  org.sonatype.nexus.security.authz.AuthorizationManager
 *  org.sonatype.nexus.security.authz.NoSuchAuthorizationManagerException
 *  org.sonatype.nexus.security.privilege.Privilege
 *  org.sonatype.nexus.security.privilege.PrivilegeDescriptor
 *  org.sonatype.nexus.security.privilege.ReadonlyPrivilegeException
 *  org.sonatype.nexus.validation.Validate
 *  org.sonatype.nexus.validation.group.Create
 *  org.sonatype.nexus.validation.group.Update
 */
package org.sonatype.nexus.coreui;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.softwarementors.extjs.djn.config.annotations.DirectAction;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.nexus.coreui.FormFieldXO;
import org.sonatype.nexus.coreui.PrivilegeTypeXO;
import org.sonatype.nexus.coreui.PrivilegeXO;
import org.sonatype.nexus.coreui.ReferenceXO;
import org.sonatype.nexus.extdirect.DirectComponentSupport;
import org.sonatype.nexus.extdirect.model.PagedResponse;
import org.sonatype.nexus.extdirect.model.StoreLoadParameters;
import org.sonatype.nexus.formfields.FormField;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.authz.AuthorizationManager;
import org.sonatype.nexus.security.authz.NoSuchAuthorizationManagerException;
import org.sonatype.nexus.security.privilege.Privilege;
import org.sonatype.nexus.security.privilege.PrivilegeDescriptor;
import org.sonatype.nexus.security.privilege.ReadonlyPrivilegeException;
import org.sonatype.nexus.validation.Validate;
import org.sonatype.nexus.validation.group.Create;
import org.sonatype.nexus.validation.group.Update;

@Named
@Singleton
@DirectAction(action={"coreui_Privilege"})
public class PrivilegeComponent
extends DirectComponentSupport {
    private final SecuritySystem securitySystem;
    private final List<PrivilegeDescriptor> privilegeDescriptors;

    @Inject
    public PrivilegeComponent(SecuritySystem securitySystem, List<PrivilegeDescriptor> privilegeDescriptors) {
        this.securitySystem = (SecuritySystem)Preconditions.checkNotNull((Object)securitySystem);
        this.privilegeDescriptors = (List)Preconditions.checkNotNull(privilegeDescriptors);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:privileges:read"})
    public PagedResponse<PrivilegeXO> read(StoreLoadParameters parameters) {
        List<PrivilegeXO> privileges = this.securitySystem.listPrivileges().stream().map(this::convert).collect(Collectors.toList());
        return this.extractPage(parameters, privileges);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:privileges:read"})
    public List<ReferenceXO> readReferences() {
        return this.securitySystem.listPrivileges().stream().map(privilege -> new ReferenceXO(privilege.getId(), privilege.getName())).collect(Collectors.toList());
    }

    @RequiresPermissions(value={"nexus:privileges:read"})
    public PagedResponse<PrivilegeXO> extractPage(StoreLoadParameters parameters, List<PrivilegeXO> xos) {
        this.log.trace("requesting page with parameters: {} and size of: ${}", (Object)parameters, (Object)xos.size());
        Preconditions.checkArgument((parameters.getStart() == null || parameters.getStart() == 0 || parameters.getStart() < xos.size() ? 1 : 0) != 0, (Object)"Requested to skip more results than available");
        List<PrivilegeXO> result = new ArrayList<PrivilegeXO>(xos);
        if (parameters.getFilter() != null && !parameters.getFilter().isEmpty()) {
            String filter = ((StoreLoadParameters.Filter)parameters.getFilter().get(0)).getValue();
            result = xos.stream().filter(xo -> xo.getName().contains(filter) || xo.getDescription().contains(filter) || xo.getPermission().contains(filter) || xo.getType().contains(filter)).collect(Collectors.toList());
        }
        if (parameters.getSort() != null && !parameters.getSort().isEmpty()) {
            boolean ascending = "ASC".equals(((StoreLoadParameters.Sort)parameters.getSort().get(0)).getDirection());
            String sortProperty = ((StoreLoadParameters.Sort)parameters.getSort().get(0)).getProperty();
            result.sort((a, b) -> {
                int comparison = this.getFieldValue(a, sortProperty).compareTo(this.getFieldValue(b, sortProperty));
                return ascending ? comparison : -comparison;
            });
        }
        int size = result.size();
        int start = parameters.getStart() != null ? parameters.getStart() : 0;
        int limit = parameters.getLimit() != null ? parameters.getLimit() : size;
        int end = Math.min(start + limit, size);
        List<PrivilegeXO> page = result.subList(start, end);
        return new PagedResponse((long)size, page);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:privileges:read"})
    public List<PrivilegeTypeXO> readTypes() {
        return this.privilegeDescriptors.stream().map(descriptor -> {
            PrivilegeTypeXO xo = new PrivilegeTypeXO();
            xo.setId(descriptor.getType());
            xo.setName(descriptor.getName());
            xo.setFormFields(this.convertFormFields((PrivilegeDescriptor)descriptor));
            return xo;
        }).collect(Collectors.toList());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:privileges:create"})
    @Validate(groups={Create.class, Default.class})
    public PrivilegeXO create(@NotNull @Valid PrivilegeXO privilege) throws NoSuchAuthorizationManagerException {
        AuthorizationManager authorizationManager = this.securitySystem.getAuthorizationManager("default");
        privilege.withId(privilege.getName());
        return this.convert(authorizationManager.addPrivilege(this.convert(privilege)));
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:privileges:update"})
    @Validate(groups={Update.class, Default.class})
    public PrivilegeXO update(@NotNull @Valid PrivilegeXO privilege) throws NoSuchAuthorizationManagerException, IllegalAccessException {
        try {
            AuthorizationManager authorizationManager = this.securitySystem.getAuthorizationManager("default");
            return this.convert(authorizationManager.updatePrivilege(this.convert(privilege)));
        }
        catch (ReadonlyPrivilegeException e) {
            throw new IllegalAccessException("Privilege [" + privilege.getId() + "] is readonly and cannot be updated");
        }
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:privileges:delete"})
    @Validate
    public void remove(@NotEmpty String id) throws NoSuchAuthorizationManagerException, IllegalAccessException {
        AuthorizationManager authorizationManager = this.securitySystem.getAuthorizationManager("default");
        try {
            authorizationManager.deletePrivilege(id);
        }
        catch (ReadonlyPrivilegeException e) {
            throw new IllegalAccessException("Privilege [" + id + "] is readonly and cannot be deleted");
        }
    }

    PrivilegeXO convert(Privilege input) {
        return new PrivilegeXO().withId(input.getId()).withVersion(String.valueOf(input.getVersion())).withName(input.getName() != null ? input.getName() : input.getId()).withDescription(input.getDescription() != null ? input.getDescription() : input.getId()).withType(input.getType()).withReadOnly(input.isReadOnly()).withProperties(Maps.newHashMap((Map)input.getProperties())).withPermission(input.getPermission().toString());
    }

    Privilege convert(PrivilegeXO input) {
        Privilege privilege = new Privilege();
        privilege.setId(input.getId());
        privilege.setVersion(input.getVersion().isEmpty() ? 0 : Integer.parseInt(input.getVersion()));
        privilege.setName(input.getName());
        privilege.setDescription(input.getDescription());
        privilege.setType(input.getType());
        privilege.setProperties((Map)Maps.newHashMap(input.getProperties()));
        return privilege;
    }

    private Comparable getFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (Comparable)field.get(obj);
        }
        catch (IllegalAccessException | NoSuchFieldException e) {
            return null;
        }
    }

    private List<FormFieldXO> convertFormFields(PrivilegeDescriptor descriptor) {
        if (descriptor.getFormFields() == null) {
            return null;
        }
        return descriptor.getFormFields().stream().map(f -> FormFieldXO.create((FormField)f)).collect(Collectors.toList());
    }
}

