/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 *  javax.validation.constraints.Pattern
 *  org.sonatype.nexus.repository.config.UniqueRepositoryName
 *  org.sonatype.nexus.validation.group.Create
 */
package org.sonatype.nexus.coreui;

import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.sonatype.nexus.coreui.RepositoryStatusXO;
import org.sonatype.nexus.repository.config.UniqueRepositoryName;
import org.sonatype.nexus.validation.group.Create;

public class RepositoryXO {
    @Pattern(regexp="^[a-zA-Z0-9\\-]{1}[a-zA-Z0-9_\\-\\.]*$", message="{org.sonatype.nexus.validation.constraint.name}")
    @NotEmpty
    @UniqueRepositoryName(groups={Create.class})
    private @Pattern(regexp="^[a-zA-Z0-9\\-]{1}[a-zA-Z0-9_\\-\\.]*$", message="{org.sonatype.nexus.validation.constraint.name}") @NotEmpty String name;
    private String type;
    private String format;
    private Long size;
    @NotBlank(groups={Create.class})
    private String recipe;
    @NotNull
    private Boolean online;
    private String routingRuleId;
    @NotEmpty
    private Map<String, Map<String, Object>> attributes;
    private String url;
    private RepositoryStatusXO status;

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

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Long getSize() {
        return this.size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getRecipe() {
        return this.recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public Boolean getOnline() {
        return this.online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public String getRoutingRuleId() {
        return this.routingRuleId;
    }

    public void setRoutingRuleId(String routingRuleId) {
        this.routingRuleId = routingRuleId;
    }

    public Map<String, Map<String, Object>> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Map<String, Map<String, Object>> attributes) {
        this.attributes = attributes;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RepositoryStatusXO getStatus() {
        return this.status;
    }

    public void setStatus(RepositoryStatusXO status) {
        this.status = status;
    }

    public String toString() {
        return "RepositoryXO{name='" + this.name + "', type='" + this.type + "', format='" + this.format + "', size=" + this.size + ", recipe='" + this.recipe + "', online=" + this.online + ", routingRuleId='" + this.routingRuleId + "', attributes=" + this.attributes + ", url='" + this.url + "', status=" + this.status + "}";
    }
}

