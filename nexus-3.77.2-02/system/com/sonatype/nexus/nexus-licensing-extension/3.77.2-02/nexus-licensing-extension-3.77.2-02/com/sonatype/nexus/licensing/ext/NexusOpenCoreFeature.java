/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 */
package com.sonatype.nexus.licensing.ext;

import com.sonatype.nexus.licensing.ext.AbstractNexusFeature;
import com.sonatype.nexus.licensing.ext.NexusClmFeature;
import com.sonatype.nexus.licensing.ext.NexusFirewallFeature;
import com.sonatype.nexus.licensing.ext.NexusProfessionalFeature;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named(value="NexusCore")
@Singleton
public class NexusOpenCoreFeature
extends AbstractNexusFeature {
    public static final String ID = "NexusCore";
    public static final String DESCRIPTION = "Sonatype Nexus Repository Core";
    public static final String NAME = "CORE";
    public static final String SHORT_NAME = "CORE";

    @Inject
    public NexusOpenCoreFeature(NexusClmFeature nexusClmFeature, NexusFirewallFeature nexusFirewallFeature, NexusProfessionalFeature nexusProfessionalFeature) {
        nexusClmFeature.registerSubFeature(this);
        nexusFirewallFeature.registerSubFeature(this);
        nexusProfessionalFeature.registerSubFeature(this);
    }

    public String getId() {
        return ID;
    }

    public String getName() {
        return "CORE";
    }

    public String getDescription() {
        return DESCRIPTION;
    }

    public String getShortName() {
        return "CORE";
    }
}

