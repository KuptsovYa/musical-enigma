/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.shiro.SecurityUtils
 *  org.apache.shiro.subject.Subject
 */
package org.sonatype.nexus.security.subject;

import java.util.function.Supplier;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class CurrentSubjectSupplier
implements Supplier<Subject> {
    @Override
    public Subject get() {
        return SecurityUtils.getSubject();
    }
}

