/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.annotation.ExceptionMetered
 *  com.codahale.metrics.annotation.Timed
 *  com.google.common.base.Preconditions
 *  com.softwarementors.extjs.djn.config.annotations.DirectAction
 *  com.softwarementors.extjs.djn.config.annotations.DirectMethod
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.common.encoding.EncodingUtil
 *  org.sonatype.nexus.extdirect.DirectComponentSupport
 *  org.sonatype.nexus.repository.Repository
 *  org.sonatype.nexus.repository.browse.node.BrowseNode
 *  org.sonatype.nexus.repository.browse.node.BrowseNodeConfiguration
 *  org.sonatype.nexus.repository.browse.node.BrowseNodeQueryService
 *  org.sonatype.nexus.repository.manager.RepositoryManager
 */
package org.sonatype.nexus.coreui;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Preconditions;
import com.softwarementors.extjs.djn.config.annotations.DirectAction;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.common.encoding.EncodingUtil;
import org.sonatype.nexus.coreui.BrowseNodeXO;
import org.sonatype.nexus.coreui.TreeStoreLoadParameters;
import org.sonatype.nexus.extdirect.DirectComponentSupport;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.browse.node.BrowseNode;
import org.sonatype.nexus.repository.browse.node.BrowseNodeConfiguration;
import org.sonatype.nexus.repository.browse.node.BrowseNodeQueryService;
import org.sonatype.nexus.repository.manager.RepositoryManager;

@Named
@Singleton
@DirectAction(action={"coreui_Browse"})
public class BrowseComponent
extends DirectComponentSupport {
    static final String FOLDER = "folder";
    static final String COMPONENT = "component";
    static final String ASSET = "asset";
    private final BrowseNodeConfiguration configuration;
    private final BrowseNodeQueryService browseNodeQueryService;
    private final RepositoryManager repositoryManager;

    @Inject
    public BrowseComponent(BrowseNodeConfiguration browseNodeConfiguration, BrowseNodeQueryService browseNodeQueryService, RepositoryManager repositoryManager) {
        this.configuration = (BrowseNodeConfiguration)Preconditions.checkNotNull((Object)browseNodeConfiguration);
        this.browseNodeQueryService = (BrowseNodeQueryService)Preconditions.checkNotNull((Object)browseNodeQueryService);
        this.repositoryManager = (RepositoryManager)Preconditions.checkNotNull((Object)repositoryManager);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    public List<BrowseNodeXO> read(TreeStoreLoadParameters treeStoreLoadParameters) {
        String repositoryName = treeStoreLoadParameters.getRepositoryName();
        String path = treeStoreLoadParameters.getNode();
        Repository repository = this.repositoryManager.get(repositoryName);
        List pathSegments = this.isRoot(path) ? Collections.emptyList() : Arrays.stream(path.split("/")).map(EncodingUtil::urlDecode).collect(Collectors.toList());
        return StreamSupport.stream(this.browseNodeQueryService.getByPath(repository, pathSegments, this.configuration.getMaxNodes()).spliterator(), false).map(browseNode -> {
            String encodedPath = EncodingUtil.urlEncode((String)browseNode.getName());
            String type = this.getNodeType((BrowseNode)browseNode);
            return new BrowseNodeXO().withId((String)(this.isRoot(path) ? encodedPath : path + "/" + encodedPath)).withType(type).withText(browseNode.getName()).withLeaf(browseNode.isLeaf()).withComponentId(browseNode.getComponentId() == null ? null : browseNode.getComponentId().getValue()).withAssetId(browseNode.getAssetId() == null ? null : browseNode.getAssetId().getValue()).withPackageUrl(browseNode.getPackageUrl());
        }).collect(Collectors.toList());
    }

    public boolean isRoot(String path) {
        return "/".equals(path);
    }

    private String getNodeType(BrowseNode browseNode) {
        if (browseNode.getAssetId() != null) {
            return ASSET;
        }
        if (browseNode.getComponentId() != null) {
            return COMPONENT;
        }
        return FOLDER;
    }
}

