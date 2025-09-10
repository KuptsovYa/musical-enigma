/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.annotation.ExceptionMetered
 *  com.codahale.metrics.annotation.Timed
 *  com.google.common.base.Preconditions
 *  com.softwarementors.extjs.djn.config.annotations.DirectAction
 *  com.softwarementors.extjs.djn.config.annotations.DirectMethod
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.extdirect.DirectComponentSupport
 *  org.sonatype.nexus.rapture.StateContributor
 *  org.sonatype.nexus.repository.upload.UploadConfiguration
 *  org.sonatype.nexus.repository.upload.UploadDefinition
 */
package org.sonatype.nexus.coreui;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Preconditions;
import com.softwarementors.extjs.djn.config.annotations.DirectAction;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.coreui.internal.UploadService;
import org.sonatype.nexus.extdirect.DirectComponentSupport;
import org.sonatype.nexus.rapture.StateContributor;
import org.sonatype.nexus.repository.upload.UploadConfiguration;
import org.sonatype.nexus.repository.upload.UploadDefinition;

@Named
@Singleton
@DirectAction(action={"coreui_Upload"})
public class UploadComponent
extends DirectComponentSupport
implements StateContributor {
    private final UploadService uploadService;
    private final UploadConfiguration configuration;

    @Inject
    public UploadComponent(UploadService uploadService, UploadConfiguration configuration) {
        this.uploadService = (UploadService)((Object)Preconditions.checkNotNull((Object)((Object)uploadService)));
        this.configuration = (UploadConfiguration)Preconditions.checkNotNull((Object)configuration);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    public Collection<UploadDefinition> getUploadDefinitions() {
        return this.uploadService.getAvailableDefinitions().stream().filter(UploadDefinition::isUiUpload).collect(Collectors.toList());
    }

    @Nullable
    public Map<String, Object> getState() {
        return Map.of("upload", this.configuration.isEnabled());
    }
}

