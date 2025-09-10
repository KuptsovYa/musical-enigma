/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.apache.shiro.web.filter.authz.HttpMethodPermissionFilter
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.sonatype.nexus.security.authz;

import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.shiro.web.filter.authz.HttpMethodPermissionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@Singleton
public class NexusHttpMethodPermissionFilter
extends HttpMethodPermissionFilter {
    public static final String NAME = "nx-http-permissions";
    protected final Logger log = LoggerFactory.getLogger(((Object)((Object)this)).getClass());
}

