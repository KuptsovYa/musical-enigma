/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.Streams
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.ws.rs.Consumes
 *  javax.ws.rs.POST
 *  javax.ws.rs.Path
 *  javax.ws.rs.Produces
 *  org.apache.shiro.authz.annotation.Logical
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.repository.Repository
 *  org.sonatype.nexus.repository.manager.RepositoryManager
 *  org.sonatype.nexus.repository.query.PageResult
 *  org.sonatype.nexus.repository.query.QueryOptions
 *  org.sonatype.nexus.repository.security.RepositorySelector
 *  org.sonatype.nexus.rest.Resource
 *  org.sonatype.nexus.selector.SelectorFactory
 */
package org.sonatype.nexus.coreui.internal.content;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.coreui.AssetXO;
import org.sonatype.nexus.coreui.ComponentHelper;
import org.sonatype.nexus.coreui.internal.content.SelectorPreviewRequest;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.manager.RepositoryManager;
import org.sonatype.nexus.repository.query.PageResult;
import org.sonatype.nexus.repository.query.QueryOptions;
import org.sonatype.nexus.repository.security.RepositorySelector;
import org.sonatype.nexus.rest.Resource;
import org.sonatype.nexus.selector.SelectorFactory;

@Named
@Singleton
@Consumes(value={"application/json"})
@Produces(value={"application/json"})
@Path(value="internal/ui/content-selectors")
public class SelectorPreviewResource
extends ComponentSupport
implements Resource {
    static final String RESOURCE_PATH = "internal/ui/content-selectors";
    private final ComponentHelper componentHelper;
    private final RepositoryManager repositoryManager;
    private final SelectorFactory selectorFactory;

    @Inject
    public SelectorPreviewResource(ComponentHelper componentHelper, RepositoryManager repositoryManager, SelectorFactory selectorFactory) {
        this.componentHelper = (ComponentHelper)Preconditions.checkNotNull((Object)componentHelper);
        this.repositoryManager = (RepositoryManager)Preconditions.checkNotNull((Object)repositoryManager);
        this.selectorFactory = (SelectorFactory)Preconditions.checkNotNull((Object)selectorFactory);
    }

    @POST
    @Path(value="/preview")
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:selectors:create", "nexus:selectors:update"}, logical=Logical.OR)
    public PageResult<AssetXO> previewContent(SelectorPreviewRequest request) {
        this.selectorFactory.validateSelector(request.getType().toLowerCase(), request.getExpression());
        RepositorySelector repositorySelector = RepositorySelector.fromSelector((String)request.getRepository());
        List<Repository> selectedRepositories = this.getPreviewRepositories(repositorySelector);
        if (selectedRepositories.isEmpty()) {
            return new PageResult(0L, Collections.emptyList());
        }
        return this.componentHelper.previewAssets(repositorySelector, selectedRepositories, request.getExpression(), new QueryOptions(null, null, null, Integer.valueOf(0), Integer.valueOf(10)));
    }

    private List<Repository> getPreviewRepositories(RepositorySelector repositorySelector) {
        if (!repositorySelector.isAllRepositories()) {
            return ImmutableList.of((Object)this.repositoryManager.get(repositorySelector.getName()));
        }
        if (!repositorySelector.isAllFormats()) {
            return Streams.stream((Iterable)this.repositoryManager.browse()).filter(repository -> repository.getFormat().toString().equals(repositorySelector.getFormat())).collect(Collectors.toList());
        }
        return Streams.stream((Iterable)this.repositoryManager.browse()).collect(Collectors.toList());
    }
}

