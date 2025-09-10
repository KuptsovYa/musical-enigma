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
 *  javax.validation.Valid
 *  javax.validation.constraints.NotNull
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.nexus.extdirect.DirectComponentSupport
 *  org.sonatype.nexus.repository.Repository
 *  org.sonatype.nexus.repository.config.Configuration
 *  org.sonatype.nexus.repository.rest.api.AuthorizingRepositoryManager
 *  org.sonatype.nexus.validation.Validate
 */
package org.sonatype.nexus.coreui;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Preconditions;
import com.softwarementors.extjs.djn.config.annotations.DirectAction;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.nexus.coreui.ProprietaryRepositoriesXO;
import org.sonatype.nexus.coreui.ReferenceXO;
import org.sonatype.nexus.extdirect.DirectComponentSupport;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.config.Configuration;
import org.sonatype.nexus.repository.rest.api.AuthorizingRepositoryManager;
import org.sonatype.nexus.validation.Validate;

@Named
@Singleton
@DirectAction(action={"coreui_ProprietaryRepositories"})
public class ProprietaryRepositoriesComponent
extends DirectComponentSupport {
    private final AuthorizingRepositoryManager repositoryManager;

    @Inject
    public ProprietaryRepositoriesComponent(AuthorizingRepositoryManager authorizingRepositoryManager) {
        this.repositoryManager = (AuthorizingRepositoryManager)Preconditions.checkNotNull((Object)authorizingRepositoryManager);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:settings:read"})
    public ProprietaryRepositoriesXO read() {
        List<String> enabledRepositories = this.repositoryManager.getRepositoriesWithAdmin().stream().filter(ProprietaryRepositoriesComponent::isProprietary).map(Repository::getName).sorted().collect(Collectors.toList());
        ProprietaryRepositoriesXO proprietaryRepositoriesXO = new ProprietaryRepositoriesXO();
        proprietaryRepositoriesXO.setEnabledRepositories(enabledRepositories);
        return proprietaryRepositoriesXO;
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:settings:read"})
    public List<ReferenceXO> readPossibleRepos() {
        return this.repositoryManager.getRepositoriesWithAdmin().stream().filter(ProprietaryRepositoriesComponent::isHosted).map(repo -> new ReferenceXO(repo.getName(), repo.getName())).sorted(Comparator.comparing(ReferenceXO::getName)).collect(Collectors.toList());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:settings:update"})
    @Validate
    ProprietaryRepositoriesXO update(@NotNull @Valid ProprietaryRepositoriesXO proprietaryRepositoriesXO) {
        HashSet<String> shouldBeEnabled = new HashSet<String>(proprietaryRepositoriesXO.getEnabledRepositories());
        this.repositoryManager.getRepositoriesWithAdmin().stream().filter(ProprietaryRepositoriesComponent::isHosted).filter(repo -> !ProprietaryRepositoriesComponent.isProprietary(repo) && shouldBeEnabled.contains(repo.getName()) || ProprietaryRepositoriesComponent.isProprietary(repo) && !shouldBeEnabled.contains(repo.getName())).forEach(repo -> this.setProprietaryStatus((Repository)repo, shouldBeEnabled.contains(repo.getName())));
        return this.read();
    }

    private static boolean isHosted(Repository repository) {
        return "hosted".equals(repository.getType().getValue());
    }

    private static boolean isProprietary(Repository repository) {
        return Boolean.TRUE.equals(repository.getConfiguration().attributes("component").get("proprietaryComponents", Boolean.class));
    }

    private void setProprietaryStatus(Repository repository, boolean status) {
        Configuration newConfig = repository.getConfiguration().copy();
        newConfig.attributes("component").set("proprietaryComponents", (Object)status);
        try {
            this.repositoryManager.update(newConfig);
        }
        catch (Exception e) {
            this.log.error("Error updating proprietary status in repo: {}", (Object)repository.getName(), (Object)e);
            throw new RuntimeException(e);
        }
    }
}

