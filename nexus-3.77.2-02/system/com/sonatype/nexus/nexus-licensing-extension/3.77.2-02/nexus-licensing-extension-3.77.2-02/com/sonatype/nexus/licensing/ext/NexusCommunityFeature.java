/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 */
package com.sonatype.nexus.licensing.ext;

import com.sonatype.nexus.licensing.ext.AbstractNexusFeature;
import com.sonatype.nexus.licensing.ext.NexusClmFeature;
import com.sonatype.nexus.licensing.ext.NexusFirewallFeature;
import com.sonatype.nexus.licensing.ext.NexusProfessionalFeature;
import javax.inject.Inject;

public class NexusCommunityFeature
extends AbstractNexusFeature {
    public static final String ID = "NexusCommunity";
    public static final String DESCRIPTION = "Sonatype Nexus Repository Community Edition";
    public static final String NAME = "COMMUNITY";
    public static final String SHORT_NAME = "COMMUNITY";

    @Inject
    public NexusCommunityFeature(NexusClmFeature nexusClmFeature, NexusFirewallFeature nexusFirewallFeature, NexusProfessionalFeature nexusProfessionalFeature) {
        nexusClmFeature.registerSubFeature(this);
        nexusFirewallFeature.registerSubFeature(this);
        nexusProfessionalFeature.registerSubFeature(this);
    }

    public String getId() {
        return ID;
    }

    public String getName() {
        return "COMMUNITY";
    }

    public String getDescription() {
        return DESCRIPTION;
    }

    public String getShortName() {
        return "COMMUNITY";
    }
}

