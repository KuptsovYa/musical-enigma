/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.sonatype.nexus.common.log.LoggerLevel
 */
package org.sonatype.nexus.coreui.internal.log;

import org.sonatype.nexus.common.log.LoggerLevel;

public class UpdateLoggingConfigurationRequest {
    private LoggerLevel level;

    public LoggerLevel getLevel() {
        return this.level;
    }

    public void setLevel(LoggerLevel level) {
        this.level = level;
    }
}

