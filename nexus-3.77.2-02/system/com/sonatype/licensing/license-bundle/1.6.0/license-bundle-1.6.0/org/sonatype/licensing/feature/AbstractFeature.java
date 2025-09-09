/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing.feature;

import java.util.Set;
import org.sonatype.licensing.feature.Feature;

public abstract class AbstractFeature
implements Feature {
    public boolean equals(Object object) {
        if (object == null || !(object instanceof Feature)) {
            return false;
        }
        Feature feature = (Feature)object;
        return this.getId().equals(feature.getId());
    }

    public int hashCode() {
        return this.getId().hashCode();
    }

    public String toString() {
        return "Id: " + this.getId() + ", Name: " + this.getName() + ", Description: " + this.getDescription();
    }

    @Override
    public Set<String> getSubFeatureIds() {
        return null;
    }
}

