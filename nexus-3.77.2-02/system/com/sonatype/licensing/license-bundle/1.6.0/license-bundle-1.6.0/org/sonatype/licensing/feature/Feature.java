/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing.feature;

import java.util.Set;

public interface Feature {
    public String getId();

    public String getName();

    public String getDescription();

    public String getShortName();

    public Set<String> getSubFeatureIds();
}

