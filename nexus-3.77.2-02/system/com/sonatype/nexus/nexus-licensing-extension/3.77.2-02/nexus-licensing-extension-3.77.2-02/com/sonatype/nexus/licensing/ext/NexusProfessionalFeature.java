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

@Named(value="NexusProfessional")
@Singleton
public class NexusProfessionalFeature
extends AbstractNexusFeature {
    public static final String ID = "NexusProfessional";
    public static final String DESCRIPTION = "Sonatype Nexus Repository Professional";
    public static final String NAME = "PRO";
    public static final String SHORT_NAME = "PRO";

    public String getId() {
        return ID;
    }

    public String getName() {
        return "PRO";
    }

    public String getDescription() {
        return DESCRIPTION;
    }

    public String getShortName() {
        return "PRO";
    }
}

