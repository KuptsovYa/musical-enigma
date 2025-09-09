/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.sonatype.licensing.feature.Feature
 *  org.sonatype.nexus.capability.Condition
 */
package com.sonatype.nexus.licensing.ext.capability;

import org.sonatype.licensing.feature.Feature;
import org.sonatype.nexus.capability.Condition;

public interface LicensingConditions {
    public Condition licenseIsValid(Feature var1);
}

