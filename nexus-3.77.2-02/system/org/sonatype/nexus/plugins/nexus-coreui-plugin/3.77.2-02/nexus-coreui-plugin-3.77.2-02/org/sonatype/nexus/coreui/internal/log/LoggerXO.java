/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 *  org.sonatype.nexus.common.log.LoggerLevel
 */
package org.sonatype.nexus.coreui.internal.log;

import java.util.Map;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.sonatype.nexus.common.log.LoggerLevel;

public class LoggerXO {
    @NotEmpty
    private String name;
    @NotNull
    private LoggerLevel level;
    private boolean override;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LoggerLevel getLevel() {
        return this.level;
    }

    public void setLevel(LoggerLevel level) {
        this.level = level;
    }

    public boolean isOverride() {
        return this.override;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }

    public String toString() {
        return "LoggerXO(name:" + this.name + ", level:" + this.level + ", override:" + this.override + ")";
    }

    public static LoggerXO fromEntry(Map.Entry<String, LoggerLevel> entry) {
        LoggerXO loggerXO = new LoggerXO();
        loggerXO.setName(entry.getKey());
        loggerXO.setLevel(entry.getValue());
        return loggerXO;
    }
}

