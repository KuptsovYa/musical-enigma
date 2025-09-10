/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.Sets
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.validation.Valid
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 *  javax.validation.groups.Default
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.shiro.authz.Permission
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.app.BaseUrlHolder
 *  org.sonatype.nexus.common.app.GlobalComponentLookupHelper
 *  org.sonatype.nexus.common.entity.DetachedEntityId
 *  org.sonatype.nexus.common.entity.EntityId
 *  org.sonatype.nexus.extdirect.model.StoreLoadParameters
 *  org.sonatype.nexus.rapture.PasswordPlaceholder
 *  org.sonatype.nexus.rapture.StateContributor
 *  org.sonatype.nexus.repository.Facet
 *  org.sonatype.nexus.repository.Format
 *  org.sonatype.nexus.repository.MissingFacetException
 *  org.sonatype.nexus.repository.Recipe
 *  org.sonatype.nexus.repository.Repository
 *  org.sonatype.nexus.repository.cache.RepositoryCacheInvalidationService
 *  org.sonatype.nexus.repository.config.Configuration
 *  org.sonatype.nexus.repository.config.ConfigurationStore
 *  org.sonatype.nexus.repository.httpclient.HttpClientFacet
 *  org.sonatype.nexus.repository.httpclient.RemoteConnectionStatus
 *  org.sonatype.nexus.repository.manager.RepositoryManager
 *  org.sonatype.nexus.repository.rest.api.RepositoryMetricsService
 *  org.sonatype.nexus.repository.security.RepositoryAdminPermission
 *  org.sonatype.nexus.repository.security.RepositoryPermissionChecker
 *  org.sonatype.nexus.repository.security.RepositorySelector
 *  org.sonatype.nexus.repository.types.ProxyType
 *  org.sonatype.nexus.scheduling.TaskConfiguration
 *  org.sonatype.nexus.scheduling.TaskInfo
 *  org.sonatype.nexus.scheduling.TaskScheduler
 *  org.sonatype.nexus.security.SecurityHelper
 *  org.sonatype.nexus.validation.Validate
 *  org.sonatype.nexus.validation.group.Create
 *  org.sonatype.nexus.validation.group.Update
 */
package org.sonatype.nexus.coreui.service;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.app.BaseUrlHolder;
import org.sonatype.nexus.common.app.GlobalComponentLookupHelper;
import org.sonatype.nexus.common.entity.DetachedEntityId;
import org.sonatype.nexus.common.entity.EntityId;
import org.sonatype.nexus.coreui.ReferenceXO;
import org.sonatype.nexus.coreui.RepositoryReferenceXO;
import org.sonatype.nexus.coreui.RepositoryStatusXO;
import org.sonatype.nexus.coreui.RepositoryXO;
import org.sonatype.nexus.coreui.internal.RepositoryCleanupAttributesUtil;
import org.sonatype.nexus.coreui.search.BrowseableFormatXO;
import org.sonatype.nexus.extdirect.model.StoreLoadParameters;
import org.sonatype.nexus.rapture.PasswordPlaceholder;
import org.sonatype.nexus.rapture.StateContributor;
import org.sonatype.nexus.repository.Facet;
import org.sonatype.nexus.repository.Format;
import org.sonatype.nexus.repository.MissingFacetException;
import org.sonatype.nexus.repository.Recipe;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.cache.RepositoryCacheInvalidationService;
import org.sonatype.nexus.repository.config.Configuration;
import org.sonatype.nexus.repository.config.ConfigurationStore;
import org.sonatype.nexus.repository.httpclient.HttpClientFacet;
import org.sonatype.nexus.repository.httpclient.RemoteConnectionStatus;
import org.sonatype.nexus.repository.manager.RepositoryManager;
import org.sonatype.nexus.repository.rest.api.RepositoryMetricsService;
import org.sonatype.nexus.repository.security.RepositoryAdminPermission;
import org.sonatype.nexus.repository.security.RepositoryPermissionChecker;
import org.sonatype.nexus.repository.security.RepositorySelector;
import org.sonatype.nexus.repository.types.ProxyType;
import org.sonatype.nexus.scheduling.TaskConfiguration;
import org.sonatype.nexus.scheduling.TaskInfo;
import org.sonatype.nexus.scheduling.TaskScheduler;
import org.sonatype.nexus.security.SecurityHelper;
import org.sonatype.nexus.validation.Validate;
import org.sonatype.nexus.validation.group.Create;
import org.sonatype.nexus.validation.group.Update;

@Named
@Singleton
public class RepositoryUiService
extends ComponentSupport
implements StateContributor {
    private final RepositoryCacheInvalidationService repositoryCacheInvalidationService;
    private final RepositoryManager repositoryManager;
    private final Optional<RepositoryMetricsService> repositoryMetricsService;
    private final ConfigurationStore configurationStore;
    private final SecurityHelper securityHelper;
    private final Map<String, Recipe> recipes;
    private final TaskScheduler taskScheduler;
    private final GlobalComponentLookupHelper typeLookup;
    private final List<Format> formats;
    private final RepositoryPermissionChecker repositoryPermissionChecker;

    @Inject
    public RepositoryUiService(RepositoryCacheInvalidationService repositoryCacheInvalidationService, RepositoryManager repositoryManager, @Nullable RepositoryMetricsService repositoryMetricsService, ConfigurationStore configurationStore, SecurityHelper securityHelper, Map<String, Recipe> recipes, TaskScheduler taskScheduler, GlobalComponentLookupHelper typeLookup, List<Format> formats, RepositoryPermissionChecker repositoryPermissionChecker) {
        this.repositoryCacheInvalidationService = (RepositoryCacheInvalidationService)Preconditions.checkNotNull((Object)repositoryCacheInvalidationService);
        this.repositoryManager = (RepositoryManager)Preconditions.checkNotNull((Object)repositoryManager);
        this.repositoryMetricsService = Optional.ofNullable(repositoryMetricsService);
        this.configurationStore = (ConfigurationStore)Preconditions.checkNotNull((Object)configurationStore);
        this.securityHelper = (SecurityHelper)Preconditions.checkNotNull((Object)securityHelper);
        this.recipes = new HashMap<String, Recipe>((Map)Preconditions.checkNotNull(recipes));
        this.taskScheduler = (TaskScheduler)Preconditions.checkNotNull((Object)taskScheduler);
        this.typeLookup = (GlobalComponentLookupHelper)Preconditions.checkNotNull((Object)typeLookup);
        this.formats = (List)Preconditions.checkNotNull(formats);
        this.repositoryPermissionChecker = (RepositoryPermissionChecker)Preconditions.checkNotNull((Object)repositoryPermissionChecker);
    }

    public List<RepositoryXO> read() {
        return StreamSupport.stream(this.browse().spliterator(), false).map(this::asRepository).collect(Collectors.toList());
    }

    public List<ReferenceXO> readRecipes() {
        return this.recipes.entrySet().stream().filter((? super T entry) -> ((Recipe)entry.getValue()).isFeatureEnabled()).map(RepositoryUiService::toReference).collect(Collectors.toList());
    }

    private static ReferenceXO toReference(Map.Entry<String, Recipe> recipe) {
        ReferenceXO xo = new ReferenceXO();
        xo.setId(recipe.getKey());
        xo.setName(String.format("%s (%s)", recipe.getValue().getFormat(), recipe.getValue().getType()));
        return xo;
    }

    public List<Format> readFormats() {
        return this.formats;
    }

    public List<BrowseableFormatXO> getBrowseableFormats() {
        List browseableRepositories = this.repositoryPermissionChecker.userCanBrowseRepositories(this.repositoryManager.browse());
        return browseableRepositories.stream().map(Repository::getFormat).map(Format::getValue).distinct().map(RepositoryUiService::toBrowseableFormat).collect(Collectors.toList());
    }

    private static BrowseableFormatXO toBrowseableFormat(String format) {
        BrowseableFormatXO xo = new BrowseableFormatXO();
        xo.setId(format);
        return xo;
    }

    public Map<String, Object> getState() {
        return Collections.singletonMap("browseableformats", this.getBrowseableFormats());
    }

    public List<RepositoryReferenceXO> readReferences(@Nullable StoreLoadParameters parameters) {
        List<RepositoryReferenceXO> references = StreamSupport.stream(this.filter(parameters).spliterator(), false).map(repository -> new RepositoryReferenceXO(repository.getRepositoryName(), repository.getRepositoryName(), this.getType((Configuration)repository), this.getFormat((Configuration)repository), RepositoryUiService.getVersionPolicy(repository), RepositoryUiService.getUrl(repository.getRepositoryName()), this.getBlobStoreName((Configuration)repository), this.buildStatus((Configuration)repository))).collect(Collectors.toList());
        references = RepositoryUiService.filterForAutocomplete(parameters, references);
        return references;
    }

    private String getBlobStoreName(Configuration repository) {
        return ((Map)repository.getAttributes().get("storage")).get("blobStoreName").toString();
    }

    private static String getVersionPolicy(Configuration configuration) {
        return Optional.of(configuration).map(Configuration::getAttributes).map(attr -> (Map)attr.get("maven")).map(maven -> maven.get("versionPolicy")).map(String.class::cast).orElse(null);
    }

    @VisibleForTesting
    static List<RepositoryReferenceXO> filterForAutocomplete(@Nullable StoreLoadParameters parameters, List<RepositoryReferenceXO> references) {
        if (StringUtils.isNotBlank((CharSequence)parameters.getQuery())) {
            return references.stream().filter((? super T repo) -> repo.getName().startsWith(parameters.getQuery())).collect(Collectors.toList());
        }
        return references;
    }

    public List<RepositoryReferenceXO> readReferencesAddingEntryForAll(@Nullable StoreLoadParameters parameters) {
        List<RepositoryReferenceXO> references = this.readReferences(parameters);
        RepositoryReferenceXO all = new RepositoryReferenceXO(RepositorySelector.all().toSelector(), "(All Repositories)", null, null, null, null, null, null, 1);
        references.add(all);
        return references;
    }

    public List<RepositoryReferenceXO> readReferencesAddingEntriesForAllFormats(@Nullable StoreLoadParameters parameters) {
        List<RepositoryReferenceXO> references = this.readReferencesAddingEntryForAll(parameters);
        this.formats.stream().forEach(format -> references.add(new RepositoryReferenceXO(RepositorySelector.allOfFormat((String)format.getValue()).toSelector(), "(All " + format.getValue() + " Repositories)", null, null, null, null, null, null)));
        return references;
    }

    @RequiresAuthentication
    @Validate(groups={Create.class, Default.class})
    public RepositoryXO create(@NotNull @Valid RepositoryXO repositoryXO) throws Exception {
        this.securityHelper.ensurePermitted(new Permission[]{new RepositoryAdminPermission(repositoryXO.getFormat(), repositoryXO.getName(), Collections.singletonList("add"))});
        RepositoryCleanupAttributesUtil.initializeCleanupAttributes(repositoryXO);
        Configuration config = this.repositoryManager.newConfiguration();
        config.setRepositoryName(repositoryXO.getName());
        config.setRecipeName(repositoryXO.getRecipe());
        config.setOnline(repositoryXO.getOnline().booleanValue());
        Optional.ofNullable(repositoryXO).map(RepositoryXO::getRoutingRuleId).filter(StringUtils::isNotBlank).map(DetachedEntityId::new).ifPresent(arg_0 -> ((Configuration)config).setRoutingRuleId(arg_0));
        config.setAttributes(repositoryXO.getAttributes());
        return this.asRepository(this.repositoryManager.create(config));
    }

    @RequiresAuthentication
    @Validate(groups={Update.class, Default.class})
    public RepositoryXO update(@NotNull @Valid RepositoryXO repositoryXO) throws Exception {
        Repository repository = this.repositoryManager.get(repositoryXO.getName());
        this.securityHelper.ensurePermitted(new Permission[]{this.adminPermission(repository, "edit")});
        Optional.of(repositoryXO).map(RepositoryXO::getAttributes).map(attr -> (Map)attr.get("httpclient")).map(httpclient -> httpclient.get("authentication")).map(Map.class::cast).ifPresent(authentication -> {
            String password = (String)authentication.get("password");
            if (PasswordPlaceholder.is((String)password)) {
                Optional.of(repository).map(Repository::getConfiguration).map(Configuration::getAttributes).map(attr -> (Map)attr.get("httpclient")).map(Map.class::cast).map(httpclient -> httpclient.get("authentication")).map(Map.class::cast).map(storedAuthentication -> storedAuthentication.get("password")).ifPresent(storedPassword -> authentication.put("password", storedPassword));
            }
        });
        RepositoryCleanupAttributesUtil.initializeCleanupAttributes(repositoryXO);
        Configuration updatedConfiguration = repository.getConfiguration().copy();
        updatedConfiguration.setOnline(repositoryXO.getOnline().booleanValue());
        updatedConfiguration.setRoutingRuleId((EntityId)this.toDetachedEntityId(repositoryXO.getRoutingRuleId()));
        updatedConfiguration.setAttributes(repositoryXO.getAttributes());
        return this.asRepository(this.repositoryManager.update(updatedConfiguration));
    }

    private DetachedEntityId toDetachedEntityId(String s) {
        return StringUtils.isBlank((CharSequence)s) ? null : new DetachedEntityId(s);
    }

    @RequiresAuthentication
    @Validate
    public void remove(@NotEmpty String name) throws Exception {
        Repository repository = this.repositoryManager.get(name);
        this.securityHelper.ensurePermitted(new Permission[]{this.adminPermission(repository, "delete")});
        this.repositoryManager.delete(name);
    }

    @RequiresAuthentication
    @Validate
    public String rebuildIndex(@NotEmpty String name) {
        Repository repository = this.repositoryManager.get(name);
        this.securityHelper.ensurePermitted(new Permission[]{this.adminPermission(repository, "edit")});
        TaskConfiguration taskConfiguration = this.taskScheduler.createTaskConfigurationInstance("repository.rebuild-index");
        taskConfiguration.setString("repositoryName", repository.getName());
        TaskInfo taskInfo = this.taskScheduler.submit(taskConfiguration);
        return taskInfo.getId();
    }

    @RequiresAuthentication
    @Validate
    public void invalidateCache(@NotEmpty String name) {
        Repository repository = this.repositoryManager.get(name);
        this.securityHelper.ensurePermitted(new Permission[]{this.adminPermission(repository, "edit")});
        this.repositoryCacheInvalidationService.processCachesInvalidation(repository);
    }

    @VisibleForTesting
    RepositoryXO asRepository(Repository input) {
        RepositoryXO xo = new RepositoryXO();
        xo.setName(input.getName());
        xo.setType(input.getType().getValue());
        xo.setFormat(input.getFormat().getValue());
        xo.setOnline(input.getConfiguration().isOnline());
        xo.setRecipe(input.getConfiguration().getRecipeName());
        xo.setStatus(this.buildStatus(input));
        String routingRuleId = Optional.of(input).map(Repository::getConfiguration).map(Configuration::getRoutingRuleId).map(EntityId::getValue).filter(StringUtils::isNotBlank).orElse("");
        xo.setRoutingRuleId(routingRuleId);
        xo.setAttributes(RepositoryUiService.filterAttributes(input.getConfiguration().copy().getAttributes()));
        xo.setUrl(RepositoryUiService.getUrl(input.getName()));
        return xo;
    }

    private RepositoryXO asRepository(Configuration input) {
        RepositoryXO xo = new RepositoryXO();
        xo.setName(input.getRepositoryName());
        xo.setType(this.getType(input));
        xo.setFormat(this.getFormat(input));
        xo.setSize(this.getSize(input));
        xo.setOnline(input.isOnline());
        xo.setRecipe(input.getRecipeName());
        xo.setStatus(this.buildStatus(input));
        String routingRuleId = Optional.of(input).map(Configuration::getRoutingRuleId).map(EntityId::getValue).filter(StringUtils::isNotBlank).orElse("");
        xo.setRoutingRuleId(routingRuleId);
        xo.setAttributes(RepositoryUiService.filterAttributes(input.copy().getAttributes()));
        xo.setUrl(RepositoryUiService.getUrl(input.getRepositoryName()));
        return xo;
    }

    private static String getUrl(String repositoryName) {
        return BaseUrlHolder.get() + "/repository/" + repositoryName + "/";
    }

    private static Map<String, Map<String, Object>> filterAttributes(Map<String, Map<String, Object>> attributes) {
        Optional.ofNullable(attributes).map(attr -> (Map)attr.get("httpclient")).map(httpclient -> httpclient.get("authentication")).map(Map.class::cast).ifPresent(authentication -> authentication.put("password", PasswordPlaceholder.get()));
        return attributes;
    }

    @RequiresAuthentication
    public List<RepositoryStatusXO> readStatus(Map<String, String> params) {
        return StreamSupport.stream(this.browse().spliterator(), true).map(this::buildStatus).collect(Collectors.toList());
    }

    private RepositoryStatusXO buildStatus(Repository repository) {
        RepositoryStatusXO statusXO = new RepositoryStatusXO();
        statusXO.setRepositoryName(repository.getName());
        statusXO.setOnline(repository.getConfiguration().isOnline());
        if (repository.getType() instanceof ProxyType) {
            try {
                RemoteConnectionStatus remoteStatus = ((HttpClientFacet)repository.facet(HttpClientFacet.class)).getStatus();
                statusXO.setDescription(remoteStatus.getDescription());
                if (remoteStatus.getReason() != null) {
                    statusXO.setReason(remoteStatus.getReason());
                }
            }
            catch (MissingFacetException missingFacetException) {
                // empty catch block
            }
        }
        return statusXO;
    }

    private RepositoryStatusXO buildStatus(Configuration configuration) {
        RepositoryStatusXO statusXO = new RepositoryStatusXO();
        statusXO.setRepositoryName(configuration.getRepositoryName());
        statusXO.setOnline(configuration.isOnline());
        Recipe recipe = this.recipes.get(configuration.getRecipeName());
        if (recipe.getType() instanceof ProxyType) {
            try {
                boolean loaded = StreamSupport.stream(this.repositoryManager.browse().spliterator(), false).anyMatch(repo -> configuration.getRepositoryName().equals(repo.getName()));
                if (loaded) {
                    RemoteConnectionStatus remoteStatus = ((HttpClientFacet)this.repositoryManager.get(configuration.getRepositoryName()).facet(HttpClientFacet.class)).getStatus();
                    statusXO.setDescription(remoteStatus.getDescription());
                    if (remoteStatus.getReason() != null) {
                        statusXO.setReason(remoteStatus.getReason());
                    }
                }
            }
            catch (MissingFacetException missingFacetException) {
                // empty catch block
            }
        }
        return statusXO;
    }

    @VisibleForTesting
    public Iterable<Configuration> filter(@Nullable StoreLoadParameters parameters) {
        Function<Configuration, String> configToFormat = configuration -> this.recipes.get(configuration.getRecipeName()).getFormat().getValue();
        List<Object> configurations = this.configurationStore.list();
        if (parameters != null) {
            String format = parameters.getFilter("format");
            if (format != null && format.indexOf(",") > -1) {
                HashSet formats = Sets.newHashSet((Object[])format.split(","));
                configurations = configurations.stream().filter((? super T configuration) -> formats.contains(configToFormat.apply((Configuration)configuration))).collect(Collectors.toList());
            } else {
                configurations = RepositoryUiService.filterIn(configurations, format, configToFormat);
            }
            String type = parameters.getFilter("type");
            configurations = RepositoryUiService.filterIn(configurations, type, this::getType);
            List<Class<Facet>> facetTypes = this.toFacetList(parameters.getFilter("facets"));
            if (!facetTypes.isEmpty()) {
                configurations = configurations.stream().filter((? super T configuration) -> Optional.ofNullable(this.repositoryManager.get(configuration.getRepositoryName())).map(this.repositoryHasAnyFacet(facetTypes)).orElse(false)).collect(Collectors.toList());
            }
            String versionPolicies = parameters.getFilter("versionPolicies");
            configurations = RepositoryUiService.filterIn(configurations, versionPolicies, configuration -> Optional.of(configuration).map(Configuration::getAttributes).map(attr -> (Map)attr.get("maven")).map(Map.class::cast).map(maven -> maven.get("versionPolicy")).map(String.class::cast).orElse(null));
        }
        configurations = this.repositoryPermissionChecker.userCanBrowseRepositories(configurations.toArray(new Configuration[0]));
        return configurations;
    }

    private Function<Repository, Boolean> repositoryHasAnyFacet(List<Class<Facet>> facetTypes) {
        return repository -> {
            for (Class facetType : facetTypes) {
                try {
                    repository.facet(facetType);
                    return true;
                }
                catch (MissingFacetException missingFacetException) {
                }
            }
            return false;
        };
    }

    private List<Class<Facet>> toFacetList(String facets) {
        if (facets == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(facets.split(",")).stream().filter(StringUtils::isNotBlank).map(arg_0 -> ((GlobalComponentLookupHelper)this.typeLookup).type(arg_0)).map(clazz -> clazz).collect(Collectors.toList());
    }

    private Iterable<Configuration> browse() {
        return this.repositoryPermissionChecker.userHasRepositoryAdminPermissionFor((Iterable)this.configurationStore.list(), new String[]{"read"});
    }

    RepositoryAdminPermission adminPermission(Repository repository, String action) {
        return new RepositoryAdminPermission(repository.getFormat().getValue(), repository.getName(), Collections.singletonList(action));
    }

    private String getFormat(Configuration configuration) {
        Recipe recipe = this.recipes.get(configuration.getRecipeName());
        return recipe.getFormat().getValue();
    }

    private String getType(Configuration configuration) {
        Recipe recipe = this.recipes.get(configuration.getRecipeName());
        return recipe.getType().getValue();
    }

    private Long getSize(Configuration configuration) {
        return this.repositoryMetricsService.flatMap(s -> s.get(configuration.getRepositoryName()).map(repoMetrics -> repoMetrics.totalSize)).orElse(null);
    }

    private static <U> List<U> filterIn(Iterable<U> iterable, String filter, Function<U, String> filteredFieldSelector) {
        if (filter == null) {
            return iterable instanceof List ? (List)iterable : StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
        }
        List<String> filters = Arrays.asList(filter.split(","));
        boolean allExcludes = filters.stream().allMatch(strFilter -> strFilter.startsWith("!"));
        return StreamSupport.stream(iterable.spliterator(), false).filter((? super T result) -> {
            String fieldValue = (String)filteredFieldSelector.apply(result);
            boolean shouldInclude = allExcludes;
            for (String strFilter : filters) {
                if (strFilter.startsWith("!")) {
                    if (!Objects.equals(fieldValue, strFilter.substring(1))) continue;
                    shouldInclude = false;
                    continue;
                }
                if (!Objects.equals(fieldValue, strFilter)) continue;
                shouldInclude = true;
            }
            return shouldInclude;
        }).collect(Collectors.toList());
    }

    public void addRecipe(String format, Recipe recipe) {
        this.recipes.put(format, recipe);
    }
}

