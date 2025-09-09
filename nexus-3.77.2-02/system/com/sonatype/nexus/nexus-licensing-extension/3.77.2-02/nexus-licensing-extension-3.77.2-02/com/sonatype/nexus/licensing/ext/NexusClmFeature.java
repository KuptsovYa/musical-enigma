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

@Named(value="SonatypeCLM")
@Singleton
public class NexusClmFeature
extends AbstractNexusFeature {
    public static final String ID = "SonatypeCLM";

    public String getDescription() {
        return "Nexus IQ Server";
    }

    public String getId() {
        return ID;
    }

    public String getName() {
        return "IQ Server";
    }

    public String getShortName() {
        return "IQ";
    }
}

