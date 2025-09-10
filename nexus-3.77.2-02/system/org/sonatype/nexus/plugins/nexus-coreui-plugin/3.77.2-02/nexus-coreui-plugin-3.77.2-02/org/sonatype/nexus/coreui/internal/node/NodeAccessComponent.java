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
 *  org.sonatype.nexus.common.node.NodeAccess
 *  org.sonatype.nexus.extdirect.DirectComponentSupport
 */
package org.sonatype.nexus.coreui.internal.node;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Preconditions;
import com.softwarementors.extjs.djn.config.annotations.DirectAction;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.common.node.NodeAccess;
import org.sonatype.nexus.coreui.internal.node.NodeInfoXO;
import org.sonatype.nexus.extdirect.DirectComponentSupport;

@Named
@Singleton
@DirectAction(action={"node_NodeAccess"})
public class NodeAccessComponent
extends DirectComponentSupport {
    private final NodeAccess nodeAccess;

    @Inject
    public NodeAccessComponent(NodeAccess nodeAccess) {
        this.nodeAccess = (NodeAccess)Preconditions.checkNotNull((Object)nodeAccess);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    public List<NodeInfoXO> nodes() {
        return this.nodeAccess.getMemberAliases().entrySet().stream().map(this::asNodeInfoXO).collect(Collectors.toList());
    }

    private NodeInfoXO asNodeInfoXO(Map.Entry<String, String> entry) {
        NodeInfoXO nodeInfoXO = new NodeInfoXO();
        nodeInfoXO.setName(entry.getKey());
        nodeInfoXO.setLocal(entry.getKey().equals(this.nodeAccess.getId()));
        nodeInfoXO.setDisplayName(entry.getValue());
        return nodeInfoXO;
    }
}

