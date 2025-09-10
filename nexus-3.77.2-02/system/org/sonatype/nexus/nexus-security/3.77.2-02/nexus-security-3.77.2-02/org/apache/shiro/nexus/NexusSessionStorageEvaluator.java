/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  org.apache.shiro.subject.Subject
 *  org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator
 */
package org.apache.shiro.nexus;

import javax.inject.Inject;
import javax.inject.Named;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.sonatype.nexus.security.anonymous.AnonymousHelper;

public class NexusSessionStorageEvaluator
extends DefaultWebSessionStorageEvaluator {
    @Inject
    @Named(value="${nexus.session.enabled:-true}")
    private boolean sessionsEnabled;

    public boolean isSessionStorageEnabled(Subject subject) {
        if (this.sessionsEnabled) {
            return !AnonymousHelper.isAnonymous(subject) && super.isSessionStorageEnabled(subject);
        }
        return false;
    }
}

