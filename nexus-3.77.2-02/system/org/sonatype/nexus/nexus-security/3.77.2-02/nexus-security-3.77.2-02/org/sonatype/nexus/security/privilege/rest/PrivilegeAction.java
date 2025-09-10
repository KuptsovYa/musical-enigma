/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package org.sonatype.nexus.security.privilege.rest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public enum PrivilegeAction {
    READ("read"),
    BROWSE("browse"),
    EDIT("edit"),
    ADD("add"),
    DELETE("delete"),
    RUN("run"),
    START("start"),
    STOP("stop"),
    ASSOCIATE("associate"),
    DISASSOCIATE("disassociate"),
    ALL("*");

    private final String action;
    private static final String CREATE = "create";
    private static final String UPDATE = "update";

    private PrivilegeAction(String action) {
        this.action = action;
    }

    @Nullable
    public String getBreadAction() {
        switch (this) {
            case BROWSE: 
            case READ: 
            case EDIT: 
            case ADD: 
            case DELETE: 
            case ALL: {
                return this.action;
            }
        }
        return null;
    }

    @Nullable
    public String getBreadRunAction() {
        switch (this) {
            case BROWSE: 
            case READ: 
            case EDIT: 
            case ADD: 
            case DELETE: 
            case ALL: 
            case RUN: {
                return this.action;
            }
        }
        return null;
    }

    @Nullable
    public String getCrudAction() {
        switch (this) {
            case ADD: {
                return CREATE;
            }
            case EDIT: {
                return UPDATE;
            }
            case READ: 
            case DELETE: 
            case ALL: 
            case ASSOCIATE: 
            case DISASSOCIATE: {
                return this.action;
            }
        }
        return null;
    }

    @Nullable
    public String getCrudTaskActions() {
        switch (this) {
            case ADD: {
                return CREATE;
            }
            case EDIT: {
                return UPDATE;
            }
            case READ: 
            case DELETE: 
            case ALL: 
            case ASSOCIATE: 
            case DISASSOCIATE: 
            case START: 
            case STOP: {
                return this.action;
            }
        }
        return null;
    }

    @Nullable
    public static PrivilegeAction fromAction(String action) {
        String trimmed;
        switch (trimmed = action.trim()) {
            case "create": {
                return ADD;
            }
            case "update": {
                return EDIT;
            }
        }
        return Arrays.stream(PrivilegeAction.values()).filter(a -> a.action.equals(action)).findFirst().orElse(null);
    }

    public static List<PrivilegeAction> getBreadActions() {
        return Arrays.asList(BROWSE, READ, EDIT, ADD, DELETE, ALL);
    }

    public static List<PrivilegeAction> getBreadRunActions() {
        return Arrays.asList(BROWSE, READ, EDIT, ADD, DELETE, RUN, ALL);
    }

    public static List<PrivilegeAction> getCrudActions() {
        return Arrays.asList(READ, EDIT, ADD, DELETE, ASSOCIATE, DISASSOCIATE, ALL);
    }

    public static List<PrivilegeAction> getCrudTaskAction() {
        return Arrays.asList(READ, EDIT, ADD, DELETE, ASSOCIATE, DISASSOCIATE, START, STOP, ALL);
    }

    public static List<String> getBreadActionStrings() {
        return Arrays.asList(BROWSE, READ, EDIT, ADD, DELETE).stream().map(PrivilegeAction::getBreadAction).collect(Collectors.toList());
    }

    public static List<String> getBreadRunActionStrings() {
        return Arrays.asList(BROWSE, READ, EDIT, ADD, DELETE, RUN).stream().map(PrivilegeAction::getBreadRunAction).collect(Collectors.toList());
    }

    public static List<String> getCrudTaskActionStrings() {
        return Arrays.asList(ADD, READ, EDIT, DELETE, START, STOP, ASSOCIATE, DISASSOCIATE).stream().map(PrivilegeAction::getCrudTaskActions).collect(Collectors.toList());
    }
}

