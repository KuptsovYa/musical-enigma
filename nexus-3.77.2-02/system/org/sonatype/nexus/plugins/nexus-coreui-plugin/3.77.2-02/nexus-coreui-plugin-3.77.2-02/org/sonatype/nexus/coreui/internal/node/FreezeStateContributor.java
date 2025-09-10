/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.common.app.FreezeService
 *  org.sonatype.nexus.rapture.StateContributor
 */
package org.sonatype.nexus.coreui.internal.node;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.common.app.FreezeService;
import org.sonatype.nexus.rapture.StateContributor;

@Named
@Singleton
public class FreezeStateContributor
implements StateContributor {
    private final FreezeService freezeService;

    @Inject
    public FreezeStateContributor(FreezeService freezeService) {
        this.freezeService = (FreezeService)Preconditions.checkNotNull((Object)freezeService);
    }

    public Map<String, Object> getState() {
        HashMap<String, Object> state = new HashMap<String, Object>();
        state.put("frozen", this.freezeService.isFrozen());
        state.put("frozenManually", this.freezeService.isFrozenByUser());
        return state;
    }
}

