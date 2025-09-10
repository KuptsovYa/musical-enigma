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
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.nexus.common.atlas.SystemInformationGenerator
 *  org.sonatype.nexus.extdirect.DirectComponentSupport
 */
package org.sonatype.nexus.coreui.internal.atlas;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Preconditions;
import com.softwarementors.extjs.djn.config.annotations.DirectAction;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.nexus.common.atlas.SystemInformationGenerator;
import org.sonatype.nexus.extdirect.DirectComponentSupport;

@Named
@Singleton
@DirectAction(action={"atlas_SystemInformation"})
public class SystemInformationComponent
extends DirectComponentSupport {
    private final SystemInformationGenerator systemInformationGenerator;

    @Inject
    public SystemInformationComponent(SystemInformationGenerator systemInformationGenerator) {
        this.systemInformationGenerator = (SystemInformationGenerator)Preconditions.checkNotNull((Object)systemInformationGenerator);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:atlas:read"})
    public Map<String, Object> read() {
        return this.systemInformationGenerator.report();
    }
}

