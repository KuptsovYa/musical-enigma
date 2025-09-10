/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.shiro.subject.Subject
 */
package org.sonatype.nexus.security.anonymous;

import org.apache.shiro.subject.Subject;
import org.sonatype.nexus.security.anonymous.AnonymousConfiguration;

public interface AnonymousManager {
    public AnonymousConfiguration getConfiguration();

    public AnonymousConfiguration newConfiguration();

    public void setConfiguration(AnonymousConfiguration var1);

    public boolean isEnabled();

    public Subject buildSubject();

    public boolean isConfigured();
}

