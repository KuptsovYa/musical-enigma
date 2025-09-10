/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.config;

import org.sonatype.nexus.security.config.SecurityConfiguration;

public interface SecurityConfigurationCleaner {
    public void roleRemoved(SecurityConfiguration var1, String var2);

    public void privilegeRemoved(SecurityConfiguration var1, String var2);
}

