/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.shiro.session.Session
 *  org.apache.shiro.session.mgt.SessionContext
 *  org.apache.shiro.session.mgt.SimpleSession
 *  org.apache.shiro.session.mgt.SimpleSessionFactory
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.apache.shiro.nexus;

import java.util.Collections;
import java.util.Map;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.SimpleSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NexusSessionFactory
extends SimpleSessionFactory {
    private static final Logger log = LoggerFactory.getLogger(NexusSessionFactory.class);

    public Session createSession(SessionContext initData) {
        String host;
        log.trace("Creating session w/init-data: {}", (Object)initData);
        if (initData != null && (host = initData.getHost()) != null) {
            return new SimpleSessionImpl(host);
        }
        return new SimpleSessionImpl();
    }

    private static class SimpleSessionImpl
    extends SimpleSession {
        public SimpleSessionImpl() {
        }

        public SimpleSessionImpl(String host) {
            super(host);
        }

        public void setAttributes(Map<Object, Object> attributes) {
            super.setAttributes(attributes != null ? Collections.synchronizedMap(attributes) : null);
        }
    }
}

