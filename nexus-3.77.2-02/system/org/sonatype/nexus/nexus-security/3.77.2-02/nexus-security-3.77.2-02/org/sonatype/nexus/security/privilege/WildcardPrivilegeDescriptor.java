/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableList
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.apache.shiro.authz.Permission
 *  org.sonatype.goodies.i18n.I18N
 *  org.sonatype.goodies.i18n.MessageBundle
 *  org.sonatype.goodies.i18n.MessageBundle$DefaultMessage
 *  org.sonatype.nexus.formfields.FormField
 *  org.sonatype.nexus.formfields.StringTextFormField
 */
package org.sonatype.nexus.security.privilege;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.shiro.authz.Permission;
import org.sonatype.goodies.i18n.I18N;
import org.sonatype.goodies.i18n.MessageBundle;
import org.sonatype.nexus.formfields.FormField;
import org.sonatype.nexus.formfields.StringTextFormField;
import org.sonatype.nexus.security.authz.WildcardPermission2;
import org.sonatype.nexus.security.config.CPrivilege;
import org.sonatype.nexus.security.config.CPrivilegeBuilder;
import org.sonatype.nexus.security.privilege.Privilege;
import org.sonatype.nexus.security.privilege.PrivilegeDescriptorSupport;
import org.sonatype.nexus.security.privilege.rest.ApiPrivilegeWildcard;
import org.sonatype.nexus.security.privilege.rest.ApiPrivilegeWildcardRequest;

@Named(value="wildcard")
@Singleton
public class WildcardPrivilegeDescriptor
extends PrivilegeDescriptorSupport<ApiPrivilegeWildcard, ApiPrivilegeWildcardRequest> {
    public static final String TYPE = "wildcard";
    public static final String P_PATTERN = "pattern";
    private static final Messages messages = (Messages)I18N.create(Messages.class);
    private final List<FormField> formFields = ImmutableList.of((Object)new StringTextFormField("pattern", messages.pattern(), messages.patternHelp(), true));

    public WildcardPrivilegeDescriptor() {
        super(TYPE);
    }

    @Override
    public Permission createPermission(CPrivilege privilege) {
        Preconditions.checkNotNull((Object)privilege);
        String pattern = this.readProperty(privilege, P_PATTERN);
        return new WildcardPermission2(pattern);
    }

    @Override
    public List<FormField> getFormFields() {
        return this.formFields;
    }

    @Override
    public String getName() {
        return messages.name();
    }

    public static String id(String pattern) {
        return pattern;
    }

    public static CPrivilege privilege(String pattern) {
        return new CPrivilegeBuilder().type(TYPE).id(WildcardPrivilegeDescriptor.id(pattern)).property(P_PATTERN, pattern).create();
    }

    @Override
    public ApiPrivilegeWildcard createApiPrivilegeImpl(Privilege privilege) {
        return new ApiPrivilegeWildcard(privilege);
    }

    @Override
    public void validate(ApiPrivilegeWildcardRequest apiPrivilege) {
    }

    private static interface Messages
    extends MessageBundle {
        @MessageBundle.DefaultMessage(value="Wildcard")
        public String name();

        @MessageBundle.DefaultMessage(value="Privilege String")
        public String pattern();

        @MessageBundle.DefaultMessage(value="The internal segment matching algorithm uses Apache Shiro wildcard permissions")
        public String patternHelp();
    }
}

