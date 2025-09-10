/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.base.Predicates
 *  com.google.common.base.Strings
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.ws.rs.GET
 *  javax.ws.rs.Path
 *  javax.ws.rs.PathParam
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.blobstore.BlobStoreDescriptor
 *  org.sonatype.nexus.blobstore.BlobStoreDescriptorProvider
 *  org.sonatype.nexus.blobstore.api.BlobStore
 *  org.sonatype.nexus.blobstore.api.BlobStoreConfiguration
 *  org.sonatype.nexus.blobstore.api.BlobStoreManager
 *  org.sonatype.nexus.blobstore.api.BlobStoreMetrics
 *  org.sonatype.nexus.blobstore.group.BlobStoreGroup
 *  org.sonatype.nexus.blobstore.quota.BlobStoreQuota
 *  org.sonatype.nexus.blobstore.s3.S3BlobStoreConfigurationHelper
 *  org.sonatype.nexus.repository.blobstore.BlobStoreConfigurationStore
 *  org.sonatype.nexus.repository.manager.RepositoryManager
 *  org.sonatype.nexus.rest.Resource
 */
package org.sonatype.nexus.coreui.internal.blobstore;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.blobstore.BlobStoreDescriptor;
import org.sonatype.nexus.blobstore.BlobStoreDescriptorProvider;
import org.sonatype.nexus.blobstore.api.BlobStore;
import org.sonatype.nexus.blobstore.api.BlobStoreConfiguration;
import org.sonatype.nexus.blobstore.api.BlobStoreManager;
import org.sonatype.nexus.blobstore.api.BlobStoreMetrics;
import org.sonatype.nexus.blobstore.group.BlobStoreGroup;
import org.sonatype.nexus.blobstore.quota.BlobStoreQuota;
import org.sonatype.nexus.blobstore.s3.S3BlobStoreConfigurationHelper;
import org.sonatype.nexus.coreui.internal.blobstore.BlobStoreQuotaTypesUIResponse;
import org.sonatype.nexus.coreui.internal.blobstore.BlobStoreTypesUIResponse;
import org.sonatype.nexus.coreui.internal.blobstore.BlobStoreUIResponse;
import org.sonatype.nexus.coreui.internal.blobstore.BlobStoreUsageUIResponse;
import org.sonatype.nexus.repository.blobstore.BlobStoreConfigurationStore;
import org.sonatype.nexus.repository.manager.RepositoryManager;
import org.sonatype.nexus.rest.Resource;

@Named
@Singleton
@Path(value="/internal/ui/blobstores")
public class BlobStoreInternalResource
extends ComponentSupport
implements Resource {
    static final String RESOURCE_PATH = "/internal/ui/blobstores";
    public static final String GOOGLE_CONFIG = "google cloud storage";
    public static final String GOOGLE_TYPE = "google";
    public static final String GOOGLE_BUCKET_KEY = "bucketName";
    public static final String PREFIX_KEY = "prefix";
    public static final String AZURE_CONFIG = "azure cloud storage";
    public static final String AZURE_TYPE = "azure";
    public static final String CONTAINER_NAME = "containerName";
    private final BlobStoreManager blobStoreManager;
    private final BlobStoreConfigurationStore store;
    private final BlobStoreDescriptorProvider blobStoreDescriptorProvider;
    private final List<BlobStoreQuotaTypesUIResponse> blobStoreQuotaTypes;
    private final RepositoryManager repositoryManager;
    private static final Logger logger = LoggerFactory.getLogger(BlobStoreInternalResource.class);

    @Inject
    public BlobStoreInternalResource(BlobStoreManager blobStoreManager, BlobStoreConfigurationStore store, BlobStoreDescriptorProvider blobStoreDescriptorProvider, Map<String, BlobStoreQuota> quotaFactories, RepositoryManager repositoryManager) {
        this.blobStoreManager = (BlobStoreManager)Preconditions.checkNotNull((Object)blobStoreManager);
        this.store = (BlobStoreConfigurationStore)Preconditions.checkNotNull((Object)store);
        this.blobStoreDescriptorProvider = (BlobStoreDescriptorProvider)Preconditions.checkNotNull((Object)blobStoreDescriptorProvider);
        this.blobStoreQuotaTypes = quotaFactories.entrySet().stream().map(BlobStoreQuotaTypesUIResponse::new).collect(Collectors.toList());
        this.repositoryManager = (RepositoryManager)Preconditions.checkNotNull((Object)repositoryManager);
    }

    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:blobstores:read"})
    @GET
    public List<BlobStoreUIResponse> listBlobStores() {
        return this.store.list().stream().map(configuration -> {
            String blobStoreType = configuration.getType();
            BlobStoreDescriptor blobStoreDescriptor = Optional.ofNullable(this.blobStoreDescriptorProvider.get()).map(it -> (BlobStoreDescriptor)it.get(blobStoreType)).orElse(null);
            if (blobStoreDescriptor == null) {
                return null;
            }
            String typeId = blobStoreDescriptor.getId();
            String path = BlobStoreInternalResource.getPath(typeId.toLowerCase(), configuration);
            BlobStoreMetrics metrics = Optional.ofNullable(this.blobStoreManager.get(configuration.getName())).map(BlobStoreInternalResource::getBlobStoreMetrics).orElse(null);
            return new BlobStoreUIResponse(typeId, (BlobStoreConfiguration)configuration, metrics, path);
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static BlobStoreMetrics getBlobStoreMetrics(BlobStore bs) {
        if (bs.isGroupable()) {
            return bs.isStarted() ? bs.getMetrics() : null;
        }
        return ((BlobStoreGroup)bs).getMembers().stream().map(BlobStore::isStarted).reduce(Boolean::logicalAnd).orElse(false) != false ? bs.getMetrics() : null;
    }

    private static String getPath(String typeId, BlobStoreConfiguration configuration) {
        if (typeId.equals("File".toLowerCase())) {
            return (String)configuration.attributes("file").get("path", String.class);
        }
        if (typeId.equals("s3")) {
            return S3BlobStoreConfigurationHelper.getBucketPrefix((BlobStoreConfiguration)configuration) + (String)configuration.attributes("s3").get("bucket", String.class);
        }
        if (typeId.equals(AZURE_TYPE)) {
            return (String)configuration.attributes(AZURE_CONFIG).get(CONTAINER_NAME, String.class);
        }
        if (typeId.equals("Group".toLowerCase())) {
            return "N/A";
        }
        if (typeId.equals(GOOGLE_TYPE)) {
            String prefix = Optional.ofNullable((String)configuration.attributes(GOOGLE_CONFIG).get(PREFIX_KEY, String.class)).filter((Predicate<String>)Predicates.not(Strings::isNullOrEmpty)).map(s -> s.replaceFirst("/$", "") + "/").orElse("");
            return prefix + (String)configuration.attributes(GOOGLE_CONFIG).get(GOOGLE_BUCKET_KEY, String.class);
        }
        logger.warn("blob store type {} unknown, defaulting to N/A for path", (Object)typeId);
        return "N/A";
    }

    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:blobstores:read"})
    @GET
    @Path(value="/types")
    public List<BlobStoreTypesUIResponse> listBlobStoreTypes() {
        return this.blobStoreDescriptorProvider.get().entrySet().stream().filter(entry -> ((BlobStoreDescriptor)entry.getValue()).isEnabled()).map(BlobStoreTypesUIResponse::new).collect(Collectors.toList());
    }

    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:blobstores:read"})
    @GET
    @Path(value="/usage/{name}")
    public BlobStoreUsageUIResponse getBlobStoreUsage(@PathParam(value="name") String name) {
        long repositoryUsage = this.repositoryManager.blobstoreUsageCount(name);
        long blobStoreUsage = this.blobStoreManager.blobStoreUsageCount(name);
        return new BlobStoreUsageUIResponse(repositoryUsage, blobStoreUsage);
    }

    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:blobstores:read"})
    @GET
    @Path(value="/quotaTypes")
    public List<BlobStoreQuotaTypesUIResponse> listQuotaTypes() {
        return this.blobStoreQuotaTypes;
    }
}

