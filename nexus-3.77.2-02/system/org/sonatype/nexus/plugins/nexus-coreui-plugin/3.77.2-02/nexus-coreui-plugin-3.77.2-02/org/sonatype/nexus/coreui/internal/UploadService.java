/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.Iterables
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.servlet.http.HttpServletRequest
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.repository.Repository
 *  org.sonatype.nexus.repository.cache.RepositoryCacheInvalidationService
 *  org.sonatype.nexus.repository.manager.RepositoryManager
 *  org.sonatype.nexus.repository.upload.UploadDefinition
 *  org.sonatype.nexus.repository.upload.UploadManager
 *  org.sonatype.nexus.repository.upload.UploadResponse
 */
package org.sonatype.nexus.coreui.internal;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import java.io.IOException;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.cache.RepositoryCacheInvalidationService;
import org.sonatype.nexus.repository.manager.RepositoryManager;
import org.sonatype.nexus.repository.upload.UploadDefinition;
import org.sonatype.nexus.repository.upload.UploadManager;
import org.sonatype.nexus.repository.upload.UploadResponse;

@Named
@Singleton
public class UploadService
extends ComponentSupport {
    private UploadManager uploadManager;
    private RepositoryManager repositoryManager;
    private RepositoryCacheInvalidationService repositoryCacheInvalidationService;
    private static final String NPM_FORMAT = "npm";

    @Inject
    public UploadService(RepositoryManager repositoryManager, UploadManager uploadManager, RepositoryCacheInvalidationService repositoryCacheInvalidationService) {
        this.uploadManager = (UploadManager)Preconditions.checkNotNull((Object)uploadManager);
        this.repositoryManager = (RepositoryManager)Preconditions.checkNotNull((Object)repositoryManager);
        this.repositoryCacheInvalidationService = (RepositoryCacheInvalidationService)Preconditions.checkNotNull((Object)repositoryCacheInvalidationService);
    }

    public Collection<UploadDefinition> getAvailableDefinitions() {
        return this.uploadManager.getAvailableDefinitions();
    }

    public String upload(String repositoryName, HttpServletRequest request) throws IOException {
        Preconditions.checkNotNull((Object)repositoryName);
        Preconditions.checkNotNull((Object)request);
        Repository repository = (Repository)Preconditions.checkNotNull((Object)this.repositoryManager.get(repositoryName), (Object)"Specified repository is missing");
        UploadResponse uploadResponse = this.uploadManager.handle(repository, request);
        if (NPM_FORMAT.equals(repository.getFormat().getValue())) {
            this.repositoryManager.findContainingGroups(repositoryName).forEach(groupRepoName -> this.repositoryCacheInvalidationService.processCachesInvalidation(this.repositoryManager.get(groupRepoName)));
        }
        return this.createSearchTerm(uploadResponse.getAssetPaths());
    }

    @VisibleForTesting
    String createSearchTerm(Collection<String> createdPaths) {
        if (createdPaths.isEmpty()) {
            return null;
        }
        String prefix = (String)Iterables.getFirst(createdPaths, null);
        for (String path : createdPaths) {
            prefix = this.longestPrefix(prefix, path);
        }
        return prefix;
    }

    private String removeLastSegment(String path) {
        int index = path.lastIndexOf(47);
        if (index != -1) {
            return path.substring(0, index);
        }
        return path;
    }

    private String longestPrefix(String prefix, String path) {
        String result = prefix;
        while (result.length() > 0 && !path.startsWith(result)) {
            result = this.removeLastSegment(result);
        }
        return result;
    }
}

