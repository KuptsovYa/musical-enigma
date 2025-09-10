/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.annotation.ExceptionMetered
 *  com.codahale.metrics.annotation.Timed
 *  com.fasterxml.jackson.databind.json.JsonMapper
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.Streams
 *  com.softwarementors.extjs.djn.config.annotations.DirectAction
 *  com.softwarementors.extjs.djn.config.annotations.DirectMethod
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.validation.constraints.NotEmpty
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.nexus.common.entity.DetachedEntityId
 *  org.sonatype.nexus.common.entity.EntityId
 *  org.sonatype.nexus.common.text.Strings2
 *  org.sonatype.nexus.extdirect.DirectComponentSupport
 *  org.sonatype.nexus.extdirect.model.PagedResponse
 *  org.sonatype.nexus.extdirect.model.StoreLoadParameters
 *  org.sonatype.nexus.extdirect.model.StoreLoadParameters$Sort
 *  org.sonatype.nexus.repository.Repository
 *  org.sonatype.nexus.repository.manager.RepositoryManager
 *  org.sonatype.nexus.repository.query.PageResult
 *  org.sonatype.nexus.repository.query.QueryOptions
 *  org.sonatype.nexus.repository.security.RepositorySelector
 *  org.sonatype.nexus.selector.SelectorFactory
 *  org.sonatype.nexus.validation.Validate
 */
package org.sonatype.nexus.coreui;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.Streams;
import com.softwarementors.extjs.djn.config.annotations.DirectAction;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.constraints.NotEmpty;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.nexus.common.entity.DetachedEntityId;
import org.sonatype.nexus.common.entity.EntityId;
import org.sonatype.nexus.common.text.Strings2;
import org.sonatype.nexus.coreui.AssetAttributeTransformer;
import org.sonatype.nexus.coreui.AssetXO;
import org.sonatype.nexus.coreui.ComponentHelper;
import org.sonatype.nexus.coreui.ComponentXO;
import org.sonatype.nexus.extdirect.DirectComponentSupport;
import org.sonatype.nexus.extdirect.model.PagedResponse;
import org.sonatype.nexus.extdirect.model.StoreLoadParameters;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.manager.RepositoryManager;
import org.sonatype.nexus.repository.query.PageResult;
import org.sonatype.nexus.repository.query.QueryOptions;
import org.sonatype.nexus.repository.security.RepositorySelector;
import org.sonatype.nexus.selector.SelectorFactory;
import org.sonatype.nexus.validation.Validate;

@Named
@Singleton
@DirectAction(action={"coreui_Component"})
public class ComponentComponent
extends DirectComponentSupport {
    private final RepositoryManager repositoryManager;
    private final SelectorFactory selectorFactory;
    private final JsonMapper jsonMapper;
    private final ComponentHelper componentHelper;
    private final Map<String, AssetAttributeTransformer> formatTransformations;

    @Inject
    public ComponentComponent(RepositoryManager repositoryManager, SelectorFactory selectorFactory, JsonMapper jsonMapper, ComponentHelper componentHelper, Map<String, AssetAttributeTransformer> formatTransformations) {
        this.repositoryManager = (RepositoryManager)Preconditions.checkNotNull((Object)repositoryManager);
        this.selectorFactory = (SelectorFactory)Preconditions.checkNotNull((Object)selectorFactory);
        this.jsonMapper = (JsonMapper)Preconditions.checkNotNull((Object)jsonMapper);
        this.componentHelper = (ComponentHelper)Preconditions.checkNotNull((Object)componentHelper);
        this.formatTransformations = (Map)Preconditions.checkNotNull(formatTransformations);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    public List<AssetXO> readComponentAssets(StoreLoadParameters parameters) {
        String repositoryName = parameters.getFilter("repositoryName");
        Repository repository = this.repositoryManager.get(repositoryName);
        if (!repository.getConfiguration().isOnline()) {
            return Collections.emptyList();
        }
        ComponentXO componentXO = this.readComponent(parameters.getFilter("componentModel"));
        return this.componentHelper.readComponentAssets(repository, componentXO);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:selectors:*"})
    public PagedResponse<AssetXO> previewAssets(StoreLoadParameters parameters) {
        String repositoryName = parameters.getFilter("repositoryName");
        String expression = parameters.getFilter("expression");
        String type = parameters.getFilter("type");
        if (Strings2.isBlank((String)expression) || Strings2.isBlank((String)type) || Strings2.isBlank((String)repositoryName)) {
            return null;
        }
        this.selectorFactory.validateSelector(type, expression);
        RepositorySelector repositorySelector = RepositorySelector.fromSelector((String)repositoryName);
        List<Repository> selectedRepositories = this.getPreviewRepositories(repositorySelector);
        if (selectedRepositories.isEmpty()) {
            return null;
        }
        PageResult<AssetXO> result = this.componentHelper.previewAssets(repositorySelector, selectedRepositories, expression, this.toQueryOptions(parameters));
        return new PagedResponse(result.getTotal(), (Collection)result.getResults());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @Validate
    public boolean canDeleteComponent(@NotEmpty String componentModelString) {
        ComponentXO componentXO = this.readComponent(componentModelString);
        Repository repository = this.repositoryManager.get(componentXO.getRepositoryName());
        return this.componentHelper.canDeleteComponent(repository, componentXO);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @Validate
    public Set<String> deleteComponent(@NotEmpty String componentModelString) {
        ComponentXO componentXO = this.readComponent(componentModelString);
        Repository repository = this.repositoryManager.get(componentXO.getRepositoryName());
        return this.componentHelper.deleteComponent(repository, componentXO);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @Validate
    public boolean canDeleteAsset(@NotEmpty String assetId, @NotEmpty String repositoryName) {
        Repository repository = this.repositoryManager.get(repositoryName);
        return this.componentHelper.canDeleteAsset(repository, (EntityId)new DetachedEntityId(assetId));
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @Validate
    public Set<String> deleteAsset(@NotEmpty String assetId, @NotEmpty String repositoryName) {
        Repository repository = this.repositoryManager.get(repositoryName);
        return new HashSet<String>(this.componentHelper.deleteAsset(repository, (EntityId)new DetachedEntityId(assetId)));
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @Validate
    @Nullable
    public ComponentXO readComponent(@NotEmpty String componentId, @NotEmpty String repositoryName) {
        Repository repository = this.repositoryManager.get(repositoryName);
        return this.componentHelper.readComponent(repository, (EntityId)new DetachedEntityId(componentId));
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @Validate
    @Nullable
    public AssetXO readAsset(@NotEmpty String assetId, @NotEmpty String repositoryName) {
        Repository repository = this.repositoryManager.get(repositoryName);
        AssetXO assetXO = this.componentHelper.readAsset(repository, (EntityId)new DetachedEntityId(assetId));
        Optional.ofNullable(this.formatTransformations.get(assetXO.getFormat())).ifPresent(transformation -> transformation.transform(assetXO));
        return assetXO;
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @Validate
    public boolean canDeleteFolder(@NotEmpty String path, @NotEmpty String repositoryName) {
        Repository repository = this.repositoryManager.get(repositoryName);
        return this.componentHelper.canDeleteFolder(repository, path);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @Validate
    public void deleteFolder(@NotEmpty String path, @NotEmpty String repositoryName) {
        Repository repository = this.repositoryManager.get(repositoryName);
        this.componentHelper.deleteFolder(repository, path);
    }

    private QueryOptions toQueryOptions(StoreLoadParameters storeLoadParameters) {
        StoreLoadParameters.Sort sort = storeLoadParameters.getSort() != null ? (StoreLoadParameters.Sort)storeLoadParameters.getSort().get(0) : null;
        return new QueryOptions(storeLoadParameters.getFilter("filter"), sort != null ? sort.getProperty() : null, sort != null ? sort.getDirection() : null, storeLoadParameters.getStart(), storeLoadParameters.getLimit());
    }

    private List<Repository> getPreviewRepositories(RepositorySelector repositorySelector) {
        if (!repositorySelector.isAllRepositories()) {
            return Collections.singletonList(this.repositoryManager.get(repositorySelector.getName()));
        }
        if (!repositorySelector.isAllFormats()) {
            return Streams.stream((Iterable)this.repositoryManager.browse()).filter(repository -> repository.getFormat().getValue().equals(repositorySelector.getFormat())).collect(Collectors.toList());
        }
        return Streams.stream((Iterable)this.repositoryManager.browse()).collect(Collectors.toList());
    }

    private ComponentXO readComponent(String componentString) {
        Preconditions.checkNotNull((Object)componentString);
        try {
            return (ComponentXO)this.jsonMapper.readValue(componentString, ComponentXO.class);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

