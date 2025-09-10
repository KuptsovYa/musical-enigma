/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.sonatype.nexus.common.entity.EntityId
 *  org.sonatype.nexus.repository.Repository
 *  org.sonatype.nexus.repository.query.PageResult
 *  org.sonatype.nexus.repository.query.QueryOptions
 *  org.sonatype.nexus.repository.security.RepositorySelector
 */
package org.sonatype.nexus.coreui;

import java.util.List;
import java.util.Set;
import org.sonatype.nexus.common.entity.EntityId;
import org.sonatype.nexus.coreui.AssetXO;
import org.sonatype.nexus.coreui.ComponentXO;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.query.PageResult;
import org.sonatype.nexus.repository.query.QueryOptions;
import org.sonatype.nexus.repository.security.RepositorySelector;

public interface ComponentHelper {
    public List<AssetXO> readComponentAssets(Repository var1, ComponentXO var2);

    public PageResult<AssetXO> previewAssets(RepositorySelector var1, List<Repository> var2, String var3, QueryOptions var4);

    public ComponentXO readComponent(Repository var1, EntityId var2);

    public boolean canDeleteComponent(Repository var1, ComponentXO var2);

    public Set<String> deleteComponent(Repository var1, ComponentXO var2);

    public AssetXO readAsset(Repository var1, EntityId var2);

    public boolean canDeleteAsset(Repository var1, EntityId var2);

    public Set<String> deleteAsset(Repository var1, EntityId var2);

    public boolean canDeleteFolder(Repository var1, String var2);

    public void deleteFolder(Repository var1, String var2);
}

