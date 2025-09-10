/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.coreui.internal.content;

public class SelectorPreviewRequest {
    private String repository = "*";
    private String type = "csel";
    private String expression;

    public String getRepository() {
        return this.repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpression() {
        return this.expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}

