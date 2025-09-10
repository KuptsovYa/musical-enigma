/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.Sets
 */
package org.sonatype.nexus.coreui.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import org.sonatype.nexus.coreui.RepositoryXO;

public class RepositoryCleanupAttributesUtil {
    private static final String CLEANUP_ATTRIBUTES_KEY = "cleanup";
    private static final String CLEANUP_NAME_KEY = "policyName";

    private RepositoryCleanupAttributesUtil() {
    }

    public static void initializeCleanupAttributes(RepositoryXO repositoryXO) {
        Preconditions.checkNotNull((Object)repositoryXO);
        Map attributes = (Map)Preconditions.checkNotNull(repositoryXO.getAttributes());
        Map cleanup = (Map)attributes.get(CLEANUP_ATTRIBUTES_KEY);
        if (Objects.nonNull(cleanup)) {
            Collection policyNames = (Collection)cleanup.get(CLEANUP_NAME_KEY);
            if (Objects.isNull(policyNames) || policyNames.isEmpty()) {
                attributes.remove(CLEANUP_ATTRIBUTES_KEY);
            } else {
                cleanup.put(CLEANUP_NAME_KEY, Sets.newLinkedHashSet((Iterable)policyNames));
            }
        }
    }
}

