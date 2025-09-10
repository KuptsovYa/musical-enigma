/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotEmpty
 */
package org.sonatype.nexus.security.privilege.rest;

import io.swagger.annotations.ApiModelProperty;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.constraints.NotEmpty;
import org.sonatype.nexus.security.privilege.Privilege;
import org.sonatype.nexus.security.privilege.rest.ApiPrivilege;
import org.sonatype.nexus.security.privilege.rest.PrivilegeAction;

public abstract class ApiPrivilegeWithActions
extends ApiPrivilege {
    public static final String ACTIONS_KEY = "actions";
    @NotEmpty
    @ApiModelProperty(value="A collection of actions to associate with the privilege, using BREAD syntax (browse,read,edit,add,delete,all) as well as 'run' for script privileges.")
    private Collection<PrivilegeAction> actions;

    public ApiPrivilegeWithActions(String privilegeType) {
        super(privilegeType);
    }

    public ApiPrivilegeWithActions(String type, String name, String description, boolean readOnly, Collection<PrivilegeAction> actions) {
        super(type, name, description, readOnly);
        this.actions = actions;
    }

    public ApiPrivilegeWithActions(Privilege privilege) {
        super(privilege);
        String[] parts = privilege.getPrivilegeProperty(ACTIONS_KEY).split(",");
        this.actions = Arrays.stream(parts).map(PrivilegeAction::fromAction).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void setActions(Collection<PrivilegeAction> actions) {
        this.actions = actions;
    }

    public Collection<PrivilegeAction> getActions() {
        return this.actions;
    }

    @Override
    protected Privilege doAsPrivilege(Privilege privilege) {
        privilege.addProperty(ACTIONS_KEY, this.asActionString());
        return privilege;
    }

    private String asActionString() {
        return this.doAsActionString();
    }

    protected abstract String doAsActionString();

    protected String toCrudActionString() {
        if (this.actions == null || this.actions.isEmpty()) {
            return null;
        }
        List actionList = this.actions.stream().map(PrivilegeAction::getCrudAction).filter(Objects::nonNull).collect(Collectors.toList());
        return String.join((CharSequence)",", actionList);
    }

    protected String toBreadActionString() {
        if (this.actions == null || this.actions.isEmpty()) {
            return null;
        }
        List actionList = this.actions.stream().map(PrivilegeAction::getBreadAction).collect(Collectors.toList());
        return String.join((CharSequence)",", actionList);
    }

    protected String toBreadRunActionString() {
        if (this.actions == null || this.actions.isEmpty()) {
            return null;
        }
        List actionList = this.actions.stream().map(PrivilegeAction::getBreadRunAction).collect(Collectors.toList());
        return String.join((CharSequence)",", actionList);
    }
}

