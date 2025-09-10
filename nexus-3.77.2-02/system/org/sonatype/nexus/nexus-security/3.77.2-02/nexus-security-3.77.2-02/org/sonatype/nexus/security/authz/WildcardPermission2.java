/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.common.base.Joiner
 *  com.google.common.collect.ImmutableSet
 *  org.apache.shiro.authz.permission.WildcardPermission
 */
package org.sonatype.nexus.security.authz;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.shiro.authz.permission.WildcardPermission;

public class WildcardPermission2
extends WildcardPermission {
    private static final boolean CASE_SENSITIVE = true;
    private int cachedHash;
    private static final Joiner JOINER = Joiner.on((char)',');

    protected WildcardPermission2() {
    }

    public WildcardPermission2(String wildcardString) {
        this(wildcardString, false);
    }

    public WildcardPermission2(String wildcardString, boolean caseSensitive) {
        super(wildcardString, caseSensitive);
    }

    protected void setParts(String wildcardString, boolean caseSensitive) {
        super.setParts(wildcardString, caseSensitive);
        this.cachedHash = super.hashCode();
    }

    protected void setParts(List<String> subParts, List<String> actions) {
        this.setParts(subParts, actions, false);
    }

    protected void setParts(List<String> subParts, List<String> actions, boolean caseSensitive) {
        ArrayList<Set<String>> parts = new ArrayList<Set<String>>();
        subParts.forEach(subPart -> parts.add(WildcardPermission2.toPart(subPart, caseSensitive)));
        parts.add(WildcardPermission2.toPart(actions, caseSensitive));
        this.setParts(parts);
        this.cachedHash = super.hashCode();
    }

    @VisibleForTesting
    protected List<Set<String>> getParts() {
        return super.getParts();
    }

    private static Set<String> toPart(String subpart, boolean caseSensitive) {
        return ImmutableSet.of((Object)(caseSensitive ? subpart : subpart.toLowerCase()));
    }

    private static Set<String> toPart(List<String> actions, boolean caseSensitive) {
        if (actions.size() == 1) {
            return WildcardPermission2.toPart(actions.get(0), caseSensitive);
        }
        return (Set)actions.stream().map(action -> caseSensitive ? action : action.toLowerCase()).collect(ImmutableSet.toImmutableSet());
    }

    public int hashCode() {
        return this.cachedHash;
    }

    public String toString() {
        StringBuilder buff = new StringBuilder();
        for (Set<String> part : this.getParts()) {
            if (buff.length() > 0) {
                buff.append(':');
            }
            JOINER.appendTo(buff, part);
        }
        return buff.toString();
    }
}

