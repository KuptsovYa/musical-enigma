/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.coreui.internal.content;

public class PreviewRepositoryXO {
    private final String id;
    private final String name;

    public PreviewRepositoryXO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}

