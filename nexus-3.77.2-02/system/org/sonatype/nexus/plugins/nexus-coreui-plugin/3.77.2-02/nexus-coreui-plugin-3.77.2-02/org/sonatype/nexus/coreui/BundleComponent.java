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
 *  org.apache.karaf.bundle.core.BundleInfo
 *  org.apache.karaf.bundle.core.BundleService
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.osgi.framework.Bundle
 *  org.osgi.framework.BundleContext
 *  org.sonatype.nexus.extdirect.DirectComponentSupport
 */
package org.sonatype.nexus.coreui;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Preconditions;
import com.softwarementors.extjs.djn.config.annotations.DirectAction;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.karaf.bundle.core.BundleInfo;
import org.apache.karaf.bundle.core.BundleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.sonatype.nexus.coreui.BundleXO;
import org.sonatype.nexus.extdirect.DirectComponentSupport;

@Named
@Singleton
@DirectAction(action={"coreui_Bundle"})
public class BundleComponent
extends DirectComponentSupport {
    private final BundleContext bundleContext;
    private final BundleService bundleService;

    @Inject
    public BundleComponent(BundleContext bundleContext, BundleService bundleService) {
        this.bundleContext = (BundleContext)Preconditions.checkNotNull((Object)bundleContext);
        this.bundleService = (BundleService)Preconditions.checkNotNull((Object)bundleService);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:bundles:read"})
    public List<BundleXO> read() {
        return Arrays.stream(this.bundleContext.getBundles()).map(bundle -> {
            BundleInfo info = this.bundleService.getInfo(bundle);
            BundleXO entry = new BundleXO().withId(info.getBundleId()).withState(info.getState().name()).withName(info.getName()).withSymbolicName(info.getSymbolicName()).withVersion(info.getVersion()).withLocation(info.getUpdateLocation()).withStartLevel(info.getStartLevel()).withLastModified(bundle.getLastModified()).withFragment(info.isFragment()).withFragments(info.getFragments().stream().map(Bundle::getBundleId).collect(Collectors.toList())).withFragmentHosts(info.getFragmentHosts().stream().map(Bundle::getBundleId).collect(Collectors.toList()));
            LinkedHashMap<String, String> headers = new LinkedHashMap<String, String>();
            Dictionary bundleHeaders = bundle.getHeaders();
            Iterator it = bundleHeaders.keys().asIterator();
            while (it.hasNext()) {
                String key = (String)it.next();
                String value = (String)bundleHeaders.get(key);
                headers.put(key, value);
            }
            entry.withHeaders(headers);
            return entry;
        }).collect(Collectors.toList());
    }
}

