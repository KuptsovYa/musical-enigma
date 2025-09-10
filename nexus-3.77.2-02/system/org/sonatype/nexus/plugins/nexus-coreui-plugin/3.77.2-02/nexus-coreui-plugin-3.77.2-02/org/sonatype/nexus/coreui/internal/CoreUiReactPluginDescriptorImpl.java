/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.eclipse.sisu.Priority
 *  org.eclipse.sisu.space.ClassSpace
 *  org.sonatype.nexus.ui.UiPluginDescriptor
 *  org.sonatype.nexus.ui.UiUtil
 */
package org.sonatype.nexus.coreui.internal;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.eclipse.sisu.Priority;
import org.eclipse.sisu.space.ClassSpace;
import org.sonatype.nexus.ui.UiPluginDescriptor;
import org.sonatype.nexus.ui.UiUtil;

@Named
@Singleton
@Priority(value=2147483547)
public class CoreUiReactPluginDescriptorImpl
implements UiPluginDescriptor {
    private final List<String> scripts;
    private final List<String> debugScripts;
    private final List<String> styles;

    @Inject
    public CoreUiReactPluginDescriptorImpl(ClassSpace space) {
        this.scripts = Arrays.asList(UiUtil.getPathForFile((String)"nexus-coreui-bundle.js", (ClassSpace)space));
        this.debugScripts = Arrays.asList(UiUtil.getPathForFile((String)"nexus-coreui-bundle.debug.js", (ClassSpace)space));
        this.styles = Arrays.asList(UiUtil.getPathForFile((String)"nexus-coreui-bundle.css", (ClassSpace)space));
    }

    public String getName() {
        return "nexus-coreui-plugin";
    }

    @Nullable
    public List<String> getScripts(boolean isDebug) {
        return isDebug ? this.debugScripts : this.scripts;
    }

    @Nullable
    public List<String> getStyles() {
        return this.styles;
    }
}

