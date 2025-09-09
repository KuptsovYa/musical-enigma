/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Named
 *  javax.inject.Singleton
 */
package com.sonatype.nexus.licensing.ext;

import com.sonatype.nexus.licensing.ext.AbstractNexusFeature;
import javax.inject.Named;
import javax.inject.Singleton;

@Deprecated
@Named(value="Firewall")
@Singleton
public class NexusFirewallFeature
extends AbstractNexusFeature {
    public static final String ID = "Firewall";
    public static final String DESCRIPTION = "Nexus Firewall";
    public static final String NAME = "Firewall";
    public static final String SHORT_NAME = "Firewall";

    public String getDescription() {
        return DESCRIPTION;
    }

    public String getId() {
        return "Firewall";
    }

    public String getName() {
        return "Firewall";
    }

    public String getShortName() {
        return "Firewall";
    }
}

