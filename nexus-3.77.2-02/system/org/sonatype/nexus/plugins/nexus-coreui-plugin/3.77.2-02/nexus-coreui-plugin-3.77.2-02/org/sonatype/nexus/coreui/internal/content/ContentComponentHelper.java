/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableSet
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.ws.rs.WebApplicationException
 *  javax.ws.rs.core.Response$Status
 *  org.apache.shiro.authz.AuthorizationException
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.entity.EntityId
 *  org.sonatype.nexus.common.text.Strings2
 *  org.sonatype.nexus.repository.Repository
 *  org.sonatype.nexus.repository.content.Asset
 *  org.sonatype.nexus.repository.content.Component
 *  org.sonatype.nexus.repository.content.facet.ContentFacet
 *  org.sonatype.nexus.repository.content.facet.ContentFacetFinder
 *  org.sonatype.nexus.repository.content.fluent.FluentAsset
 *  org.sonatype.nexus.repository.content.fluent.FluentComponent
 *  org.sonatype.nexus.repository.content.fluent.FluentQuery
 *  org.sonatype.nexus.repository.content.maintenance.MaintenanceService
 *  org.sonatype.nexus.repository.content.search.ComponentFinder
 *  org.sonatype.nexus.repository.content.security.AssetPermissionChecker
 *  org.sonatype.nexus.repository.content.store.InternalIds
 *  org.sonatype.nexus.repository.group.GroupFacet
 *  org.sonatype.nexus.repository.manager.RepositoryManager
 *  org.sonatype.nexus.repository.query.PageResult
 *  org.sonatype.nexus.repository.query.QueryOptions
 *  org.sonatype.nexus.repository.security.RepositorySelector
 *  org.sonatype.nexus.repository.types.GroupType
 *  org.sonatype.nexus.repository.types.HostedType
 *  org.sonatype.nexus.selector.Selector
 *  org.sonatype.nexus.selector.SelectorFactory
 *  org.sonatype.nexus.selector.SelectorSqlBuilder
 */
package org.sonatype.nexus.coreui.internal.content;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.apache.shiro.authz.AuthorizationException;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.entity.EntityId;
import org.sonatype.nexus.common.text.Strings2;
import org.sonatype.nexus.coreui.AssetXO;
import org.sonatype.nexus.coreui.ComponentHelper;
import org.sonatype.nexus.coreui.ComponentXO;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.content.Asset;
import org.sonatype.nexus.repository.content.Component;
import org.sonatype.nexus.repository.content.facet.ContentFacet;
import org.sonatype.nexus.repository.content.facet.ContentFacetFinder;
import org.sonatype.nexus.repository.content.fluent.FluentAsset;
import org.sonatype.nexus.repository.content.fluent.FluentComponent;
import org.sonatype.nexus.repository.content.fluent.FluentQuery;
import org.sonatype.nexus.repository.content.maintenance.MaintenanceService;
import org.sonatype.nexus.repository.content.search.ComponentFinder;
import org.sonatype.nexus.repository.content.security.AssetPermissionChecker;
import org.sonatype.nexus.repository.content.store.InternalIds;
import org.sonatype.nexus.repository.group.GroupFacet;
import org.sonatype.nexus.repository.manager.RepositoryManager;
import org.sonatype.nexus.repository.query.PageResult;
import org.sonatype.nexus.repository.query.QueryOptions;
import org.sonatype.nexus.repository.security.RepositorySelector;
import org.sonatype.nexus.repository.types.GroupType;
import org.sonatype.nexus.repository.types.HostedType;
import org.sonatype.nexus.selector.Selector;
import org.sonatype.nexus.selector.SelectorFactory;
import org.sonatype.nexus.selector.SelectorSqlBuilder;

@Named
@Singleton
public class ContentComponentHelper
extends ComponentSupport
implements ComponentHelper {
    private final MaintenanceService maintenanceService;
    private final Map<String, ComponentFinder> componentFinders;
    private final ComponentFinder defaultComponentFinder;
    private final AssetPermissionChecker assetPermissionChecker;
    private final RepositoryManager repositoryManager;
    private final SelectorFactory selectorFactory;

    @Inject
    public ContentComponentHelper(MaintenanceService maintenanceService, Map<String, ComponentFinder> componentFinders, AssetPermissionChecker assetPermissionChecker, SelectorFactory selectorFactory, RepositoryManager repositoryManager) {
        this.maintenanceService = (MaintenanceService)Preconditions.checkNotNull((Object)maintenanceService);
        this.componentFinders = (Map)Preconditions.checkNotNull(componentFinders);
        this.assetPermissionChecker = (AssetPermissionChecker)Preconditions.checkNotNull((Object)assetPermissionChecker);
        this.defaultComponentFinder = (ComponentFinder)Preconditions.checkNotNull((Object)componentFinders.get("default"));
        this.selectorFactory = (SelectorFactory)Preconditions.checkNotNull((Object)selectorFactory);
        this.repositoryManager = (RepositoryManager)Preconditions.checkNotNull((Object)repositoryManager);
    }

    @Override
    public List<AssetXO> readComponentAssets(Repository repository, ComponentXO model) {
        Optional<FluentComponent> component = this.findComponentsByModel(repository, model).findFirst();
        if (!component.isPresent()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        Collection assets = component.get().assets();
        String repositoryName = repository.getName();
        String format = repository.getFormat().getValue();
        return this.assetPermissionChecker.findPermittedAssets(assets, format, "browse").map(entry -> this.toAssetXO(repositoryName, (String)entry.getValue(), format, (Asset)entry.getKey())).collect(Collectors.toList());
    }

    @Override
    public PageResult<AssetXO> previewAssets(RepositorySelector repositorySelector, List<Repository> selectedRepositories, String jexlExpression, QueryOptions queryOptions) {
        LinkedHashSet previewRepositories = new LinkedHashSet();
        selectedRepositories.forEach(r -> {
            if (r.getType() instanceof GroupType) {
                previewRepositories.addAll(((GroupFacet)r.facet(GroupFacet.class)).leafMembers());
            } else {
                previewRepositories.add(r);
            }
        });
        int numAssets = 0;
        ArrayList assets = new ArrayList();
        for (Repository r2 : previewRepositories) {
            String format = r2.getFormat().getValue();
            SelectorSqlBuilder sqlBuilder = new SelectorSqlBuilder().propertyAlias("path", "path").propertyAlias("format", "'" + format + "'").parameterPrefix("#{filterParams.").parameterSuffix("}");
            Selector selector = this.selectorFactory.createSelector("csel", jexlExpression);
            selector.toSql(sqlBuilder);
            Object filterString = sqlBuilder.getQueryString();
            Map filterParams = sqlBuilder.getQueryParameters();
            if (queryOptions.getFilter() != null) {
                filterString = (String)filterString + " AND path LIKE #{filterParams.pathFilter}";
                filterParams.put("pathFilter", "%" + queryOptions.getFilter() + "%");
            }
            FluentQuery assetQuery = ((ContentFacet)r2.facet(ContentFacet.class)).assets().byFilter((String)filterString, filterParams);
            numAssets += assetQuery.count();
            int nextLimit = queryOptions.getLimit() - assets.size();
            if (nextLimit <= 0) continue;
            assetQuery.browse(nextLimit, null).stream().map(asset -> this.toAssetXO(r2.getName(), r2.getName(), format, (Asset)asset)).collect(Collectors.toCollection(() -> assets));
        }
        return new PageResult((long)numAssets, assets);
    }

    @Override
    public ComponentXO readComponent(Repository repository, EntityId componentId) {
        Optional<FluentComponent> component = this.findComponentById(repository, componentId);
        if (!component.isPresent()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        Collection assets = component.get().assets();
        if (assets.isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        String repositoryName = repository.getName();
        String format = repository.getFormat().getValue();
        if (this.assetPermissionChecker.findPermittedAssets(assets, format, "browse").findFirst().isPresent()) {
            return ContentComponentHelper.toComponentXO(repositoryName, format, (Component)component.get());
        }
        throw new AuthorizationException();
    }

    @Override
    public boolean canDeleteComponent(Repository repository, ComponentXO model) {
        return this.findComponentsByModel(repository, model).allMatch(component -> this.maintenanceService.canDeleteComponent(repository, (Component)component));
    }

    @Override
    public Set<String> deleteComponent(Repository repository, ComponentXO model) {
        return this.findComponentsByModel(repository, model).flatMap(component -> this.maintenanceService.deleteComponent(repository, (Component)component).stream()).collect(Collectors.toSet());
    }

    @Override
    public AssetXO readAsset(Repository repository, EntityId assetId) {
        Optional<FluentAsset> asset = this.findAssetById(repository, assetId);
        if (!asset.isPresent()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        String repositoryName = repository.getName();
        String format = repository.getFormat().getValue();
        return this.assetPermissionChecker.isPermitted((Asset)asset.get(), format, "browse").map(containingRepositoryName -> this.toAssetXO(repositoryName, (String)containingRepositoryName, format, (Asset)asset.get())).orElseThrow(AuthorizationException::new);
    }

    @Override
    public boolean canDeleteAsset(Repository repository, EntityId assetId) {
        return this.findAssetById(repository, assetId).filter(asset -> this.maintenanceService.canDeleteAsset(repository, (Asset)asset)).isPresent();
    }

    @Override
    public Set<String> deleteAsset(Repository repository, EntityId assetId) {
        return this.findAssetById(repository, assetId).map(asset -> this.maintenanceService.deleteAsset(repository, (Asset)asset)).orElse((Set)ImmutableSet.of());
    }

    @Override
    public boolean canDeleteFolder(Repository repository, String path) {
        return this.maintenanceService.canDeleteFolder(repository, path);
    }

    @Override
    public void deleteFolder(Repository repository, String path) {
        this.maintenanceService.deleteFolder(repository, path);
    }

    private Stream<FluentComponent> findComponentsByModel(Repository repository, ComponentXO model) {
        String format = repository.getFormat().getValue();
        ComponentFinder finder = this.componentFinders.getOrDefault(format, this.defaultComponentFinder);
        return finder.findComponentsByModel(repository, model.getId(), model.getGroup(), model.getName(), model.getVersion());
    }

    private Optional<FluentComponent> findComponentById(Repository repository, EntityId componentId) {
        return ContentFacetFinder.findContentFacets((Repository)repository).map(ContentFacet::components).map(facet -> facet.find(componentId)).filter(Optional::isPresent).map(Optional::get).findFirst();
    }

    private Optional<FluentAsset> findAssetById(Repository repository, EntityId assetId) {
        return ContentFacetFinder.findContentFacets((Repository)repository).map(ContentFacet::assets).map(facet -> facet.find(assetId)).filter(Optional::isPresent).map(Optional::get).findFirst();
    }

    private static ComponentXO toComponentXO(String repositoryName, String format, Component component) {
        ComponentXO componentXO = new ComponentXO();
        componentXO.setRepositoryName(repositoryName);
        componentXO.setFormat(format);
        componentXO.setId(ContentComponentHelper.componentId(component));
        componentXO.setGroup(component.namespace());
        componentXO.setName(component.name());
        componentXO.setVersion(component.version());
        return componentXO;
    }

    protected AssetXO toAssetXO(String repositoryName, String containingRepositoryName, String format, Asset asset) {
        AssetXO assetXO = new AssetXO();
        assetXO.setRepositoryName(repositoryName);
        assetXO.setContainingRepositoryName(containingRepositoryName);
        assetXO.setFormat(format);
        assetXO.setId(ContentComponentHelper.assetId(asset));
        assetXO.setName(asset.path());
        asset.component().ifPresent(component -> assetXO.setComponentId(ContentComponentHelper.componentId(component)));
        HashMap<String, Object> attributes = new HashMap<String, Object>(asset.attributes().backing());
        Object formatAttributes = attributes.get(format);
        if (!Strings2.isEmpty((String)asset.kind())) {
            if (formatAttributes instanceof Map) {
                ((Map)formatAttributes).put("asset_kind", asset.kind());
            } else {
                attributes.put(format, Collections.singletonMap("asset_kind", asset.kind()));
            }
        }
        OffsetDateTime createdTime = asset.created();
        assetXO.setBlobCreated(Date.from(createdTime.toInstant()));
        asset.blob().ifPresent(blob -> {
            Date blobCreated = Date.from(blob.blobCreated().toInstant());
            if (blob.blobCreated().isBefore(createdTime)) {
                assetXO.setBlobCreated(blobCreated);
            }
            assetXO.setBlobRef(blob.blobRef().toString());
            assetXO.setSize(blob.blobSize());
            assetXO.setContentType(blob.contentType());
            assetXO.setBlobUpdated(blobCreated);
            attributes.put("checksum", blob.checksums());
            assetXO.setCreatedBy(blob.createdBy().orElse(null));
            assetXO.setCreatedByIp(blob.createdByIp().orElse(null));
        });
        if (this.repositoryManager.get(repositoryName).getType() instanceof HostedType && attributes.containsKey("content")) {
            Map contentMap = (Map)attributes.get("content");
            contentMap.remove("last_modified");
        }
        assetXO.setAttributes(attributes);
        asset.lastDownloaded().ifPresent(when -> assetXO.setLastDownloaded(Date.from(when.toInstant())));
        return assetXO;
    }

    private static String componentId(Component component) {
        return InternalIds.toExternalId((int)InternalIds.internalComponentId((Component)component)).getValue();
    }

    private static String assetId(Asset asset) {
        return InternalIds.toExternalId((int)InternalIds.internalAssetId((Asset)asset)).getValue();
    }
}

