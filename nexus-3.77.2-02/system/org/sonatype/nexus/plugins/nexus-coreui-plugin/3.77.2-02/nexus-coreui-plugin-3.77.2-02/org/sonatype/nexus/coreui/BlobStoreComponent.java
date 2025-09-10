/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.annotation.ExceptionMetered
 *  com.codahale.metrics.annotation.Timed
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.Streams
 *  com.softwarementors.extjs.djn.config.annotations.DirectAction
 *  com.softwarementors.extjs.djn.config.annotations.DirectMethod
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.validation.Valid
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 *  javax.validation.groups.Default
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.nexus.blobstore.BlobStoreDescriptor
 *  org.sonatype.nexus.blobstore.BlobStoreDescriptorProvider
 *  org.sonatype.nexus.blobstore.api.BlobStore
 *  org.sonatype.nexus.blobstore.api.BlobStoreConfiguration
 *  org.sonatype.nexus.blobstore.api.BlobStoreException
 *  org.sonatype.nexus.blobstore.api.BlobStoreManager
 *  org.sonatype.nexus.blobstore.api.BlobStoreMetrics
 *  org.sonatype.nexus.blobstore.api.tasks.BlobStoreTaskService
 *  org.sonatype.nexus.blobstore.group.BlobStoreGroup
 *  org.sonatype.nexus.blobstore.quota.BlobStoreQuota
 *  org.sonatype.nexus.common.app.ApplicationDirectories
 *  org.sonatype.nexus.common.collect.NestedAttributesMap
 *  org.sonatype.nexus.extdirect.DirectComponentSupport
 *  org.sonatype.nexus.extdirect.model.StoreLoadParameters
 *  org.sonatype.nexus.rapture.PasswordPlaceholder
 *  org.sonatype.nexus.repository.blobstore.BlobStoreConfigurationStore
 *  org.sonatype.nexus.repository.manager.RepositoryManager
 *  org.sonatype.nexus.repository.security.RepositoryPermissionChecker
 *  org.sonatype.nexus.security.privilege.ApplicationPermission
 *  org.sonatype.nexus.validation.Validate
 *  org.sonatype.nexus.validation.group.Create
 *  org.sonatype.nexus.validation.group.Update
 */
package org.sonatype.nexus.coreui;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Streams;
import com.softwarementors.extjs.djn.config.annotations.DirectAction;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.nexus.blobstore.BlobStoreDescriptor;
import org.sonatype.nexus.blobstore.BlobStoreDescriptorProvider;
import org.sonatype.nexus.blobstore.api.BlobStore;
import org.sonatype.nexus.blobstore.api.BlobStoreConfiguration;
import org.sonatype.nexus.blobstore.api.BlobStoreException;
import org.sonatype.nexus.blobstore.api.BlobStoreManager;
import org.sonatype.nexus.blobstore.api.BlobStoreMetrics;
import org.sonatype.nexus.blobstore.api.tasks.BlobStoreTaskService;
import org.sonatype.nexus.blobstore.group.BlobStoreGroup;
import org.sonatype.nexus.blobstore.quota.BlobStoreQuota;
import org.sonatype.nexus.common.app.ApplicationDirectories;
import org.sonatype.nexus.common.collect.NestedAttributesMap;
import org.sonatype.nexus.coreui.BlobStoreQuotaTypeXO;
import org.sonatype.nexus.coreui.BlobStoreTypeXO;
import org.sonatype.nexus.coreui.BlobStoreXO;
import org.sonatype.nexus.coreui.FormFieldXO;
import org.sonatype.nexus.coreui.PathSeparatorXO;
import org.sonatype.nexus.extdirect.DirectComponentSupport;
import org.sonatype.nexus.extdirect.model.StoreLoadParameters;
import org.sonatype.nexus.rapture.PasswordPlaceholder;
import org.sonatype.nexus.repository.blobstore.BlobStoreConfigurationStore;
import org.sonatype.nexus.repository.manager.RepositoryManager;
import org.sonatype.nexus.repository.security.RepositoryPermissionChecker;
import org.sonatype.nexus.security.privilege.ApplicationPermission;
import org.sonatype.nexus.validation.Validate;
import org.sonatype.nexus.validation.group.Create;
import org.sonatype.nexus.validation.group.Update;

@Named
@Singleton
@DirectAction(action={"coreui_Blobstore"})
public class BlobStoreComponent
extends DirectComponentSupport {
    private static final long MILLION = 1000000L;
    private static final String BLOB_STORES_DOMAIN = "blobstores";
    public static final String AZURE_ACCOUNT_KEY = "accountKey";
    private final BlobStoreManager blobStoreManager;
    private final BlobStoreConfigurationStore store;
    private final BlobStoreDescriptorProvider blobStoreDescriptorProvider;
    private final Map<String, BlobStoreQuota> quotaFactories;
    private final ApplicationDirectories applicationDirectories;
    private final RepositoryManager repositoryManager;
    private final RepositoryPermissionChecker repositoryPermissionChecker;
    private final BlobStoreTaskService blobStoreTaskService;

    @Inject
    public BlobStoreComponent(BlobStoreManager blobStoreManager, BlobStoreConfigurationStore store, BlobStoreDescriptorProvider blobStoreDescriptorProvider, Map<String, BlobStoreQuota> quotaFactories, ApplicationDirectories applicationDirectories, RepositoryManager repositoryManager, RepositoryPermissionChecker repositoryPermissionChecker, @Nullable BlobStoreTaskService blobStoreTaskService) {
        this.blobStoreManager = (BlobStoreManager)Preconditions.checkNotNull((Object)blobStoreManager);
        this.store = (BlobStoreConfigurationStore)Preconditions.checkNotNull((Object)store);
        this.blobStoreDescriptorProvider = (BlobStoreDescriptorProvider)Preconditions.checkNotNull((Object)blobStoreDescriptorProvider);
        this.quotaFactories = (Map)Preconditions.checkNotNull(quotaFactories);
        this.applicationDirectories = (ApplicationDirectories)Preconditions.checkNotNull((Object)applicationDirectories);
        this.repositoryManager = (RepositoryManager)Preconditions.checkNotNull((Object)repositoryManager);
        this.repositoryPermissionChecker = (RepositoryPermissionChecker)Preconditions.checkNotNull((Object)repositoryPermissionChecker);
        this.blobStoreTaskService = blobStoreTaskService;
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    public List<BlobStoreXO> read() {
        this.repositoryPermissionChecker.ensureUserHasAnyPermissionOrAdminAccess(Collections.singletonList(new ApplicationPermission(BLOB_STORES_DOMAIN, new String[]{"read"})), "read", this.repositoryManager.browse());
        List<BlobStoreGroup> blobStoreGroups = this.getBlobStoreGroups();
        return this.store.list().stream().map(config -> this.asBlobStoreXO((BlobStoreConfiguration)config, (Collection<BlobStoreGroup>)blobStoreGroups)).collect(Collectors.toList());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    public List<BlobStoreXO> readNoneGroupEntriesIncludingEntryForAll() {
        this.repositoryPermissionChecker.ensureUserHasAnyPermissionOrAdminAccess(Collections.singletonList(new ApplicationPermission(BLOB_STORES_DOMAIN, new String[]{"read"})), "read", this.repositoryManager.browse());
        List<BlobStoreXO> blobStores = this.store.list().stream().filter(config -> !"Group".equals(config.getType())).map(this::asBlobStoreXO).collect(Collectors.toList());
        BlobStoreXO allXO = new BlobStoreXO().withName("(All Blob Stores)");
        blobStores.add(allXO);
        return blobStores;
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    public List<BlobStoreXO> readNames() {
        this.repositoryPermissionChecker.ensureUserHasAnyPermissionOrAdminAccess(Collections.singletonList(new ApplicationPermission(BLOB_STORES_DOMAIN, new String[]{"read"})), "read", this.repositoryManager.browse());
        return this.store.list().stream().map(config -> new BlobStoreXO().withName(config.getName())).collect(Collectors.toList());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:blobstores:read"})
    public List<BlobStoreXO> readGroupable(@Nullable StoreLoadParameters parameters) {
        List<BlobStoreGroup> blobStoreGroups = this.getBlobStoreGroups();
        String selectedBlobStoreName = Optional.ofNullable(parameters).map(params -> params.getFilter("blobStoreName")).orElse(null);
        List otherGroups = blobStoreGroups.stream().filter(group -> selectedBlobStoreName == null || !group.getBlobStoreConfiguration().getName().equals(selectedBlobStoreName)).collect(Collectors.toList());
        return this.store.list().stream().filter(config -> !"Group".equals(config.getType()) && !this.repositoryManager.browseForBlobStore(config.getName()).iterator().hasNext() && this.isNotInOtherGroups((BlobStoreConfiguration)config, otherGroups)).map(this::asBlobStoreXO).collect(Collectors.toList());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:blobstores:read"})
    public List<BlobStoreXO> readGroups() {
        return this.store.list().stream().filter(config -> "Group".equals(config.getType())).map(this::asBlobStoreXO).collect(Collectors.toList());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:blobstores:read"})
    public List<BlobStoreTypeXO> readTypes() {
        List<BlobStoreTypeXO> readTypes = this.blobStoreDescriptorProvider.get().entrySet().stream().map(entry -> {
            BlobStoreDescriptor descriptor = (BlobStoreDescriptor)entry.getValue();
            BlobStoreTypeXO xo = new BlobStoreTypeXO();
            xo.setId((String)entry.getKey());
            xo.setName(descriptor.getName());
            xo.setFormFields(descriptor.getFormFields().stream().map(FormFieldXO::create).collect(Collectors.toCollection(ArrayList::new)));
            xo.setCustomFormName(descriptor.customFormName());
            xo.setIsModifiable(descriptor.isModifiable());
            xo.setConnectionTestable(descriptor.isConnectionTestable());
            xo.setIsEnabled(descriptor.isEnabled());
            return xo;
        }).collect(Collectors.toList());
        BlobStoreTypeXO emptyType = new BlobStoreTypeXO();
        emptyType.setId("");
        emptyType.setName("");
        emptyType.setCustomFormName("");
        emptyType.setIsModifiable(false);
        emptyType.setIsEnabled(true);
        readTypes.add(emptyType);
        return readTypes;
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:blobstores:read"})
    public List<BlobStoreQuotaTypeXO> readQuotaTypes() {
        return this.quotaFactories.entrySet().stream().map(entry -> {
            BlobStoreQuotaTypeXO xo = new BlobStoreQuotaTypeXO();
            xo.setId((String)entry.getKey());
            xo.setName(((BlobStoreQuota)entry.getValue()).getDisplayName());
            return xo;
        }).collect(Collectors.toList());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:blobstores:create"})
    @Validate(groups={Create.class, Default.class})
    public BlobStoreXO create(@NotNull @Valid BlobStoreXO blobStore) throws Exception {
        return this.asBlobStoreXO(this.blobStoreManager.create(this.asConfiguration(blobStore)).getBlobStoreConfiguration());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:blobstores:update"})
    @Validate(groups={Update.class, Default.class})
    public BlobStoreXO update(@NotNull @Valid BlobStoreXO blobStoreXO) throws Exception {
        BlobStore blobStore = this.blobStoreManager.get(blobStoreXO.getName());
        if (PasswordPlaceholder.is((String)this.getS3SecretAccessKey(blobStoreXO))) {
            blobStoreXO.getAttributes().get("s3").put("secretAccessKey", ((Map)blobStore.getBlobStoreConfiguration().getAttributes().get("s3")).get("secretAccessKey"));
        }
        if (PasswordPlaceholder.is((String)this.getAzureAccountKey(blobStoreXO))) {
            blobStoreXO.getAttributes().get("azure cloud storage").put(AZURE_ACCOUNT_KEY, ((Map)blobStore.getBlobStoreConfiguration().getAttributes().get("azure cloud storage")).get(AZURE_ACCOUNT_KEY));
        }
        return this.asBlobStoreXO(this.blobStoreManager.update(this.asConfiguration(blobStoreXO)).getBlobStoreConfiguration());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:blobstores:delete"})
    @Validate
    public void remove(@NotEmpty String name) throws Exception {
        if (this.repositoryManager.isBlobstoreUsed(name)) {
            throw new BlobStoreException("Blob store (" + name + ") is in use by at least one repository", null);
        }
        if (this.blobStoreTaskService.countTasksInUseForBlobStore(name) > 0) {
            throw new BlobStoreException("Blob store (" + name + ") is in use by a Change Repository Blob Store task", null);
        }
        this.blobStoreManager.delete(name);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:blobstores:read"})
    public PathSeparatorXO defaultWorkDirectory() {
        PathSeparatorXO xo = new PathSeparatorXO();
        xo.setPath(this.applicationDirectories.getWorkDirectory("blobs").getPath());
        xo.setFileSeparator(File.separator);
        return xo;
    }

    @VisibleForTesting
    BlobStoreConfiguration asConfiguration(BlobStoreXO blobStoreXO) {
        if (blobStoreXO.isQuotaEnabled()) {
            HashMap<String, Object> quotaAttributes = new HashMap<String, Object>();
            quotaAttributes.put("quotaType", blobStoreXO.getQuotaType());
            quotaAttributes.put("quotaLimitBytes", blobStoreXO.getQuotaLimit() * 1000000L);
            blobStoreXO.getAttributes().put("blobStoreQuotaConfig", quotaAttributes);
        }
        BlobStoreConfiguration config = this.blobStoreManager.newConfiguration();
        config.setName(blobStoreXO.getName());
        config.setType(blobStoreXO.getType());
        config.setAttributes(blobStoreXO.getAttributes());
        return config;
    }

    @VisibleForTesting
    BlobStoreXO asBlobStoreXO(BlobStoreConfiguration blobStoreConfiguration) {
        return this.asBlobStoreXO(blobStoreConfiguration, Collections.emptyList());
    }

    @VisibleForTesting
    BlobStoreXO asBlobStoreXO(BlobStoreConfiguration blobStoreConfiguration, Collection<BlobStoreGroup> blobStoreGroups) {
        NestedAttributesMap quotaAttributes = blobStoreConfiguration.attributes("blobStoreQuotaConfig");
        BlobStoreXO blobStoreXO = new BlobStoreXO().withName(blobStoreConfiguration.getName()).withType(blobStoreConfiguration.getType()).withAttributes(BlobStoreComponent.filterAttributes(blobStoreConfiguration.getAttributes())).withRepositoryUseCount(this.repositoryManager.blobstoreUsageCount(blobStoreConfiguration.getName())).withTaskUseCount(this.blobStoreTaskService.countTasksInUseForBlobStore(blobStoreConfiguration.getName())).withBlobStoreUseCount(this.blobStoreManager.blobStoreUsageCount(blobStoreConfiguration.getName())).withInUse(this.repositoryManager.isBlobstoreUsed(blobStoreConfiguration.getName())).withConvertable(this.blobStoreManager.isConvertable(blobStoreConfiguration.getName())).withIsQuotaEnabled(!quotaAttributes.isEmpty()).withQuotaType((String)quotaAttributes.get("quotaType")).withQuotaLimit(this.getQuotaLimit(quotaAttributes)).withGroupName(blobStoreGroups.stream().filter(group -> this.getMembersConfig(group.getMembers()).contains(blobStoreConfiguration)).map(group -> group.getBlobStoreConfiguration().getName()).findFirst().orElse(null));
        BlobStore blobStore = (BlobStore)this.blobStoreManager.getByName().get(blobStoreConfiguration.getName());
        if (blobStore != null && blobStore.isStarted()) {
            BlobStoreMetrics metrics = blobStore.getMetrics();
            blobStoreXO.withBlobCount(metrics.getBlobCount()).withTotalSize(metrics.getTotalSize()).withAvailableSpace(metrics.getAvailableSpace()).withUnlimited(metrics.isUnlimited()).withUnavailable(metrics.isUnavailable());
        } else {
            blobStoreXO.withUnavailable(true);
        }
        return blobStoreXO;
    }

    private static Map<String, Map<String, Object>> filterAttributes(Map<String, Map<String, Object>> attributes) {
        if (attributes.get("s3") != null && attributes.get("s3").get("secretAccessKey") != null) {
            attributes.get("s3").put("secretAccessKey", PasswordPlaceholder.get());
        } else if (attributes.get("azure cloud storage") != null && attributes.get("azure cloud storage").get(AZURE_ACCOUNT_KEY) != null) {
            attributes.get("azure cloud storage").put(AZURE_ACCOUNT_KEY, PasswordPlaceholder.get());
        }
        return attributes;
    }

    private List<BlobStoreGroup> getBlobStoreGroups() {
        return Streams.stream((Iterable)this.blobStoreManager.browse()).filter(blobStore -> "Group".equals(blobStore.getBlobStoreConfiguration().getType())).map(BlobStoreGroup.class::cast).collect(Collectors.toList());
    }

    private String getS3SecretAccessKey(BlobStoreXO blobStoreXO) {
        return Optional.ofNullable(blobStoreXO).map(BlobStoreXO::getAttributes).map(attributes -> (Map)attributes.get("s3")).map(s3 -> s3.get("secretAccessKey").toString()).orElse(null);
    }

    private String getAzureAccountKey(BlobStoreXO blobStoreXO) {
        return Optional.ofNullable(blobStoreXO).map(BlobStoreXO::getAttributes).map(attributes -> (Map)attributes.get("azure cloud storage")).map(s3 -> s3.get(AZURE_ACCOUNT_KEY).toString()).orElse(null);
    }

    private Long getQuotaLimit(NestedAttributesMap quotaAttributes) {
        Long quotaLimit = null;
        Number limit = (Number)quotaAttributes.get("quotaLimitBytes", Number.class);
        if (limit != null) {
            quotaLimit = limit.longValue() / 1000000L;
        }
        return quotaLimit;
    }

    private boolean isNotInOtherGroups(BlobStoreConfiguration config, List<BlobStoreGroup> otherGroups) {
        return otherGroups.stream().noneMatch(group -> this.getMembersConfig(group.getMembers()).contains(config));
    }

    private List<BlobStoreConfiguration> getMembersConfig(List<BlobStore> members) {
        return members.stream().map(BlobStore::getBlobStoreConfiguration).collect(Collectors.toList());
    }
}

