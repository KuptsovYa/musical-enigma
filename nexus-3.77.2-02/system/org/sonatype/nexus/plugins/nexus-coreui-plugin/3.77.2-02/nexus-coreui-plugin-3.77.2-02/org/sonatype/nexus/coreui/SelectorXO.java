/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.Pattern
 *  org.sonatype.nexus.selector.UniqueSelectorName
 *  org.sonatype.nexus.validation.group.Create
 *  org.sonatype.nexus.validation.group.Update
 */
package org.sonatype.nexus.coreui;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.sonatype.nexus.selector.UniqueSelectorName;
import org.sonatype.nexus.validation.group.Create;
import org.sonatype.nexus.validation.group.Update;

public class SelectorXO {
    @NotBlank(groups={Update.class})
    private String id;
    @Pattern(regexp="^[a-zA-Z0-9\\-]{1}[a-zA-Z0-9_\\-\\.]*$", message="{org.sonatype.nexus.validation.constraint.name}")
    @NotBlank(groups={Create.class})
    @UniqueSelectorName(groups={Create.class})
    private @Pattern(regexp="^[a-zA-Z0-9\\-]{1}[a-zA-Z0-9_\\-\\.]*$", message="{org.sonatype.nexus.validation.constraint.name}") @NotBlank(groups={Create.class}) String name;
    @NotBlank(groups={Create.class})
    private String type;
    private String description;
    @NotBlank
    private String expression;
    private List<String> usedBy;
    private int usedByCount;

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

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpression() {
        return this.expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public List<String> getUsedBy() {
        return this.usedBy;
    }

    public void setUsedBy(List<String> usedBy) {
        this.usedBy = usedBy;
    }

    public int getUsedByCount() {
        return this.usedByCount;
    }

    public void setUsedByCount(int usedByCount) {
        this.usedByCount = usedByCount;
    }

    public String toString() {
        return "SelectorXO{id='" + this.id + "', name='" + this.name + "', type='" + this.type + "', description='" + this.description + "', expression='" + this.expression + "', usedBy=" + this.usedBy + ", usedByCount=" + this.usedByCount + "}";
    }
}

