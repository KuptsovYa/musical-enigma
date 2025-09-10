/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Named
 *  org.eclipse.sisu.BeanEntry
 *  org.eclipse.sisu.Mediator
 *  org.sonatype.goodies.common.ComponentSupport
 */
package org.sonatype.nexus.security.internal;

import javax.inject.Named;
import org.eclipse.sisu.BeanEntry;
import org.eclipse.sisu.Mediator;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.security.config.SecurityContributor;
import org.sonatype.nexus.security.internal.SecurityConfigurationManagerImpl;

@Named
public class SecurityContributorMediator
extends ComponentSupport
implements Mediator<Named, SecurityContributor, SecurityConfigurationManagerImpl> {
    public void add(BeanEntry<Named, SecurityContributor> entry, SecurityConfigurationManagerImpl watcher) {
        watcher.addContributor((SecurityContributor)entry.getValue());
    }

    public void remove(BeanEntry<Named, SecurityContributor> entry, SecurityConfigurationManagerImpl watcher) {
        watcher.removeContributor((SecurityContributor)entry.getValue());
    }
}

