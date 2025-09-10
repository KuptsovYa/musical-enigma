/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.base.Splitter
 *  com.google.common.collect.ImmutableList
 */
package org.sonatype.nexus.security.privilege;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.List;
import org.sonatype.nexus.security.authz.WildcardPermission2;

public class ApplicationPermission
extends WildcardPermission2 {
    public static final String SYSTEM = "nexus";
    private static final char PART_SEPARATOR = ':';
    private static final Splitter PART_SPLITTER = Splitter.on((char)':');
    private final String domain;
    private final List<String> actions;

    public ApplicationPermission(String domain, List<String> actions) {
        this.domain = (String)Preconditions.checkNotNull((Object)domain);
        this.actions = (List)Preconditions.checkNotNull(actions);
        if (domain.indexOf(58) < 0) {
            this.setParts((List<String>)ImmutableList.of((Object)SYSTEM, (Object)domain), actions);
        } else {
            this.setParts((List<String>)ImmutableList.builder().add((Object)SYSTEM).addAll(PART_SPLITTER.split((CharSequence)domain)).build(), actions);
        }
    }

    public ApplicationPermission(String domain, String ... actions) {
        this(domain, Arrays.asList(actions));
    }

    public String getDomain() {
        return this.domain;
    }

    public List<String> getActions() {
        return this.actions;
    }
}

