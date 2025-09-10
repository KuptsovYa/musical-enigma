/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.shiro.session.Session
 *  org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.apache.shiro.nexus;

import java.io.Serializable;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NexusSessionDAO
extends EnterpriseCacheSessionDAO {
    private static final Logger log = LoggerFactory.getLogger(NexusSessionDAO.class);

    protected Serializable doCreate(Session session) {
        Serializable id = super.doCreate(session);
        log.trace("Created session-id: {} for session: {}", (Object)id, (Object)session);
        return id;
    }
}

