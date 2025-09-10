/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.NotBlank
 *  org.sonatype.nexus.repository.routing.RoutingMode
 */
package org.sonatype.nexus.coreui;

import java.util.List;
import javax.validation.constraints.NotBlank;
import org.sonatype.nexus.repository.routing.RoutingMode;

public class RoutingRuleTestXO {
    @NotBlank
    private RoutingMode mode;
    @NotBlank
    private List<String> matchers;
    @NotBlank
    private String path;

    public RoutingMode getMode() {
        return this.mode;
    }

    public void setMode(RoutingMode mode) {
        this.mode = mode;
    }

    public List<String> getMatchers() {
        return this.matchers;
    }

    public void setMatchers(List<String> matchers) {
        this.matchers = matchers;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

