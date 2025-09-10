/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Named
 *  org.sonatype.nexus.upgrade.datastore.RepeatableDatabaseMigrationStep
 */
package com.sonatype.nexus.pro.systemchecks.heartbeat.upgrade;

import java.sql.Connection;
import javax.inject.Named;
import org.sonatype.nexus.upgrade.datastore.RepeatableDatabaseMigrationStep;

@Named
public class NodeHeartbeatDatabaseMigrationStep
implements RepeatableDatabaseMigrationStep {
    private static final String RENAME_NODE_ID = "ALTER TABLE node_heartbeat RENAME COLUMN node_id TO heartbeat_id";

    public void migrate(Connection conn) throws Exception {
        if (this.columnExists(conn, "node_heartbeat", "node_id")) {
            this.runStatement(conn, RENAME_NODE_ID);
        }
    }

    public Integer getChecksum() {
        return 1;
    }
}

