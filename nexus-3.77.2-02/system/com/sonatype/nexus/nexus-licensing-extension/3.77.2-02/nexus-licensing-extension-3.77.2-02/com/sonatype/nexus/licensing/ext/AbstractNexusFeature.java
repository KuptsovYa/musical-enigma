/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.sonatype.licensing.feature.AbstractFeature
 */
package com.sonatype.nexus.licensing.ext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.sonatype.licensing.feature.AbstractFeature;

public abstract class AbstractNexusFeature
extends AbstractFeature {
    private final Set<AbstractNexusFeature> subFeatures = new HashSet<AbstractNexusFeature>();

    public void registerSubFeature(AbstractNexusFeature subFeature) {
        this.subFeatures.add(subFeature);
    }

    public Set<String> getSubFeatureIds() {
        return this.subFeatures.stream().map(AbstractNexusFeature::getAllFeatureIds).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    private Set<String> getAllFeatureIds() {
        HashSet<String> allFeatures = new HashSet<String>();
        allFeatures.add(this.getId());
        allFeatures.addAll(this.getSubFeatureIds());
        return allFeatures;
    }
}

