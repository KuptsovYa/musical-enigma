/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.annotation.ExceptionMetered
 *  com.codahale.metrics.annotation.Timed
 *  com.google.common.base.Preconditions
 *  com.google.common.base.Predicate
 *  com.google.common.collect.ImmutableMap
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
 *  org.sonatype.nexus.capability.Capability
 *  org.sonatype.nexus.capability.CapabilityDescriptor
 *  org.sonatype.nexus.capability.CapabilityDescriptorRegistry
 *  org.sonatype.nexus.capability.CapabilityIdentity
 *  org.sonatype.nexus.capability.CapabilityReference
 *  org.sonatype.nexus.capability.CapabilityReferenceFilterBuilder
 *  org.sonatype.nexus.capability.CapabilityReferenceFilterBuilder$CapabilityReferenceFilter
 *  org.sonatype.nexus.capability.CapabilityRegistry
 *  org.sonatype.nexus.capability.CapabilityType
 *  org.sonatype.nexus.capability.Tag
 *  org.sonatype.nexus.capability.Taggable
 *  org.sonatype.nexus.extdirect.DirectComponentSupport
 *  org.sonatype.nexus.rapture.PasswordPlaceholder
 *  org.sonatype.nexus.rapture.StateContributor
 *  org.sonatype.nexus.validation.Validate
 *  org.sonatype.nexus.validation.group.Create
 *  org.sonatype.nexus.validation.group.Update
 */
package org.sonatype.nexus.coreui.internal.capability;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.softwarementors.extjs.djn.config.annotations.DirectAction;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashSet;
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
import org.sonatype.nexus.capability.Capability;
import org.sonatype.nexus.capability.CapabilityDescriptor;
import org.sonatype.nexus.capability.CapabilityDescriptorRegistry;
import org.sonatype.nexus.capability.CapabilityIdentity;
import org.sonatype.nexus.capability.CapabilityReference;
import org.sonatype.nexus.capability.CapabilityReferenceFilterBuilder;
import org.sonatype.nexus.capability.CapabilityRegistry;
import org.sonatype.nexus.capability.CapabilityType;
import org.sonatype.nexus.capability.Tag;
import org.sonatype.nexus.capability.Taggable;
import org.sonatype.nexus.coreui.FormFieldXO;
import org.sonatype.nexus.coreui.internal.capability.CapabilityNotesXO;
import org.sonatype.nexus.coreui.internal.capability.CapabilityTypeXO;
import org.sonatype.nexus.coreui.internal.capability.CapabilityXO;
import org.sonatype.nexus.extdirect.DirectComponentSupport;
import org.sonatype.nexus.rapture.PasswordPlaceholder;
import org.sonatype.nexus.rapture.StateContributor;
import org.sonatype.nexus.validation.Validate;
import org.sonatype.nexus.validation.group.Create;
import org.sonatype.nexus.validation.group.Update;

@Named
@Singleton
@DirectAction(action={"capability_Capability"})
public class CapabilityComponent
extends DirectComponentSupport
implements StateContributor {
    private static final CapabilityReferenceFilterBuilder.CapabilityReferenceFilter ALL_CREATED = CapabilityReferenceFilterBuilder.capabilities().includeNotExposed();
    private static final CapabilityReferenceFilterBuilder.CapabilityReferenceFilter ALL_ACTIVE = CapabilityReferenceFilterBuilder.capabilities().includeNotExposed().active();
    private final CapabilityDescriptorRegistry capabilityDescriptorRegistry;
    private final CapabilityRegistry capabilityRegistry;

    @Inject
    public CapabilityComponent(CapabilityDescriptorRegistry capabilityDescriptorRegistry, CapabilityRegistry capabilityRegistry) {
        this.capabilityDescriptorRegistry = (CapabilityDescriptorRegistry)Preconditions.checkNotNull((Object)capabilityDescriptorRegistry);
        this.capabilityRegistry = (CapabilityRegistry)Preconditions.checkNotNull((Object)capabilityRegistry);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:capabilities:read"})
    public List<CapabilityXO> read() {
        this.capabilityRegistry.pullAndRefreshReferencesFromDB();
        return this.capabilityRegistry.get((Predicate)CapabilityReferenceFilterBuilder.capabilities()).stream().map(this::asCapability).collect(Collectors.toList());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:capabilities:read"})
    public List<CapabilityTypeXO> readTypes() {
        return Arrays.stream(this.capabilityDescriptorRegistry.getAll()).filter(CapabilityDescriptor::isExposed).map(this::asCapabilityType).collect(Collectors.toList());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:capabilities:create"})
    @Validate(groups={Create.class, Default.class})
    public CapabilityXO create(@NotNull @Valid CapabilityXO capabilityXO) {
        return this.asCapability(this.capabilityRegistry.add(CapabilityType.capabilityType((String)capabilityXO.getTypeId()), capabilityXO.getEnabled().booleanValue(), capabilityXO.getNotes(), capabilityXO.getProperties()));
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:capabilities:update"})
    @Validate(groups={Update.class, Default.class})
    public CapabilityXO update(@NotNull @Valid CapabilityXO capabilityXO) {
        CapabilityReference capabilityReference = this.capabilityRegistry.get(CapabilityIdentity.capabilityIdentity((String)capabilityXO.getId()));
        return this.asCapability(this.capabilityRegistry.update(CapabilityIdentity.capabilityIdentity((String)capabilityXO.getId()), capabilityXO.getEnabled().booleanValue(), capabilityXO.getNotes(), this.unfilterProperties(capabilityXO.getProperties(), capabilityReference.context().properties())));
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:capabilities:update"})
    @Validate(groups={Update.class, Default.class})
    public CapabilityXO updateNotes(@NotNull @Valid CapabilityNotesXO capabilityNotesXO) {
        CapabilityReference capabilityReference = this.capabilityRegistry.get(CapabilityIdentity.capabilityIdentity((String)capabilityNotesXO.getId()));
        return this.asCapability(this.capabilityRegistry.update(capabilityReference.context().id(), capabilityReference.context().isEnabled(), capabilityNotesXO.getNotes(), capabilityReference.context().properties()));
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:capabilities:delete"})
    @Validate
    public void remove(@NotEmpty String id) {
        this.capabilityRegistry.remove(CapabilityIdentity.capabilityIdentity((String)id));
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:capabilities:update"})
    @Validate
    public void enable(@NotEmpty String id) {
        this.capabilityRegistry.enable(CapabilityIdentity.capabilityIdentity((String)id));
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:capabilities:update"})
    @Validate
    public void disable(@NotEmpty String id) {
        this.capabilityRegistry.disable(CapabilityIdentity.capabilityIdentity((String)id));
    }

    public Map<String, Object> getState() {
        return ImmutableMap.of((Object)"capabilityCreatedTypes", this.capabilityRegistry.get((Predicate)ALL_CREATED).stream().map(this::capabilityToType).collect(Collectors.toSet()), (Object)"capabilityActiveTypes", this.capabilityRegistry.get((Predicate)ALL_ACTIVE).stream().map(this::capabilityToType).collect(Collectors.toSet()));
    }

    private String capabilityToType(CapabilityReference capabilityReference) {
        return capabilityReference.context().descriptor().type().toString();
    }

    private CapabilityXO asCapability(CapabilityReference reference) {
        CapabilityDescriptor descriptor = reference.context().descriptor();
        Capability capability = reference.capability();
        CapabilityXO capabilityXO = new CapabilityXO();
        capabilityXO.setId(reference.context().id().toString());
        capabilityXO.setNotes(reference.context().notes());
        capabilityXO.setTypeId(descriptor.type().toString());
        capabilityXO.setTypeName(descriptor.name());
        capabilityXO.setEnabled(reference.context().isEnabled());
        capabilityXO.setActive(reference.context().isActive());
        capabilityXO.setError(reference.context().hasFailure());
        capabilityXO.setState("disabled");
        capabilityXO.setStateDescription(reference.context().stateDescription());
        capabilityXO.setProperties(this.filterProperties(reference.context().properties(), capability));
        capabilityXO.setDisableWarningMessage(descriptor.getDisableWarningMessage());
        capabilityXO.setDeleteWarningMessage(descriptor.getDeleteWarningMessage());
        if (capabilityXO.getEnabled().booleanValue() && capabilityXO.getError().booleanValue()) {
            capabilityXO.setState("error");
        } else if (capabilityXO.getEnabled().booleanValue() && capabilityXO.getActive().booleanValue()) {
            capabilityXO.setState("active");
        } else if (capabilityXO.getEnabled().booleanValue() && !capabilityXO.getActive().booleanValue()) {
            capabilityXO.setState("passive");
        }
        if (capability.description() != null) {
            capabilityXO.setDescription(capability.description());
        }
        if (capability.status() != null) {
            capabilityXO.setStatus(capability.status());
        }
        HashSet tags = new HashSet();
        if (descriptor instanceof Taggable && ((Taggable)descriptor).getTags() != null) {
            tags.addAll(((Taggable)descriptor).getTags());
        }
        if (capability instanceof Taggable && ((Taggable)capability).getTags() != null) {
            tags.addAll(((Taggable)capability).getTags());
        }
        if (!tags.isEmpty()) {
            capabilityXO.setTags(tags.stream().collect(Collectors.toMap(Tag::key, Tag::value)));
        }
        return capabilityXO;
    }

    private CapabilityTypeXO asCapabilityType(CapabilityDescriptor capabilityDescriptor) {
        CapabilityTypeXO capabilityTypeXO = new CapabilityTypeXO();
        capabilityTypeXO.setId(capabilityDescriptor.type().toString());
        capabilityTypeXO.setName(capabilityDescriptor.name());
        capabilityTypeXO.setAbout(capabilityDescriptor.about());
        if (capabilityDescriptor.formFields() != null) {
            capabilityTypeXO.setFormFields(capabilityDescriptor.formFields().stream().map(FormFieldXO::create).collect(Collectors.toList()));
        }
        return capabilityTypeXO;
    }

    private Map<String, String> filterProperties(Map<String, String> properties, Capability capability) {
        return properties.entrySet().stream().filter(this::nonNullKeyAndValue).map(entry -> {
            if (capability.isPasswordProperty((String)entry.getKey())) {
                if ("PKI".equals(properties.get("authenticationType"))) {
                    return new AbstractMap.SimpleEntry<String, String>((String)entry.getKey(), "");
                }
                return new AbstractMap.SimpleEntry<String, String>((String)entry.getKey(), PasswordPlaceholder.get());
            }
            return entry;
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<String, String> unfilterProperties(Map<String, String> properties, Map<String, String> referenceProperties) {
        return properties.entrySet().stream().filter(this::nonNullKeyAndValue).map(entry -> {
            if (PasswordPlaceholder.is((String)((String)entry.getValue()))) {
                return new AbstractMap.SimpleEntry<String, String>((String)entry.getKey(), (String)referenceProperties.get(entry.getKey()));
            }
            return entry;
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private boolean nonNullKeyAndValue(Map.Entry<?, ?> entry) {
        return entry != null && entry.getKey() != null && entry.getValue() != null;
    }
}

