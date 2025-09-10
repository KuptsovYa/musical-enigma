/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.onboarding;

public interface OnboardingItem {
    public String getType();

    public boolean applies();

    public int getPriority();
}

