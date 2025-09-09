/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.inject.BindingAnnotation
 *  org.sonatype.licensing.feature.Feature
 *  org.sonatype.nexus.common.app.ManagedLifecycle$Phase
 */
package com.sonatype.nexus.licensing.ext;

import com.google.inject.BindingAnnotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.sonatype.licensing.feature.Feature;
import org.sonatype.nexus.common.app.ManagedLifecycle;

@Retention(value=RetentionPolicy.RUNTIME)
@BindingAnnotation
@Target(value={ElementType.METHOD})
public @interface RequiresLicenseCheck {
    public Class<? extends Feature>[] features();

    public Frequency frequency() default Frequency.ALWAYS;

    public int numberOfCalls() default 1;

    public ManagedLifecycle.Phase[] phases() default {};

    public static enum Frequency {
        ALWAYS,
        EVERY_N_CALLS;

    }
}

