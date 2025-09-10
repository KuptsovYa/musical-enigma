/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.common.log.LogConfigurationCustomizer
 *  org.sonatype.nexus.common.log.LogConfigurationCustomizer$Configuration
 *  org.sonatype.nexus.common.log.LoggerLevel
 */
package org.sonatype.nexus.coreui;

import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.common.log.LogConfigurationCustomizer;
import org.sonatype.nexus.common.log.LoggerLevel;

@Singleton
@Named
public class LogConfigurationCustomizerImpl
implements LogConfigurationCustomizer {
    public void customize(LogConfigurationCustomizer.Configuration configuration) {
        configuration.setLoggerLevel("org.sonatype.nexus.coreui", LoggerLevel.DEFAULT);
    }
}

