/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.inject.BindingAnnotation
 */
package org.sonatype.licensing;

import com.google.inject.BindingAnnotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.sonatype.licensing.feature.Feature;

@Retention(value=RetentionPolicy.RUNTIME)
@BindingAnnotation
@Target(value={ElementType.METHOD})
public @interface RequiresLicense {
    public Class<? extends Feature>[] features();
}

