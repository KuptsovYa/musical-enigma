/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  org.apache.shiro.session.mgt.DefaultSessionManager
 *  org.apache.shiro.session.mgt.SessionValidationScheduler
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.apache.shiro.nexus;

import javax.inject.Inject;
import javax.inject.Named;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSessionManager
extends DefaultSessionManager {
    private static final Logger log = LoggerFactory.getLogger(TestSessionManager.class);

    @Inject
    public void configureProperties(@Named(value="${shiro.globalSessionTimeout:-1800000}") long globalSessionTimeout) {
        this.setGlobalSessionTimeout(globalSessionTimeout);
    }

    protected synchronized void enableSessionValidation() {
        SessionValidationScheduler scheduler = this.getSessionValidationScheduler();
        if (scheduler == null) {
            log.info("Global session timeout: {} ms", (Object)this.getGlobalSessionTimeout());
            super.enableSessionValidation();
        }
    }
}

