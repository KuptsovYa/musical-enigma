/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.CaseFormat
 *  com.google.common.base.Function
 *  com.google.common.base.Joiner
 *  com.google.common.base.Preconditions
 *  com.google.common.base.Splitter
 *  com.google.common.base.Strings
 *  com.google.common.collect.Iterables
 *  com.google.common.collect.Lists
 *  javax.ws.rs.core.Response$Status
 *  org.sonatype.nexus.rest.WebApplicationMessageException
 */
package org.sonatype.nexus.security.privilege;

import com.google.common.base.CaseFormat;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;
import org.sonatype.nexus.rest.WebApplicationMessageException;
import org.sonatype.nexus.security.config.CPrivilege;
import org.sonatype.nexus.security.privilege.PrivilegeDescriptor;
import org.sonatype.nexus.security.privilege.rest.ApiPrivilege;
import org.sonatype.nexus.security.privilege.rest.ApiPrivilegeRequest;
import org.sonatype.nexus.security.privilege.rest.ApiPrivilegeWithActionsRequest;
import org.sonatype.nexus.security.privilege.rest.PrivilegeAction;

public abstract class PrivilegeDescriptorSupport<T extends ApiPrivilege, Y extends ApiPrivilegeRequest>
implements PrivilegeDescriptor<T, Y> {
    public static final String ALL = "*";
    public static final String INVALID_ACTIONS = "\"Privilege of type '%s' cannot use action(s) of type '%s'.\"";
    private final String type;

    public PrivilegeDescriptorSupport(String type) {
        this.type = (String)Preconditions.checkNotNull((Object)type);
    }

    @Override
    public String getType() {
        return this.type;
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{type='" + this.type + "'}";
    }

    protected String readProperty(CPrivilege privilege, String name, String defaultValue) {
        String value = privilege.getProperty(name);
        if (Strings.nullToEmpty((String)value).isEmpty()) {
            value = defaultValue;
        }
        return value;
    }

    protected String readProperty(CPrivilege privilege, String name) {
        String value = privilege.getProperty(name);
        Preconditions.checkState((!Strings.nullToEmpty((String)value).isEmpty() ? 1 : 0) != 0, (String)"Missing required property: %s", (Object)name);
        return value;
    }

    protected List<String> readListProperty(CPrivilege privilege, String name, String defaultValue) {
        String value = this.readProperty(privilege, name, defaultValue);
        return Lists.newArrayList((Iterable)Splitter.on((char)',').omitEmptyStrings().trimResults().split((CharSequence)value));
    }

    protected static String humanizeName(String name, String format) {
        if (ALL.equals(name)) {
            if (ALL.equals(format)) {
                return "all";
            }
            return "all '" + format + "'-format";
        }
        return name;
    }

    protected static String humanizeActions(String ... actions) {
        Preconditions.checkArgument((actions.length > 0 ? 1 : 0) != 0);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Joiner.on((String)", ").join(Iterables.transform(Arrays.asList(actions), (Function)new Function<String, String>(){

            public String apply(String action) {
                if (PrivilegeDescriptorSupport.ALL.equals(action)) {
                    return "All";
                }
                return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, action);
            }
        })));
        if (actions.length > 1 || ALL.equals(actions[0])) {
            stringBuilder.append(" privileges");
        } else {
            stringBuilder.append(" privilege");
        }
        return stringBuilder.toString();
    }

    protected void validateActions(ApiPrivilegeWithActionsRequest apiPrivilege, Collection<PrivilegeAction> validActions) {
        HashSet invalidActions = new HashSet();
        apiPrivilege.getActions().stream().forEach(a -> {
            if (!validActions.contains(a)) {
                invalidActions.add(a);
            }
        });
        if (!invalidActions.isEmpty()) {
            String invalidActionNames = String.join((CharSequence)",", invalidActions.stream().map(Enum::name).collect(Collectors.toList()));
            throw new WebApplicationMessageException(Response.Status.BAD_REQUEST, (Object)String.format(INVALID_ACTIONS, this.getType(), invalidActionNames), "application/json");
        }
    }
}

