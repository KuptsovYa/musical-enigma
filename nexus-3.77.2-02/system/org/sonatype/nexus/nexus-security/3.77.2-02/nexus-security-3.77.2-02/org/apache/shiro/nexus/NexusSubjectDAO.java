/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.shiro.mgt.DefaultSubjectDAO
 *  org.apache.shiro.subject.Subject
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.apache.shiro.nexus;

import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NexusSubjectDAO
extends DefaultSubjectDAO {
    private static final Logger log = LoggerFactory.getLogger(NexusSubjectDAO.class);

    public Subject save(Subject subject) {
        log.trace("Saving: {}", (Object)subject);
        return super.save(subject);
    }
}

