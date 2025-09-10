/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Joiner
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableList
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.apache.shiro.authz.Permission
 *  org.sonatype.goodies.i18n.I18N
 *  org.sonatype.goodies.i18n.MessageBundle
 *  org.sonatype.goodies.i18n.MessageBundle$DefaultMessage
 *  org.sonatype.nexus.formfields.FormField
 *  org.sonatype.nexus.formfields.SetOfCheckboxesFormField
 *  org.sonatype.nexus.formfields.StringTextFormField
 */
package org.sonatype.nexus.security.privilege;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.shiro.authz.Permission;
import org.sonatype.goodies.i18n.I18N;
import org.sonatype.goodies.i18n.MessageBundle;
import org.sonatype.nexus.formfields.FormField;
import org.sonatype.nexus.formfields.SetOfCheckboxesFormField;
import org.sonatype.nexus.formfields.StringTextFormField;
import org.sonatype.nexus.security.config.CPrivilege;
import org.sonatype.nexus.security.config.CPrivilegeBuilder;
import org.sonatype.nexus.security.privilege.ApplicationPermission;
import org.sonatype.nexus.security.privilege.Privilege;
import org.sonatype.nexus.security.privilege.PrivilegeDescriptorSupport;
import org.sonatype.nexus.security.privilege.rest.ApiPrivilegeApplication;
import org.sonatype.nexus.security.privilege.rest.ApiPrivilegeApplicationRequest;
import org.sonatype.nexus.security.privilege.rest.PrivilegeAction;

@Named(value="application")
@Singleton
public class ApplicationPrivilegeDescriptor
extends PrivilegeDescriptorSupport<ApiPrivilegeApplication, ApiPrivilegeApplicationRequest> {
    public static final String TYPE = "application";
    public static final String P_DOMAIN = "domain";
    public static final String P_ACTIONS = "actions";
    private static final Messages messages = (Messages)I18N.create(Messages.class);
    private final List<FormField> formFields;
    private static final String P_OPTIONS = "options";

    @Inject
    public ApplicationPrivilegeDescriptor(@Named(value="${nexus.react.privileges:-true}") boolean isReactPrivileges) {
        super(TYPE);
        this.formFields = ImmutableList.of((Object)new StringTextFormField(P_DOMAIN, messages.domain(), messages.domainHelp(), true), (Object)(isReactPrivileges ? new SetOfCheckboxesFormField(P_ACTIONS, messages.actions(), messages.actionsCheckboxesHelp(), true).withAttribute(P_OPTIONS, PrivilegeAction.getCrudTaskActionStrings()) : new StringTextFormField(P_ACTIONS, messages.actions(), messages.actionsHelp(), true, "(^(create|read|update|delete|start|stop|associate|disassociate)(,(create|read|update|delete|start|stop|associate|disassociate)){0,3}$)|(^\\*$)")));
    }

    @Override
    public Permission createPermission(CPrivilege privilege) {
        Preconditions.checkNotNull((Object)privilege);
        String domain = this.readProperty(privilege, P_DOMAIN, "*");
        List<String> actions = this.readListProperty(privilege, P_ACTIONS, "*");
        return new ApplicationPermission(domain, actions);
    }

    @Override
    public List<FormField> getFormFields() {
        return this.formFields;
    }

    @Override
    public String getName() {
        return messages.name();
    }

    public static String id(String domain, String ... actions) {
        return String.format("%s-%s", domain, Joiner.on((char)',').join((Object[])actions));
    }

    public static CPrivilege privilege(String domain, String ... actions) {
        return new CPrivilegeBuilder().type(TYPE).id(ApplicationPrivilegeDescriptor.id(domain, actions)).property(P_DOMAIN, domain).property(P_ACTIONS, actions).create();
    }

    @Override
    public ApiPrivilegeApplication createApiPrivilegeImpl(Privilege privilege) {
        return new ApiPrivilegeApplication(privilege);
    }

    @Override
    public void validate(ApiPrivilegeApplicationRequest apiPrivilege) {
        this.validateActions(apiPrivilege, PrivilegeAction.getCrudTaskAction());
    }

    private static interface Messages
    extends MessageBundle {
        @MessageBundle.DefaultMessage(value="Application")
        public String name();

        @MessageBundle.DefaultMessage(value="Domain")
        public String domain();

        @MessageBundle.DefaultMessage(value="The domain for the privilege")
        public String domainHelp();

        @MessageBundle.DefaultMessage(value="Actions")
        public String actions();

        @MessageBundle.DefaultMessage(value="A comma-delimited list (without whitespace) of actions allowed with this privilege; options include create, read, update, delete, start, stop, associate, disassociate, and a wildcard (*) <a href='https://links.sonatype.com/products/nxrm3/docs/privileges' target='_blank'>Help</a>")
        public String actionsHelp();

        @MessageBundle.DefaultMessage(value="The actions you wish to allow")
        public String actionsCheckboxesHelp();
    }
}

