/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.db.DatabaseCheck
 *  org.sonatype.nexus.rapture.StateContributor
 */
package org.sonatype.nexus.coreui.internal.blobstore;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.db.DatabaseCheck;
import org.sonatype.nexus.rapture.StateContributor;

@Named
@Singleton
public class S3FailoverStateContributor
extends ComponentSupport
implements StateContributor {
    private final DatabaseCheck databaseCheck;
    private final boolean zduEnabled;

    @Inject
    public S3FailoverStateContributor(DatabaseCheck databaseCheck, @Named(value="${nexus.zero.downtime.enabled:-false}") boolean zduEnabled) {
        this.databaseCheck = databaseCheck;
        this.zduEnabled = zduEnabled;
    }

    @Nullable
    public Map<String, Object> getState() {
        return ImmutableMap.of((Object)"S3FailoverEnabled", (Object)this.isAvailable());
    }

    private boolean isAvailable() {
        return !this.zduEnabled || this.databaseCheck.isAtLeast("2.6");
    }
}

