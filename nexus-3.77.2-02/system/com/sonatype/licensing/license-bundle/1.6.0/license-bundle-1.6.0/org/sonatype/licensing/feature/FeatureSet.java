/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing.feature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.sonatype.licensing.feature.Feature;

public class FeatureSet
implements Iterable<Feature> {
    private final List<Feature> dlc = new ArrayList<Feature>();

    public void addFeature(Feature feature) {
        if (feature == null) {
            throw new NullPointerException("feature must not be null!");
        }
        this.dlc.add(feature);
    }

    public boolean hasFeature(Feature feature) {
        HashSet<String> hashSet = new HashSet<String>();
        for (Feature feature2 : this.dlc) {
            hashSet.add(feature2.getId());
            if (feature2.getSubFeatureIds() == null) continue;
            hashSet.addAll(feature2.getSubFeatureIds());
        }
        return hashSet.contains(feature.getId());
    }

    public boolean hasFeatures() {
        return this.dlc.size() > 0;
    }

    List<Feature> zhj() {
        return Collections.unmodifiableList(this.dlc);
    }

    @Override
    public Iterator<Feature> iterator() {
        return this.zhj().iterator();
    }

    public int size() {
        return this.dlc.size();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("FeatureSet{");
        stringBuilder.append("features=").append(this.dlc);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}

