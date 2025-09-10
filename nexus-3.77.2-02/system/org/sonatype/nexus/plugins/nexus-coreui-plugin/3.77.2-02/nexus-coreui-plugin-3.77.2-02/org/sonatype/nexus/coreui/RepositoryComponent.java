/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.annotation.ExceptionMetered
 *  com.codahale.metrics.annotation.Timed
 *  com.google.common.base.Preconditions
 *  com.softwarementors.extjs.djn.config.annotations.DirectAction
 *  com.softwarementors.extjs.djn.config.annotations.DirectMethod
 *  com.softwarementors.extjs.djn.config.annotations.DirectPollMethod
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.validation.Valid
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 *  javax.validation.groups.Default
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.sonatype.nexus.extdirect.DirectComponentSupport
 *  org.sonatype.nexus.extdirect.model.StoreLoadParameters
 *  org.sonatype.nexus.repository.Format
 *  org.sonatype.nexus.repository.Recipe
 *  org.sonatype.nexus.validation.Validate
 *  org.sonatype.nexus.validation.group.Create
 *  org.sonatype.nexus.validation.group.Update
 */
package org.sonatype.nexus.coreui;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Preconditions;
import com.softwarementors.extjs.djn.config.annotations.DirectAction;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;
import com.softwarementors.extjs.djn.config.annotations.DirectPollMethod;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.sonatype.nexus.coreui.ReferenceXO;
import org.sonatype.nexus.coreui.RepositoryReferenceXO;
import org.sonatype.nexus.coreui.RepositoryStatusXO;
import org.sonatype.nexus.coreui.RepositoryXO;
import org.sonatype.nexus.coreui.search.BrowseableFormatXO;
import org.sonatype.nexus.coreui.service.RepositoryUiService;
import org.sonatype.nexus.extdirect.DirectComponentSupport;
import org.sonatype.nexus.extdirect.model.StoreLoadParameters;
import org.sonatype.nexus.repository.Format;
import org.sonatype.nexus.repository.Recipe;
import org.sonatype.nexus.validation.Validate;
import org.sonatype.nexus.validation.group.Create;
import org.sonatype.nexus.validation.group.Update;

@Named
@Singleton
@DirectAction(action={"coreui_Repository"})
public class RepositoryComponent
extends DirectComponentSupport {
    private final RepositoryUiService repositoryUiService;

    @Inject
    public RepositoryComponent(RepositoryUiService repositoryUiService) {
        this.repositoryUiService = (RepositoryUiService)((Object)Preconditions.checkNotNull((Object)((Object)repositoryUiService)));
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    public List<RepositoryXO> read() {
        return this.repositoryUiService.read();
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    public List<ReferenceXO> readRecipes() {
        return this.repositoryUiService.readRecipes();
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    public List<Format> readFormats() {
        return this.repositoryUiService.readFormats();
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    public List<BrowseableFormatXO> getBrowseableFormats() {
        return this.repositoryUiService.getBrowseableFormats();
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    public List<RepositoryReferenceXO> readReferences(@Nullable StoreLoadParameters parameters) {
        return this.repositoryUiService.readReferences(parameters);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    public List<RepositoryReferenceXO> readReferencesAddingEntryForAll(@Nullable StoreLoadParameters parameters) {
        return this.repositoryUiService.readReferencesAddingEntryForAll(parameters);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    public List<RepositoryReferenceXO> readReferencesAddingEntriesForAllFormats(@Nullable StoreLoadParameters parameters) {
        return this.repositoryUiService.readReferencesAddingEntriesForAllFormats(parameters);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @Validate(groups={Create.class, Default.class})
    public RepositoryXO create(@NotNull @Valid RepositoryXO repositoryXO) throws Exception {
        return this.repositoryUiService.create(repositoryXO);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @Validate(groups={Update.class, Default.class})
    public RepositoryXO update(@NotNull @Valid RepositoryXO repositoryXO) throws Exception {
        return this.repositoryUiService.update(repositoryXO);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @Validate
    public void remove(@NotEmpty String name) throws Exception {
        this.repositoryUiService.remove(name);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @Validate
    public String rebuildIndex(@NotEmpty String name) {
        return this.repositoryUiService.rebuildIndex(name);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @Validate
    public void invalidateCache(@NotEmpty String name) {
        this.repositoryUiService.invalidateCache(name);
    }

    @Timed
    @ExceptionMetered
    @DirectPollMethod(event="coreui_Repository_readStatus")
    @RequiresAuthentication
    public List<RepositoryStatusXO> readStatus(Map<String, String> params) {
        return this.repositoryUiService.readStatus(params);
    }

    public void addRecipe(String format, Recipe recipe) {
        this.repositoryUiService.addRecipe(format, recipe);
    }

    public RepositoryUiService getRepositoryUiService() {
        return this.repositoryUiService;
    }
}

