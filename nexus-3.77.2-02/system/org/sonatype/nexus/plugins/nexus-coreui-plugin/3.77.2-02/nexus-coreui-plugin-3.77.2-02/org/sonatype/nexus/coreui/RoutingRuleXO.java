/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.Pattern
 *  org.sonatype.nexus.repository.routing.RoutingMode
 *  org.sonatype.nexus.validation.group.Create
 */
package org.sonatype.nexus.coreui;

import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.sonatype.nexus.repository.routing.RoutingMode;
import org.sonatype.nexus.validation.group.Create;

public class RoutingRuleXO {
    private String id;
    @Pattern(regexp="^[a-zA-Z0-9\\-]{1}[a-zA-Z0-9_\\-\\.]*$", message="{org.sonatype.nexus.validation.constraint.name}")
    @NotBlank(groups={Create.class})
    private @Pattern(regexp="^[a-zA-Z0-9\\-]{1}[a-zA-Z0-9_\\-\\.]*$", message="{org.sonatype.nexus.validation.constraint.name}") @NotBlank(groups={Create.class}) String name;
    private String description;
    @NotBlank(groups={Create.class})
    private RoutingMode mode;
    @NotBlank
    private List<String> matchers;
    private int assignedRepositoryCount;
    private List<String> assignedRepositoryNames;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public int getAssignedRepositoryCount() {
        return this.assignedRepositoryCount;
    }

    public void setAssignedRepositoryCount(int assignedRepositoryCount) {
        this.assignedRepositoryCount = assignedRepositoryCount;
    }

    public List<String> getAssignedRepositoryNames() {
        return this.assignedRepositoryNames;
    }

    public void setAssignedRepositoryNames(List<String> assignedRepositoryNames) {
        this.assignedRepositoryNames = assignedRepositoryNames;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        RoutingRuleXO that = (RoutingRuleXO)o;
        return Objects.equals(this.name, that.name);
    }

    public int hashCode() {
        return Objects.hash(this.name);
    }

    public String toString() {
        return "RoutingRuleXO{id='" + this.id + "', name='" + this.name + "', description='" + this.description + "', mode=" + this.mode + ", matchers=" + this.matchers + ", assignedRepositoryCount=" + this.assignedRepositoryCount + ", assignedRepositoryNames=" + this.assignedRepositoryNames + "}";
    }
}

