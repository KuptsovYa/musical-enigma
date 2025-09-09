/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 */
package org.sonatype.licensing.feature;

import java.util.Collections;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.licensing.feature.Feature;

@Named(value="licensing.default")
@Singleton
public class Features {
    private Map<String, Feature> bwp;

    @Inject
    public Features(Map<String, Feature> map) {
        this.bwp = map;
    }

    public Map<String, Feature> getAvailableFeatures() {
        return Collections.unmodifiableMap(this.bwp);
    }
}

