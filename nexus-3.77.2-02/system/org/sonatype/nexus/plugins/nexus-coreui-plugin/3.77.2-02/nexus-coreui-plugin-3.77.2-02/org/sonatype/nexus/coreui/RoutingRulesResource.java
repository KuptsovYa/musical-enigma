/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.Streams
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.ws.rs.Consumes
 *  javax.ws.rs.DELETE
 *  javax.ws.rs.GET
 *  javax.ws.rs.POST
 *  javax.ws.rs.PUT
 *  javax.ws.rs.Path
 *  javax.ws.rs.PathParam
 *  javax.ws.rs.Produces
 *  javax.ws.rs.QueryParam
 *  javax.ws.rs.WebApplicationException
 *  javax.ws.rs.core.Response$Status
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.entity.EntityHelper
 *  org.sonatype.nexus.common.entity.EntityId
 *  org.sonatype.nexus.repository.Repository
 *  org.sonatype.nexus.repository.group.GroupFacet
 *  org.sonatype.nexus.repository.manager.RepositoryManager
 *  org.sonatype.nexus.repository.rest.api.RoutingRulePreviewXO
 *  org.sonatype.nexus.repository.routing.RoutingMode
 *  org.sonatype.nexus.repository.routing.RoutingRule
 *  org.sonatype.nexus.repository.routing.RoutingRuleHelper
 *  org.sonatype.nexus.repository.routing.RoutingRuleStore
 *  org.sonatype.nexus.repository.security.RepositoryPermissionChecker
 *  org.sonatype.nexus.repository.types.GroupType
 *  org.sonatype.nexus.repository.types.ProxyType
 *  org.sonatype.nexus.rest.Resource
 */
package org.sonatype.nexus.coreui;

import com.google.common.base.Preconditions;
import com.google.common.collect.Streams;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.entity.EntityHelper;
import org.sonatype.nexus.common.entity.EntityId;
import org.sonatype.nexus.coreui.RoutingRuleTestXO;
import org.sonatype.nexus.coreui.RoutingRuleXO;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.group.GroupFacet;
import org.sonatype.nexus.repository.manager.RepositoryManager;
import org.sonatype.nexus.repository.rest.api.RoutingRulePreviewXO;
import org.sonatype.nexus.repository.routing.RoutingMode;
import org.sonatype.nexus.repository.routing.RoutingRule;
import org.sonatype.nexus.repository.routing.RoutingRuleHelper;
import org.sonatype.nexus.repository.routing.RoutingRuleStore;
import org.sonatype.nexus.repository.security.RepositoryPermissionChecker;
import org.sonatype.nexus.repository.types.GroupType;
import org.sonatype.nexus.repository.types.ProxyType;
import org.sonatype.nexus.rest.Resource;

@Named
@Singleton
@Consumes(value={"application/json"})
@Produces(value={"application/json"})
@Path(value="internal/ui/routing-rules")
public class RoutingRulesResource
extends ComponentSupport
implements Resource {
    static final String RESOURCE_PATH = "internal/ui/routing-rules";
    private static final String GROUPS = "groups";
    private static final String PROXIES = "proxies";
    private static final boolean ALLOWED = true;
    private final RoutingRuleStore routingRuleStore;
    private final RoutingRuleHelper routingRuleHelper;
    private final RepositoryPermissionChecker repositoryPermissionChecker;
    @Inject
    private RepositoryManager repositoryManager;

    @Inject
    public RoutingRulesResource(RoutingRuleStore routingRuleStore, RoutingRuleHelper routingRuleHelper, RepositoryPermissionChecker repositoryPermissionChecker) {
        this.routingRuleStore = (RoutingRuleStore)Preconditions.checkNotNull((Object)routingRuleStore);
        this.routingRuleHelper = (RoutingRuleHelper)Preconditions.checkNotNull((Object)routingRuleHelper);
        this.repositoryPermissionChecker = (RepositoryPermissionChecker)Preconditions.checkNotNull((Object)repositoryPermissionChecker);
    }

    @POST
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:*"})
    public void createRoutingRule(RoutingRuleXO routingRuleXO) {
        this.routingRuleStore.create(this.fromXO(routingRuleXO));
    }

    @POST
    @Path(value="/test")
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:*"})
    public boolean isAllowed(RoutingRuleTestXO routingRuleTestXO) {
        String path = routingRuleTestXO.getPath();
        List<String> matchers = routingRuleTestXO.getMatchers();
        RoutingMode mode = routingRuleTestXO.getMode();
        return this.routingRuleHelper.isAllowed(mode, matchers, path);
    }

    @GET
    public List<RoutingRuleXO> getRoutingRules(@QueryParam(value="includeRepositoryNames") boolean includeRepositoryNames) {
        this.routingRuleHelper.ensureUserHasPermissionToRead();
        List<RoutingRuleXO> rules = this.routingRuleStore.list().stream().map(RoutingRulesResource::toXO).collect(Collectors.toList());
        if (includeRepositoryNames) {
            this.setAssignedRepositories(rules);
        }
        return rules;
    }

    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:*"})
    private void setAssignedRepositories(List<RoutingRuleXO> rules) {
        Map assignedRepositories = this.routingRuleHelper.calculateAssignedRepositories();
        for (RoutingRuleXO rule : rules) {
            List repositories = assignedRepositories.computeIfAbsent(EntityHelper.id((String)rule.getId()), id -> Collections.emptyList());
            List<String> repositoryNames = this.repositoryPermissionChecker.userHasRepositoryAdminPermission((Iterable)repositories, new String[]{"read"}).stream().map(Repository::getName).sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toList());
            rule.setAssignedRepositoryCount(repositories.size());
            rule.setAssignedRepositoryNames(repositoryNames);
        }
    }

    @GET
    @Path(value="/{name}")
    public RoutingRuleXO getRoutingRule(@PathParam(value="name") String name) {
        this.routingRuleHelper.ensureUserHasPermissionToRead();
        RoutingRuleXO routingRule = RoutingRulesResource.toXO(this.routingRuleStore.getByName(name));
        Map assignedRepositories = this.routingRuleHelper.calculateAssignedRepositories();
        this.populateAssignedRepositoryNames(assignedRepositories, routingRule);
        return routingRule;
    }

    @PUT
    @Path(value="/{name}")
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:*"})
    public void updateRoutingRule(@PathParam(value="name") String name, RoutingRuleXO routingRuleXO) {
        RoutingRule routingRule = this.routingRuleStore.getByName(name);
        if (null == routingRule) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        routingRule.name(routingRuleXO.getName());
        routingRule.description(routingRuleXO.getDescription());
        routingRule.mode(routingRuleXO.getMode());
        routingRule.matchers(routingRuleXO.getMatchers());
        this.routingRuleStore.update(routingRule);
    }

    @DELETE
    @Path(value="/{name}")
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:*"})
    public void deleteRoutingRule(@PathParam(value="name") String name) {
        RoutingRule routingRule = this.routingRuleStore.getByName(name);
        if (null == routingRule) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        Map assignedRepositories = this.routingRuleHelper.calculateAssignedRepositories();
        List repositories = assignedRepositories.getOrDefault(routingRule.id(), Collections.emptyList());
        if (repositories.size() > 0) {
            throw new WebApplicationException("Routing rule is still in use by " + repositories.size() + " repositories.", Response.Status.BAD_REQUEST);
        }
        this.routingRuleStore.delete(routingRule);
    }

    @GET
    @Path(value="/preview")
    @RequiresPermissions(value={"nexus:*"})
    @RequiresAuthentication
    public RoutingRulePreviewXO getRoutingRulesPreview(@QueryParam(value="path") String path, @QueryParam(value="filter") String filter) {
        Map<Class, List<Repository>> repositoriesByType = Streams.stream((Iterable)this.repositoryManager.browse()).collect(Collectors.groupingBy(r -> r.getType().getClass()));
        List<Repository> groupRepositories = repositoriesByType.get(GroupType.class);
        List<Repository> proxyRepositories = repositoriesByType.get(ProxyType.class);
        Map routingRulePathMapping = this.routingRuleStore.list().stream().collect(Collectors.toMap(Function.identity(), rule -> this.routingRuleHelper.isAllowed(rule, path)));
        Stream<Object> repositories = GROUPS.equals(filter) ? groupRepositories.stream() : (PROXIES.equals(filter) ? proxyRepositories.stream() : Stream.of(groupRepositories, proxyRepositories).flatMap(Collection::stream));
        List rootRepositories = repositories.map(repository -> {
            List children = repository.optionalFacet(GroupFacet.class).map(facet -> facet.members()).orElse(null);
            return this.toPreviewXO((Repository)repository, children, routingRulePathMapping);
        }).collect(Collectors.toList());
        return RoutingRulePreviewXO.builder().children(rootRepositories).expanded(Boolean.valueOf(!rootRepositories.isEmpty())).expandable(Boolean.valueOf(true)).build();
    }

    private RoutingRulePreviewXO toPreviewXO(Repository repository, List<Repository> childRepositories, Map<RoutingRule, Boolean> routingRulePathMapping) {
        Optional<RoutingRule> maybeRule = this.getRoutingRule(repository);
        boolean allowed = maybeRule.map(routingRulePathMapping::get).orElse(true);
        String ruleName = maybeRule.map(RoutingRule::name).orElse(null);
        List children = childRepositories == null ? null : childRepositories.stream().map(childRepository -> this.toPreviewXO((Repository)childRepository, null, routingRulePathMapping)).collect(Collectors.toList());
        return RoutingRulePreviewXO.builder().repository(repository.getName()).type(repository.getType().getValue()).format(repository.getFormat().getValue()).allowed(Boolean.valueOf(allowed)).rule(ruleName).children(children).expanded(Boolean.valueOf(children != null && !children.isEmpty())).expandable(Boolean.valueOf(children != null && !children.isEmpty())).build();
    }

    private Optional<RoutingRule> getRoutingRule(Repository repository) {
        return Optional.ofNullable(repository.getConfiguration().getRoutingRuleId()).map(EntityId::getValue).map(arg_0 -> ((RoutingRuleStore)this.routingRuleStore).getById(arg_0));
    }

    private RoutingRule fromXO(RoutingRuleXO routingRuleXO) {
        RoutingRule routingRule = this.routingRuleStore.newRoutingRule();
        routingRule.name(routingRuleXO.getName());
        routingRule.description(routingRuleXO.getDescription());
        routingRule.mode(routingRuleXO.getMode());
        routingRule.matchers(routingRuleXO.getMatchers());
        return routingRule;
    }

    private static RoutingRuleXO toXO(RoutingRule routingRule) {
        RoutingRuleXO routingRuleXO = new RoutingRuleXO();
        routingRuleXO.setId(routingRule.id().getValue());
        routingRuleXO.setName(routingRule.name());
        routingRuleXO.setDescription(routingRule.description());
        routingRuleXO.setMode(routingRule.mode());
        routingRuleXO.setMatchers(routingRule.matchers());
        return routingRuleXO;
    }

    private void populateAssignedRepositoryNames(Map<EntityId, List<Repository>> assignedRepositories, RoutingRuleXO routingRule) {
        List repositories = assignedRepositories.computeIfAbsent(EntityHelper.id((String)routingRule.getId()), id -> Collections.emptyList());
        List<String> repositoryNames = this.repositoryPermissionChecker.userHasRepositoryAdminPermission((Iterable)repositories, new String[]{"read"}).stream().map(Repository::getName).sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toList());
        routingRule.setAssignedRepositoryCount(repositoryNames.size());
        routingRule.setAssignedRepositoryNames(repositoryNames);
    }
}

